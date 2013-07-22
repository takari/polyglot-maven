/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.scala.model

import org.sonatype.maven.polyglot.scala.ScalaPrettyPrinter._
import scala.xml.Elem

class PrettiedConfig(c: Elem) {
  val pp = new scala.xml.PrettyPrinter(defaultWidth, defaultIndent)

  def asDoc: Doc = string(pp.format(c))
}


import scala.xml.{Elem, XML}
import org.codehaus.plexus.util.xml.{Xpp3DomBuilder, Xpp3Dom}
import java.io.StringReader

class ConvertibleMavenConfig(mc: Object) {
  def asScala: Elem = {
    mc match {
      case xmlConfig: Xpp3Dom => XML.loadString(xmlConfig.toString)
      case _ => null
    }
  }
}

class ConvertibleScalaConfig(c: Elem) {
  def asJava: Xpp3Dom = {
    Xpp3DomBuilder.build(new StringReader(c.toString()))
  }
}