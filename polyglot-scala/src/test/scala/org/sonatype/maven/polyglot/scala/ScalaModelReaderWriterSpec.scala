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
import java.io.{StringWriter, InputStream, File}
import scala.collection.JavaConverters._
import org.apache.maven.model.building.{ModelSource, ModelProcessor}
import org.specs2.specification.AfterExample
import org.apache.maven.model.Model
import org.codehaus.plexus.util.IOUtil
import java.util.Collections
import org.sonatype.maven.polyglot.execute.{ExecuteContext, ExecuteTask, ExecuteManager}
import java.util
import org.sonatype.maven.polyglot.scala.model.{Build => ScalaBuild, Model => ScalaRawModel, Task => ScalaModelTask}
import scala.collection.{immutable, mutable}
import org.apache.maven.project.MavenProject

@RunWith(classOf[JUnitRunner])
class ScalaModelReaderWriterSpec extends Specification with AfterExample {

  val evalFile = File.createTempFile("ScalaModelReaderSpec", "")
  evalFile.createNewFile()

  val modelSource = new ModelSource {
    def getInputStream: InputStream = null

    def getLocation: String = evalFile.getCanonicalPath
  }

  val options = Map(ModelProcessor.SOURCE -> modelSource).asJava

  object TestExecuteManager extends ExecuteManager {
    private val modelTasks = mutable.Map[Model, (Boolean, util.List[ExecuteTask])]()

    def register(model: Model, tasks: util.List[ExecuteTask]): Unit = modelTasks.put(model, (false, tasks))

    def getTasks(model: Model): util.List[ExecuteTask] = {
      val attributedTasks = modelTasks.get(model).get
      if (attributedTasks._1) attributedTasks._2 else List[ExecuteTask]().asJava
    }

    def install(model: Model, options: java.util.Map[String, _]) {
      val attributedTasks = modelTasks.get(model).get
      modelTasks.put(model, (true, attributedTasks._2))
    }

    def install(model: Model) {
      val attributedTasks = modelTasks.get(model).get
      modelTasks.put(model, (true, attributedTasks._2))
    }

    def reset(): Unit = modelTasks.clear()
  }

  val reader = new ScalaModelReader(TestExecuteManager)
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
    TestExecuteManager.reset()
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
    "prettify a task properly" in {
      import model._
      import ScalaPrettyPrinter._

      val m = ScalaRawModel(
        "someGroupId" % "someArtifactId" % "someVersion",
        ScalaBuild(
          tasks = immutable.Seq(
            ScalaModelTask("someId", "somePhase") {
              ec => println("here I am")
            }
          )
        )
      )

      val pp = ScalaPrettyPrinter.pretty(m.asDoc)

      pp must_== """Model(
                   |  "someGroupId" % "someArtifactId" % "someVersion",
                   |  build = Build(
                   |    tasks = Seq(
                   |      Task(
                   |        id = "someId",
                   |        phase = "somePhase"
                   |      ) {compiled code}
                   |    )
                   |  )
                   |)""".stripMargin
    }
    "prettify a task with a profile" in {
      import model._
      import ScalaPrettyPrinter._

      val m = ScalaRawModel(
        "someGroupId" % "someArtifactId" % "someVersion",
        ScalaBuild(
          tasks = immutable.Seq(
            ScalaModelTask("someId", "somePhase", "someProfileId") {
              ec => println("here I am")
            }
          )
        )
      )

      val pp = ScalaPrettyPrinter.pretty(m.asDoc)

      pp must_== """Model(
                   |  "someGroupId" % "someArtifactId" % "someVersion",
                   |  build = Build(
                   |    tasks = Seq(
                   |      Task(
                   |        id = "someId",
                   |        phase = "somePhase",
                   |        profileId = "someProfileId"
                   |      ) {compiled code}
                   |    )
                   |  )
                   |)""".stripMargin
    }
    "register a task properly and prove that it executes" in {
      val m = readScalaModel("tasks-pom.scala")
      val tasks = TestExecuteManager.getTasks(m).asScala
      tasks.size must_== 1
      tasks.head.getId must_== "someId"
      tasks.head.getPhase must_== "compile"
      val project = new MavenProject
      val ec = new ExecuteContext {
        def getProject: MavenProject = project

        def basedir: java.io.File = project.getBasedir

        def log: org.apache.maven.plugin.logging.Log = null
      }
      tasks.head.execute(ec)
      project.getArtifactId must_== "We executed!"
    }
    "format a configuration with an attribute properly" in {
      import model._
      import ScalaPrettyPrinter._

      val m = ScalaRawModel(
        "someGroupId" % "someArtifactId" % "someVersion",
        ScalaBuild(
          plugins = immutable.Seq(
            Plugin(
              "someGroupId" % "someArtifactId" % "someVersion",
              extensions = true,
              executions = immutable.Seq(
                Execution(
                  configuration = Config(
                    `@someattr` = "someAttr",
                    someValue = "someValue"
                  )
                )
              )
            )
          )
        )
      )

      val pp = ScalaPrettyPrinter.pretty(m.asDoc)

      pp must_== """Model(
                   |  "someGroupId" % "someArtifactId" % "someVersion",
                   |  build = Build(
                   |    plugins = Seq(
                   |      Plugin(
                   |        "someGroupId" % "someArtifactId" % "someVersion",
                   |        extensions = true,
                   |        executions = Seq(
                   |          Execution(
                   |            configuration = Config(
                   |              `@someattr` = "someAttr",
                   |              someValue = "someValue"
                   |            )
                   |          )
                   |        )
                   |      )
                   |    )
                   |  )
                   |)""".stripMargin
    }
  }
}
