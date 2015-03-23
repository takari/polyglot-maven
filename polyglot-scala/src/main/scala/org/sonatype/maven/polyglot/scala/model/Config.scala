/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.scala.model

import org.codehaus.plexus.util.xml.Xpp3Dom
import scala.collection.immutable
import scala.collection.mutable.ListBuffer
import scala.language.dynamics
import scala.language.postfixOps

/**
 * A Config object lets us conveniently express objects that can have any property with any type. This generally
 * serves as configuration to a plugin. The following sample illustrates configuration being created:
 * {{{
 *   scala> val c = Config(a = 1, b = "hi")
 *   c: immutable.Seq[(String, Any)] = List((a,1), (b,hi))
 * }}}
 */
class Config(val elements: immutable.Seq[(String, Option[Any])])

object Config extends Dynamic {

  object Optional {
    def unapply(x: Any): Some[Option[Any]] = Some(x match {
      case x: Option[_] => x
      case _ => Option(x)
    })
  }

  def applyDynamicNamed(method: String)(params: (String, Any)*): Config =
    if (method == "apply") new Config(params map {
      case (k, Optional(v)) if k.startsWith("$at") && k.size > 3 => s"@${k.substring(3)}" -> v
      case (k, Optional(v)) => k -> v
    } toList)
    else throw new UnsupportedOperationException

}


import org.sonatype.maven.polyglot.scala.ScalaPrettyPrinter._

class PrettiedConfig(c: Config) {
  def asDoc: Doc = {
    val args = scala.collection.mutable.ListBuffer[Doc]()
    c.elements.foreach {
      e =>
        val value: Doc = e._2.map[Doc]({
          case value: Config => value.asDoc
          case value: String => dquotes(value)
          case value: Any => value.toString
        }).getOrElse("None")
        val name = if (e._1.startsWith("@")) s"`${e._1}`" else e._1
        args += assign(name, value)
    }
    `object`("Config", args.toList)
  }
}


class ConvertibleMavenConfig(mc: Object) {

  private val config = mc match {
    case xmlConfig: Xpp3Dom =>
      def asConfig(xmlConfig: Xpp3Dom): Config = {
        val elements = ListBuffer[(String, Option[Any])]()
        xmlConfig.getAttributeNames.foreach {
          attrName =>
            val attrValue = Option(xmlConfig.getAttribute(attrName))
            elements += (s"@$attrName" -> attrValue)
        }
        xmlConfig.getChildren.foreach {
          // Note: a limitation of this model is that we can't have both attributes and values (attributes and
          // child elements are fine though). 'hopefully this won't turn out to be a restriction.
          child =>
            val childValue =
              if (child.getChildCount == 0 && child.getAttributeNames.isEmpty) {
                child.getValue
              } else {
                asConfig(child)
              }
            elements += (child.getName -> Option(childValue))
        }
        new Config(elements.toList)
      }
      asConfig(xmlConfig)
    case _ => new Config(immutable.Seq.empty)
  }

  def asScala = config
}

class ConvertibleScalaConfig(config: Config) {

  private def addChildren(parent: Xpp3Dom, children: Config): Xpp3Dom = {
    children.elements.foreach {
      p =>
        if (p._1.startsWith("@") && p._1.size > 1) {
          parent.setAttribute(p._1.substring(1), p._2.map(_.toString).orNull)
        } else {
          val e = new Xpp3Dom(p._1)
          parent.addChild(e)
          p._2.foreach {
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