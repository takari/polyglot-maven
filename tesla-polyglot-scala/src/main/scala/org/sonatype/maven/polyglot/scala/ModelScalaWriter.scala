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

package org.sonatype.maven.polyglot.scala;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.ModelWriter;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.sonatype.maven.polyglot.io.ModelWriterSupport;

import java.io.{Writer => JWriter, PrintWriter => JPrintWriter}
import java.util.{Map => JMap}

import org.codehaus.plexus.util.xml.Xpp3Dom

import scala.collection.mutable.Buffer

/**
 * Writes a Maven {@link org.apache.maven.model.Model} to a <code>pom.scala</code>.
 *
 * @since 0.7
 */
@Component(role=classOf[ModelWriter], hint="scala")
class ModelScalaWriter extends ModelWriterSupport {

  @Requirement val logger: Logger = null

  override def write(rawOutput: JWriter, options: JMap[String, java.lang.Object],
      model: Model) {
    ApacheModelBeanScalaSerializer("project", model) serializeTo (new IndentingPrintWriter(None, rawOutput,""))
    rawOutput.flush
  }

}

/**
 * Convenience class with overriding println() implementation that adds
 * the indentation to each printed line.
 **/
class IndentingPrintWriter(parent: Option[IndentingPrintWriter] = None, target: JWriter, indentStr: String = "  ")
    extends JPrintWriter(target) {
  
  def indentedWriter = new IndentingPrintWriter(Some(this), target, "  ")
  
  def printIndent: Unit = {
    print(indentStr)
    parent match {
      case Some(p) => p.printIndent
      case None => ;
    }
  }
  
  def qe(s: String)(body: (String) => String): Unit =
    if(s != null && s.length > 0) println(body(s))
  
  def qe[T <: AnyRef](t: T)(body: (T) => String): Unit =
    if(t != null) println(body(t))
    
  def qe_foreach[T](buffer: Buffer[T])(body: (T) => String): Unit =
    if(buffer != null) {
      buffer foreach  { t => println(body(t)) }
    }
    
  override def println(): Unit = {
    printIndent
    super.println()
  }
  
  override def println(b: Boolean): Unit = {
    printIndent
    super.println(b)
  }
  
  override def println(c: Char): Unit = {
    printIndent
    super.println(c)
  }
  
  override def println(ac: Array[Char]): Unit = {
    printIndent
    super.println(ac)
  }
  
  override def println(d: Double): Unit = {
    printIndent
    super.println(d)
  }
  
  override def println(f: Float): Unit = {
    printIndent
    super.println(f)
  }
  
  override def println(i: Int): Unit = {
    printIndent
    super.println(i)
  }
  
  override def println(l: Long): Unit = {
    printIndent
    super.println(l)
  }
  
  override def println(o: java.lang.Object): Unit = {
    printIndent
    super.println(o)
  }
  
  override def println(s: String): Unit = {
    printIndent
    super.println(s)
  }
  
  def serializeXpp3Dom(dom: Xpp3Dom): Unit = {
    if (dom.getChildCount() == 0) {
      println("<" + dom.getName() + ">" + escapeScalaXML(dom.getValue) + "</" + dom.getName() + ">");
    } else {
      println("<" + dom.getName() + ">")
      for(child <- dom.getChildren())
        indentedWriter.serializeXpp3Dom(child)
      println("</" + dom.getName() + ">")
    }
  }
  
  def escapeScalaXML(s: String): String = {
    val m = java.util.regex.Pattern.compile("\\$\\{[^}]+}").matcher(s)
    var t = s
    while (m.find) {
      t = t.substring(0, m.start) + "{\"" + m.group + "\"}" + t.substring(m.end)
    }
    t
  }
}