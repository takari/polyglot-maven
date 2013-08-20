/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.scala.model

abstract class ModelBase(
                          val dependencyManagement: Option[DependencyManagement],
                          val dependencies: Seq[Dependency],
                          val distributionManagement: Option[DistributionManagement],
                          val modules: Seq[String],
                          val pluginRepositories: Seq[Repository],
                          val repositories: Seq[Repository]
                          )


import org.sonatype.maven.polyglot.scala.ScalaPrettyPrinter._

class PrettiedModelBase(m: ModelBase) {
  def asDocArgs: Seq[Doc] = {
    val args = scala.collection.mutable.ListBuffer[Doc]()
    Some(m.modules).filterNot(_.isEmpty).foreach(m => args += assign("modules", seqString(m)))
    Some(m.repositories).filterNot(_.isEmpty).foreach(es => args += assign("repositories", seq(es.map(_.asDoc))))
    Some(m.pluginRepositories).filterNot(_.isEmpty).foreach(es => args += assign("pluginRepositories", seq(es.map(_.asDoc))))
    Some(m.dependencies).filterNot(_.isEmpty).foreach(es => args += assign("dependencies", seq(es.map(_.asDoc))))
    m.dependencyManagement.foreach(dm => args += assign("dependencyManagement", dm.asDoc))
    m.distributionManagement.foreach(dm => args += assign("distributionManagement", dm.asDoc))
    args
  }
}
