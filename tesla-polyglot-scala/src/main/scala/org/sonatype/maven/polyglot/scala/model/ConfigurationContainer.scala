/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.scala.model

import scala.xml.Elem

abstract class ConfigurationContainer(
                                       val inherited: Boolean,
                                       val configuration: Option[Elem]
                                       )

import org.sonatype.maven.polyglot.scala.ScalaPrettyPrinter._

class PrettiedConfigurationContainer(cc: ConfigurationContainer) {
  def asDocArgs: Seq[Doc] = {
    val args = scala.collection.mutable.ListBuffer[Doc]()
    Some(cc.inherited).filterNot(_ == true).foreach(i => args += assign("inherited", i.toString))
    cc.configuration.foreach(c => args += "configuration" <+> equal <@> spaces(defaultIndent) <> nest(c.asDoc))
    args
  }
}