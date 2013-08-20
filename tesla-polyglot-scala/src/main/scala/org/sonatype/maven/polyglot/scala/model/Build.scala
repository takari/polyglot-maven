/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.scala.model

class Build(
             val sourceDirectory: Option[String],
             val scriptSourceDirectory: Option[String],
             val testSourceDirectory: Option[String],
             val outputDirectory: Option[String],
             val testOutputDirectory: Option[String],
             val extensions: Seq[Extension],
             defaultGoal: Option[String],
             resources: Seq[Resource],
             testResources: Seq[Resource],
             directory: Option[String],
             finalName: Option[String],
             filters: Seq[String],
             pluginManagement: Option[PluginManagement],
             plugins: Seq[Plugin]
             )
  extends
  BuildBase(
    defaultGoal,
    resources,
    testResources,
    directory,
    finalName,
    filters,
    pluginManagement,
    plugins
  )

object Build {
  def apply(
             sourceDirectory: String = null,
             scriptSourceDirectory: String = null,
             testSourceDirectory: String = null,
             outputDirectory: String = null,
             testOutputDirectory: String = null,
             extensions: Seq[Extension] = Seq.empty,
             defaultGoal: String = null,
             resources: Seq[Resource] = Seq.empty,
             testResources: Seq[Resource] = Seq.empty,
             directory: String = null,
             finalName: String = null,
             filters: Seq[String] = Seq.empty,
             pluginManagement: PluginManagement = null,
             plugins: Seq[Plugin] = Seq.empty
             ) =
    new Build(
      Option(sourceDirectory),
      Option(scriptSourceDirectory),
      Option(testSourceDirectory),
      Option(outputDirectory),
      Option(testOutputDirectory),
      extensions,
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

class PrettiedBuild(b: Build) {
  def asDoc: Doc = {
    val args = scala.collection.mutable.ListBuffer[Doc]()
    b.sourceDirectory.foreach(args += assignString("sourceDirectory", _))
    b.scriptSourceDirectory.foreach(args += assignString("scriptSourceDirectory", _))
    b.testSourceDirectory.foreach(args += assignString("testSourceDirectory", _))
    b.outputDirectory.foreach(args += assignString("outputDirectory", _))
    b.testOutputDirectory.foreach(args += assignString("testOutputDirectory", _))
    Some(b.extensions).filterNot(_.isEmpty).foreach(es => args += assign("extensions", seq(es.map(_.asDoc))))
    args ++= b.asDocArgs
    `object`("Build", args)
  }
}


import org.sonatype.maven.polyglot.scala.MavenConverters._
import scala.collection.JavaConverters._
import org.apache.maven.model.{Build => MavenBuild}

class ConvertibleMavenBuild(mb: MavenBuild) {
  def asScala: Build = {
    Build(
      mb.getSourceDirectory,
      mb.getScriptSourceDirectory,
      mb.getTestSourceDirectory,
      mb.getOutputDirectory,
      mb.getTestOutputDirectory,
      mb.getExtensions.asScala.map(_.asScala),
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

class ConvertibleScalaBuild(b: Build) {
  def asJava: MavenBuild = {
    val mb = new MavenBuild
    mb.setExtensions(b.extensions.map(_.asJava).asJava)
    mb.setOutputDirectory(b.outputDirectory.orNull)
    mb.setScriptSourceDirectory(b.scriptSourceDirectory.orNull)
    mb.setSourceDirectory(b.sourceDirectory.orNull)
    mb.setTestOutputDirectory(b.testOutputDirectory.orNull)
    mb.setTestSourceDirectory(b.testSourceDirectory.orNull)
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