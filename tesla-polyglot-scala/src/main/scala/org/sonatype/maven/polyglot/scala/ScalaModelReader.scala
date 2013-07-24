/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.scala

import java.io.Reader
import java.util.{Map => JavaMap}
import org.apache.maven.model.{Model => MavenModelClass}
import org.apache.maven.model.io.ModelReader
import org.sonatype.maven.polyglot.io.ModelReaderSupport
import org.sonatype.maven.polyglot.scala.model.Model
import org.codehaus.plexus.component.annotations.{Component, Requirement}
import org.codehaus.plexus.logging.Logger

import scala.tools.nsc.GenericRunnerSettings

/**
 * @since 0.7
 */
@Component(role=classOf[ModelReader], hint="scala")
class ScalaModelReader extends ModelReaderSupport {
  type modelGenerator = {
    def generateModel: Model
  }

  @Requirement val logger: Logger = null
  
  override def read(input: Reader, options: JavaMap[String, _]): MavenModelClass = {
    require(input != null, "Illegal argument: input must not be null!")

    def errorFn(err: String) {
    }
    
    val cmdLine = List("-nocompdaemon")
    val settings = new GenericRunnerSettings(errorFn _)
    settings.parseParams(cmdLine)
        
    PMavenScriptCompiler.compileDSL(logger, settings, input) match {
      case Some(location) => 
        //...execute the script and return the Model object...
        val omg = PMavenScriptCompiler.loadModelGenerator(location)
        omg match {
          case Some(mg) =>
            mg.generateModel
          case None =>
            //...log this...
            null;
        }

      case None =>
        //...log this...
        null
    }
  }
}
