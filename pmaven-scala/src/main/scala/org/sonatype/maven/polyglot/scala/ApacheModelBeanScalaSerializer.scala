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

import scala.collection.JavaConversions._
import scala.collection.mutable.Buffer
import scala.collection.mutable.Map

import java.beans._

/**
 * @since 0.7
 */
object ApacheModelBeanScalaSerializer {

  def apply(parentRelativePropRefName: String, a: AnyRef): ApacheModelBeanScalaSerializer = {
    val bi = apacheModelBeanInfo(a)
    new ApacheModelBeanScalaSerializer(parentRelativePropRefName, shortInstanceName(bi), a, bi)
  }
    
  def apacheModelBeanInfo(a: AnyRef): BeanInfo =
    apacheModelBeanInfo(a.getClass)
    
  def apacheModelBeanInfo(clazz: Class[_]): BeanInfo =
    if(clazz.getName startsWith "org.apache.maven.model")
      Introspector.getBeanInfo(clazz)
    else
      apacheModelBeanInfo(clazz.getSuperclass)
  
  /**
   * <p>
   * Use the first letter of the bean class name, lowercased. This value is used
   * as the local variable name within initializer closures in Scala PMaven code. E.g.,
   * </p>
   *
   * <p><blockquote><pre>
   *  xyz.prop { l =>  <b><i>// <-- The name "l"</i></b>
   *    ...
   *  }
   * </pre></blockqutoe></p>
   **/
  def shortInstanceName(bi: BeanInfo): String =
    bi.getBeanDescriptor.getBeanClass.getSimpleName.substring(0, 1).toLowerCase

  /**
   * List[-non-primitive and non-String]-typed properties have special Scala-ized
   * setter functions to use. The listMemberSetterNameMap gives the special
   * Scala-ized setter to use for the list-typed beans property.
   **/
  val listMemberSetterNameMap: Map[String, String] =
      Map("dependencies" -> "dependency",
          "exclusions" -> "exclusion",
          "repositories" -> "repository",
          "pluginRepositories" -> "pluginRepository",
          "contributors" -> "contributor",
          "developers" -> "developer",
          "licenses" -> "license",
          "mailingLists" -> "mailingList",
          "profiles" -> "profile",
          "resources" -> "resource",
          "testResources" -> "testResource",
          "extensions" -> "extension",
          "notifiers" -> "notifier",
          "plugins" -> "plugin",
          "executions" -> "execution",
          "reportSets" -> "reportSet"
      )
      
  def listMemberSetterName(s: String): String =
      listMemberSetterNameMap(s)
      
  /**
   * Simple-typed properties might need a property name-> Scala-ized setter method
   * mapping. E.g., "getType/setType" in Java cannot be mapped to the name "type",
   * since that's keyword in Scala; so instead there is a mapping to "_type".
   **/
  def propertySetterName(s: String): String =
    s match {
      case "type" => "_type"
      case x => x
    }
}

class ApacheModelBeanScalaSerializer(parentRelativePropRefName: String, instanceName: String,
    theBean: AnyRef, beanInfo: BeanInfo) {

  def serializeTo(writer: IndentingPrintWriter): Unit = {
    writer.println(parentRelativePropRefName + " { " + instanceName + " =>")
    serializePropsTo(writer.indentedWriter)
    writer.println("}")
  }

  def serializePropsTo(writer: IndentingPrintWriter): Unit =
    beanInfo.getPropertyDescriptors foreach { pd => serializePropTo(pd, writer) }
    
  def serializePropTo(pd: PropertyDescriptor, writer: IndentingPrintWriter): Unit = {
    import ApacheModelBeanScalaSerializer._
  
    //...make sure there's a property getter AND setter methods, indicating the property
    //   is actually readable, and it can be set by a build script...
    val reader = pd.getReadMethod
    if(pd.getReadMethod != null && pd.getWriteMethod != null) {
    
      //...make sure the value is not null. Null values are the default, so do
      //   not need to be serialized in Scala code...
      val value = reader.invoke(theBean)      
      if(value != null) {

        if(pd.getPropertyType.equals(classOf[String])) {
          //...a String-typed property...
          writer.qe(value) { instanceName + "." + propertySetterName(pd.getName) + " = \"" + _  + "\"" }
        } else if(pd.getPropertyType.isPrimitive) {
          //...a Boolean or Int or other primitive-typed property
          writer.qe(value) { instanceName + "." + propertySetterName(pd.getName) + " = " + _ }
        } else if(classOf[java.util.List[_]].isAssignableFrom(pd.getPropertyType)) {
          //...a java.util.List-typed property. Write out each member as a property...
          val list = value.asInstanceOf[java.util.List[java.lang.Object]]
          if(list != null) {
            (list: Buffer[java.lang.Object]) foreach { item: java.lang.Object =>
              if(classOf[String].isInstance(item)) {
                //...property is a List[String], which is represented as a "+=" call
                //   to the list-typed property...
                writer.println(instanceName + "." + pd.getName + " += \"" + item.toString + "\"")
              } else {
                //...property is a List[Apache Model object type]. To serialize this
                //   we recurse a another level and serialize the value object...
                ApacheModelBeanScalaSerializer(instanceName + "." +
                    listMemberSetterName(pd.getName), item).serializeTo(writer)
              }
            }
          }
        } else if(classOf[java.util.Properties].isAssignableFrom(pd.getPropertyType)) {
          //...a Properties-typed properties.Write out each name/value pair
          //   in a tuple of pairs using a "+=" addition to the property...
          val map = value.asInstanceOf[java.util.Map[String, java.lang.Object]]
          if(map != null) {
            (map: Map[String, java.lang.Object]) foreach { pair =>
              writer.println(instanceName + "." + pd.getName + " += (\"" + pair._1 + "\" -> \"" + pair._2.toString + "\")")
            }
          }
        } else if(pd.getPropertyType.equals(classOf[java.lang.Object])) {
          //...the "java.lang.Object" property type in Apache Model bean properties
          //   represents DOM Elements, which are used as weakly-typed arbitrary
          //   configuration structures. Just serialize the XML fragment w/o processing
          //   preamble, processing instructions, etc...
          
          writer.println(instanceName + "." + pd.getName + " =")
          writer.serializeXpp3Dom(value.asInstanceOf[org.codehaus.plexus.util.xml.Xpp3Dom])
        } else {
          //...we can assume the property type is another Apache Model Bean...
          ApacheModelBeanScalaSerializer(instanceName + "." + pd.getName, value).serializeTo(writer)
        }
        
      }
      
    }
  }
}
