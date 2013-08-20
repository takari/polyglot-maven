/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.scala

import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import java.io.{StringWriter, InputStream, StringReader, File}
import scala.collection.JavaConverters._
import org.codehaus.plexus.util.xml.Xpp3DomBuilder
import org.apache.maven.model.building.{ModelSource, ModelProcessor}
import org.specs2.specification.AfterExample
import org.apache.maven.model.Model
import org.codehaus.plexus.util.{IOUtil, FileUtils}
import java.util.Collections

@RunWith(classOf[JUnitRunner])
class ScalaModelReaderWriterSpec extends Specification with AfterExample {

  val evalFile = File.createTempFile("ScalaModelReaderSpec", "")
  evalFile.createNewFile()

  val modelSource = new ModelSource {
    def getInputStream(): InputStream = null

    def getLocation(): String = evalFile.getCanonicalPath
  }

  val options = Map(ModelProcessor.SOURCE -> modelSource).asJava
  val reader = new ScalaModelReader
  val writer = new ScalaModelWriter

  def readScalaModel(pomFile: String): Model = {
    val is = getClass.getClassLoader.getResourceAsStream(pomFile)
    reader.read(is, options)
  }

  def readWriteAndCompare(f: String): Boolean = {
    val m = readScalaModel(f)
    val sw = new StringWriter
    writer.write(sw, Collections.emptyMap[String, AnyRef](), m)
    sw.toString must_== IOUtil.toString(getClass.getClassLoader.getResourceAsStream(f))
  }

  def after: Unit = {
    evalFile.delete()
  }

  sequential

  "The reader" should {
    "read, write and compare a minimal pom" in {
      readWriteAndCompare("minimal-pom.scala")
    }
    "read, write and compare a full pom" in {
      readWriteAndCompare("maximum-props-pom.scala")
    }
    "read, write and compare a typical pom" in {
      readWriteAndCompare("typical-pom.scala")
    }
  }
}
