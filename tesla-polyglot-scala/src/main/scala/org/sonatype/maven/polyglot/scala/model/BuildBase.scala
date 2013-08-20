/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.scala.model

class BuildBase(
                 val defaultGoal: Option[String],
                 val resources: Seq[Resource],
                 val testResources: Seq[Resource],
                 val directory: Option[String],
                 val finalName: Option[String],
                 val filters: Seq[String],
                 val pluginManagement: Option[PluginManagement],
                 val plugins: Seq[Plugin]
                 )

object BuildBase {
  def apply(
             defaultGoal: String = null,
             resources: Seq[Resource] = Seq.empty,
             testResources: Seq[Resource] = Seq.empty,
             directory: String = null,
             finalName: String = null,
             filters: Seq[String] = Seq.empty,
             pluginManagement: PluginManagement = null,
             plugins: Seq[Plugin] = Seq.empty
             ) =
    new BuildBase(
      Option(defaultGoal),
      resources,
      testResources,
      Option(directory),
      Option(finalName),
      filters,
      Option(pluginManagement),
      plugins
    )
}


import org.sonatype.maven.polyglot.scala.ScalaPrettyPrinter._

class PrettiedBuildBase(b: BuildBase) {
  def asDoc: Doc = {
    `object`("BuildBase", asDocArgs)
  }

  def asDocArgs: Seq[Doc] = {
    val args = scala.collection.mutable.ListBuffer[Doc]()
    b.defaultGoal.foreach(args += assignString("defaultGoal", _))
    Some(b.resources).filterNot(_.isEmpty).foreach(r => args += assign("resources", seq(r.map(_.asDoc))))
    Some(b.testResources).filterNot(_.isEmpty).foreach(trs => args += assign("testResources", seq(trs.map(_.asDoc))))
    b.directory.foreach(args += assignString("directory", _))
    b.finalName.foreach(args += assignString("finalName", _))
    Some(b.filters).filterNot(_.isEmpty).foreach(f => args += assign("filters", seqString(f)))
    b.pluginManagement.foreach(pm => args += assign("pluginManagement", pm.asDoc))
    Some(b.plugins).filterNot(_.isEmpty).foreach(ps => args += assign("plugins", seq(ps.map(_.asDoc))))
    args
  }
}


import org.sonatype.maven.polyglot.scala.MavenConverters._
import scala.collection.JavaConverters._
import org.apache.maven.model.{BuildBase => MavenBuildBase}

class ConvertibleMavenBuildBase(mb: MavenBuildBase) {
  def asScala: BuildBase = {
    BuildBase(
      mb.getDefaultGoal,
      mb.getResources.asScala.map(_.asScala),
      mb.getTestResources.asScala.map(_.asScala),
      mb.getDirectory,
      mb.getFinalName,
      mb.getFilters.asScala,
      Option(mb.getPluginManagement).map(_.asScala).orNull,
      mb.getPlugins.asScala.map(_.asScala)
    )
  }
}

import org.sonatype.maven.polyglot.scala.ScalaConverters._

class ConvertibleScalaBuildBase(b: BuildBase) {
  def asJava: MavenBuildBase = {
    val mb = new MavenBuildBase
    mb.setDefaultGoal(b.defaultGoal.orNull)
    mb.setDirectory(b.directory.orNull)
    mb.setFilters(b.filters.asJava)
    mb.setFinalName(b.finalName.orNull)
    mb.setPluginManagement(b.pluginManagement.map(_.asJava).orNull)
    mb.setPlugins(b.plugins.map(_.asJava).asJava)
    mb.setResources(b.resources.map(_.asJava).asJava)
    mb.setTestResources(b.testResources.map(_.asJava).asJava)
    mb
  }
}