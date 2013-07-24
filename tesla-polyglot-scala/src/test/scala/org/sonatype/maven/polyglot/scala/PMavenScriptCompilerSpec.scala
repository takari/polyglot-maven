/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

