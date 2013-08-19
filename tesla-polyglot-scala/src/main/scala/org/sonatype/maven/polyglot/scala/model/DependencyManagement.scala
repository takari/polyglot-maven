/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.scala.model

class DependencyManagement(val dependencies: Seq[Dependency])

object DependencyManagement {
  def apply(dependencies: Seq[Dependency] = Nil) = new DependencyManagement(dependencies)
}

import org.sonatype.maven.polyglot.scala.ScalaPrettyPrinter._

class PrettiedDependencyManagement(dm: DependencyManagement) {
  def asDoc: Doc = {
    val args = scala.collection.mutable.ListBuffer[Doc]()
    Some(dm.dependencies).filterNot(_.isEmpty).foreach(ps => args += assign("dependencies", seq(ps.map(_.asDoc))))
    `object`("DependencyManagement", args)
  }
}


import org.sonatype.maven.polyglot.scala.MavenConverters._
import scala.collection.JavaConverters._
import org.apache.maven.model.{DependencyManagement => MavenDependencyManagement}

class ConvertibleMavenDependencyManagement(mdm: MavenDependencyManagement) {
  def asScala: DependencyManagement = {
    DependencyManagement(
      mdm.getDependencies.asScala.map(_.asScala)
    )
  }
}

import org.sonatype.maven.polyglot.scala.ScalaConverters._

class ConvertibleScalaDependencyManagement(dm: DependencyManagement) {
  def asJava: MavenDependencyManagement = {
    val mdm = new MavenDependencyManagement
    mdm.setDependencies(dm.dependencies.map(_.asJava).asJava)
    mdm
  }
}