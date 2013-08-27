/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.scala.model

import org.codehaus.plexus.util.xml.Xpp3Dom
import scala.collection.mutable.ListBuffer
import scala.language.dynamics

/**
 * A Config object lets us conveniently express objects that can have any property with any type. This generally
 * serves as configuration to a plugin. The following sample illustrates configuration being created:
 * {{{
 *   scala> val c = Config(a = 1, b = "hi")
 *   c: Seq[(String, Any)] = List((a,1), (b,hi))
 * }}}
 */
class Config(val elements: Seq[(String, Option[Any])])

object Config extends Dynamic {
  def applyDynamicNamed(method: String)(params: (String, Any)*): Config = {
    if (method == "apply") {
      val elements = ListBuffer[(String, Option[Any])]()
      params.foreach {
        p =>
          val value = p._2 match {
            case value: Option[_] => value
            case value: Any => Option(value)
          }
          elements += p._1 -> value
      }
      new Config(elements)
    }
    else throw new UnsupportedOperationException
  }
}


import org.sonatype.maven.polyglot.scala.ScalaPrettyPrinter._

class PrettiedConfig(c: Config) {
  def asDoc: Doc = {
    val args = scala.collection.mutable.ListBuffer[Doc]()
    c.elements.foreach {
      e =>
        val value: Doc = e._2.map[Doc]({
          value =>
            value match {
              case value: Config => value.asDoc
              case value: String => dquotes(value)
              case value: Any => value.toString
            }
        }).getOrElse("None")
        args += assign(e._1, value)
    }
    `object`("Config", args)
  }
}


class ConvertibleMavenConfig(mc: Object) {

  private val config = mc match {
    case xmlConfig: Xpp3Dom => {
      def asConfig(children: Array[Xpp3Dom]): Config = {
        val elements = ListBuffer[(String, Option[Any])]()
        children.foreach {
          child =>
            val childValue = if (child.getChildCount == 0) child.getValue else asConfig(child.getChildren)
            elements += (child.getName -> Option(childValue))
        }
        new Config(elements)
      }
      asConfig(xmlConfig.getChildren)
    }
    case _ => new Config(Seq.empty)
  }

  def asScala = config
}

class ConvertibleScalaConfig(config: Config) {

  private def addChildren(parent: Xpp3Dom, children: Config): Xpp3Dom = {
    children.elements.foreach {
      p =>
        val e = new Xpp3Dom(p._1)
        parent.addChild(e)
        p._2.foreach {
          value =>
            value match {
              case value: Config => addChildren(e, value)
              case value: Any => e.setValue(value.toString)
            }
        }
    }
    parent
  }

  private val doc = addChildren(new Xpp3Dom("configuration"), config)

  def asJava: Xpp3Dom = doc
}