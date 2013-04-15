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

import org.junit.runner.RunWith
import org.scalatest._
import matchers.ShouldMatchers
import junit.JUnitRunner
import scala.tools.nsc.Global
import scala.tools.nsc.util.ScalaClassLoader
import org.sonatype.maven.polyglot.scala.model.Model

@RunWith(classOf[JUnitRunner])
class PMavenScriptCompilerSpec extends WordSpec with ShouldMatchers {

  "PMavenScriptCompiler.scalaJarFile" when {
  
    "called with the standard Scala Application class" should {

      "return a valid File" in {
        val file = PMavenScriptCompiler.scalaCPFileFor(classOf[Application])
        file should not be(null)
        file.exists should be (true)
        file.toString.endsWith("jar") should be(true)
        
        (ScalaClassLoader classExists (List(file.toURL), "scala.Application")) should be(true)
      }
    }

    "called with a Scala Compiler class" should {

      "return a valid URL" in {
        val file = PMavenScriptCompiler.scalaCPFileFor(classOf[Global])
        file should not be(null)
        file.exists should be (true)
        file.toString.endsWith("jar") should be(true)

        //...need to make sure Scala stand lib AND the compiler lib are
        //   loaded and visible through a single class loader...
        (ScalaClassLoader classExists (List(
                 (PMavenScriptCompiler.scalaCPFileFor(classOf[Application]).toURL),
                 file.toURL), "scala.tools.nsc.Global")) should be(true)
      }
    }
    
    "called with a PMaven Scala Model class" should {

      "return a valid URL" in {
        val file = PMavenScriptCompiler.scalaCPFileFor(classOf[Model])
        file should not be(null)
        file.exists should be (true)
        file.isDirectory should be(true)

        //...need to make sure Scala stand lib AND the Maven model lib AND
        //   target PMaven Scala lib are loaded and visible through a single class loader...
        (ScalaClassLoader classExists (List(
                 (PMavenScriptCompiler.scalaCPFileFor(classOf[Application]).toURL),
                 (PMavenScriptCompiler.scalaCPFileFor(classOf[org.apache.maven.model.Model]).toURL),
                 file.toURL),
            "org.sonatype.maven.polyglot.scala.model.Model")) should be(true)
      }
    }

    "called with a Plexus Util class" should {

      "return a valid URL" in {
        val file = PMavenScriptCompiler.scalaCPFileFor(classOf[Model])
        file should not be(null)
        file.exists should be (true)
        file.isDirectory should be(true)

        //...need to make sure Scala stand lib AND the Maven model lib AND
        //   target PMaven Scala lib are loaded and visible through a single class loader...
        (ScalaClassLoader classExists (List(
                 (PMavenScriptCompiler.scalaCPFileFor(classOf[org.codehaus.plexus.util.xml.Xpp3Dom]).toURL)),
            "org.codehaus.plexus.util.xml.Xpp3Dom")) should be(true)
      }
    }
  }
}

