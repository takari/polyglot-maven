/*
 * Copyright (C) 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sonatype.maven.polyglot.scala

import org.sonatype.maven.polyglot.scala.model.Model

import scala.tools.nsc.{GenericRunnerSettings, CompileClient, Settings, CompileSocket, Global, Properties}
import scala.tools.nsc.reporters.ConsoleReporter
import scala.tools.nsc.io.{Directory, File, Path, PlainFile}
import scala.tools.nsc.util.{CompoundSourceFile, BatchSourceFile, SourceFile, SourceFileFragment}
import scala.tools.nsc.util.ScalaClassLoader

import java.io.{InputStream, OutputStream, BufferedReader, FileInputStream, FileOutputStream,
  FileReader, InputStreamReader, PrintWriter, FileWriter, IOException, Reader, StringWriter,
  Writer, File => JFile}
import java.util.jar.{JarEntry, JarOutputStream}
import java.net.URL

import org.apache.maven.model.{Model => ApacheModelBaseClass}

import org.codehaus.plexus.logging.Logger

/**
 * @since 0.7
 */
object PMavenScriptCompiler {

  /**
   * Utility method to make sure something happens, even if there's a global
   * failure of some sort.
   **/
  private def addShutdownHook(body: => Unit) =
    Runtime.getRuntime addShutdownHook new Thread { override def run { body } }

  /**
   * Statically-defined code inserted before PMaven script, which effectively
   * makesth e script code the bod yof the generateModel method of an object
   * named ModelGenerator.
   */
  def preambleCode: String = """
    | import org.sonatype.maven.polyglot.scala.model._
    |
    | object ModelGenerator {
    |   def generateModel: Model = {
  """.stripMargin
  
  def endCode: String = """
    |   }
    | }
  """.stripMargin
  
  def copyStreams(in: InputStream, out: OutputStream) = {
    val buf = new Array[Byte](10240)
    
    def loop: Unit = in.read(buf, 0, buf.length) match {
      case -1 => in.close()
      case n  => out.write(buf, 0, n) ; loop
    }
    
    loop
  }
  
  def copyReaders(in: Reader, out: Writer) = {
    val buf = new Array[Char](10240)
    
    def loop: Unit = in.read(buf, 0, buf.length) match {
      case -1 => in.close()
      case n  => out.write(buf, 0, n) ; loop
    }
    
    loop
  }  

  private def jarFileFor(scriptFile: String): File = {
    val name =
      if (scriptFile endsWith ".jar") scriptFile
      else scriptFile + ".jar"
    
    File(name)
  }
  
  private def tryMakeJar(jarFile: File, sourcePath: Directory) = {
    def addFromDir(jar: JarOutputStream, dir: Directory, prefix: String) {
      def addFileToJar(entry: File) = {
        jar putNextEntry new JarEntry(prefix + entry.name)
        copyStreams(entry.inputStream, jar)
        jar.closeEntry
      }

      dir.list foreach { entry =>
        if (entry.isFile) addFileToJar(entry.toFile)
        else addFromDir(jar, entry.toDirectory, prefix + entry.name + "/")
      }
    }
    
    try {
      val jar = new JarOutputStream(jarFile.outputStream())
      addFromDir(jar, sourcePath, "")
      jar.close
    } 
    catch {
      case _: Error => println("jarfile creation error"); jarFile.delete() // XXX what errors to catch?
    }
  }
  
  /**
   * Locates a JAR file or directory housing the given Class' classfile. Uses the
   * location of he class' classfile, and assumes the normal Java JAR file
   * URL protocol string <code>"jar:&lt;jar location>!/package/ClassName.class"</code>,
   * and extracts the <code>&lt;jar location></code> part from it. If thats not right,
   * then looks for the appropriate parent dir to put on a classpath that would correctly
   * house the classfile.
   *
   * @returns File of the JAR file housing the given Class' classfile.
   **/
  def scalaCPFileFor(clazz: java.lang.Class[_]): JFile = {
    val fileRelativeUri = "/" + clazz.getName.replace('.', '/') + ".class"
    val urlAppClass = clazz.getResource(fileRelativeUri)
    
    urlAppClass.getProtocol match {
      case "jar" =>
        val bangIndex = urlAppClass.toString indexOf ('!')
        val fileUrl = new URL(urlAppClass.toString take bangIndex drop ("jar:".length))
        new JFile(fileUrl.getFile)
      
      case "file" =>
        val uriIndex = urlAppClass.toString indexOf fileRelativeUri
        val parentFileUrl = new URL(urlAppClass.toString take uriIndex)
        new JFile(parentFileUrl.getFile)
 
      case _ => null
    }
  }

  /**
   * Compile a script using the fsc compilation deamon.
   */
  private def compileWithDaemon(
      settings: GenericRunnerSettings,
      scriptFileIn: String): Boolean =
  {
    val scriptFile = CompileClient absFileName scriptFileIn
    
    {
      import settings._
      for (setting <- List(classpath, sourcepath, bootclasspath, extdirs, outdir)) {
        setting.value = CompileClient absFileName setting.value
      }
    }
      
    val compSettingNames  = new Settings(error).allSettings map (_.name)
    val compSettings      = settings.allSettings filter (compSettingNames contains _.name)
    val coreCompArgs      = compSettings flatMap (_.unparse)
    val compArgs          = coreCompArgs ::: List("-Xscript", "ModelGenerator", scriptFile)
    var compok            = true
        
    def ManagedResource[T](x: => T) = Some(x)
    
    for {
      socket <- ManagedResource(CompileSocket getOrCreateSocket "")
      val _ = if (socket == null) return false
      out <- ManagedResource(new PrintWriter(socket.getOutputStream(), true))
      in <- ManagedResource(new BufferedReader(new InputStreamReader(socket.getInputStream())))
    } {
      out println (CompileSocket getPassword socket.getPort)
      out println (compArgs mkString "\0")
      
      for (fromServer <- (Iterator continually in.readLine()) takeWhile (_ != null)) {
        Console.err println fromServer
        if (CompileSocket.errorPattern matcher fromServer matches)
          compok = false
      }
      in.close() ; out.close() ; socket.close()
    }
    
    compok
  }

  /**
   * Wrap a script file into a model generator object named
   * <code>ModelGenerator</code>.
   */
  def wrappedScript(
    filename: String, 
    getSourceFile: (PlainFile) => BatchSourceFile): SourceFile = 
  {
    val preamble = new BatchSourceFile("<script preamble>", preambleCode toCharArray)
    val middle = {
      val bsf = getSourceFile(PlainFile fromPath filename)
      new SourceFileFragment(bsf, 0, bsf.length)
    }
    val end = new BatchSourceFile("<script trailer>", endCode.toCharArray)

    new CompoundSourceFile(preamble, middle, end)
  }

  /**
   * Compile a script and return the compiled jar file location
   * @returns Location of the compiled script
   */
  private def compileScript(
    logger: Logger,
    settings: GenericRunnerSettings,
    scriptFile: String): Option[String] =
  {
    /** Compiles the script file, and returns the directory with the compiled
     *  class files, if the compilation succeeded.
     */
    def compile: Option[Directory] = {
      val compiledPath = Directory makeTemp "scalatemp"

      // delete the directory after the user code has finished
      addShutdownHook(compiledPath.deleteRecursively())

      settings.outdir.value = compiledPath.path

      //...need a different reporter -- one which reports to the Plexus logger...
      val reporter = new ConsoleReporter(settings)
      val compiler = new Global(settings, reporter)
      val cr = new compiler.Run
      val wrapped = wrappedScript(scriptFile, compiler getSourceFile _)
        
      cr compileSources List(wrapped)
      if (reporter.hasErrors) None else Some(compiledPath)
    }

    if (settings.savecompiled.value) {
      val jarFile = jarFileFor(scriptFile)
      def jarOK   = jarFile.canRead && (jarFile isFresher File(scriptFile))
      
      def recompile() = {
        jarFile.delete()
        
        compile match {
          case Some(compiledPath) =>
            tryMakeJar(jarFile, compiledPath)
            if (jarOK) {
              compiledPath.deleteRecursively()
              Some(jarFile.toAbsolute.path)
            }            
            else None
          case _  => None
        }
      }

      if (jarOK) Some(jarFile.toAbsolute.path) // pre-compiled jar is current
      else recompile()                         // jar old - recompile the script.
    }
    // don't use a cache jar at all--just use the class files--only here for
    // debugging/experimentation purposes since in production jars will always
    // be used.
    else compile map (cp => Some(cp.path)) getOrElse None
  }

  /** 
   * <p>
   * Compiles a temporary script file named "ModelGenerator.scala". Compiles the script in
   * to ModelGenerator.jar. Make sure the "-savecompiled" generic runner
   * setting is turned "no", or else the resultant .jar file will silently be
   * deleted.
   * </p>
   *
   * <p>
   * The Settings object should have: output dir set, and the "savecompiled" flag
   * turned on. All testing os done on this basis, and there's no intent for
   * predictable behavior for this method otherwise.
   * </p>
   * 
   * @returns the compile script location as a file path name.
   */
  def compileDSL(
    logger: Logger,
    settings: GenericRunnerSettings,
    reader: Reader) : Option[String] = {
    val buffWriter = new StringWriter
    copyReaders(reader, buffWriter)
        
    // stream the reader to a temp file
    val scriptFile = new File(new JFile("ModelGenerator"))
    scriptFile writeAll List(buffWriter.toString)
    
    // augment the compiler classpath with the Scala std libs and the cwd
    val cp = List(
      scalaCPFileFor(classOf[Application]), // std Scala lib jarfile
      scalaCPFileFor(classOf[Global]),      // Scala compiler lib jarfile
      scalaCPFileFor(classOf[ApacheModelBaseClass]),
      scalaCPFileFor(classOf[Model]),
      scalaCPFileFor(classOf[org.codehaus.plexus.util.xml.Xpp3Dom])
    )
    settings.classpath.value = cp map {_.getCanonicalPath} mkString JFile.pathSeparator
    
    try compileScript(logger, settings, scriptFile.path)
    finally scriptFile.delete()  // in case there was a compilation error
  }
  
  /**
   * <p>
   * Loads the ModelGenerator object defined in the compiled code identified by
   * the <code>location</code> parameter. The parameter value should be a value
   * returned from the <code>compileDSL</code> method. The returned
   * object will conform to the <code>modelGenerator</code> type (also defined
   * in this class).
   * </p>
   **/
  def loadModelGenerator(location: String): Option[modelGenerator] = {
    val libList = List(
        new JFile(location).toURL,
        (PMavenScriptCompiler.scalaCPFileFor(classOf[Application]).toURL),
        (PMavenScriptCompiler.scalaCPFileFor(classOf[org.apache.maven.model.Model]).toURL),
        (PMavenScriptCompiler.scalaCPFileFor(classOf[org.codehaus.plexus.util.xml.Xpp3Dom]).toURL)
    )
    val parentcl = PMavenScriptCompiler.getClass.getClassLoader
    
    val ocls: Option[Class[_]] = new ScalaClassLoader.URLClassLoader(libList, parentcl) tryToLoadClass ("ModelGenerator$")
    
    def modelGeneratorFromClass(cls: Class[_]): modelGenerator = {
      cls.getField("MODULE$").get(null).asInstanceOf[modelGenerator]
    }
    
    ocls map { modelGeneratorFromClass(_) }
  }
  
  /**
   * This structural type defines the API of the PMaven Scala DSL object compiled
   * by the <code>compileDSL</code> method.
   **/
  type modelGenerator = {
    def generateModel: ApacheModelBaseClass
  }
}
