/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.scala

import java.util
import org.apache.maven.model.io.ModelReader
import com.twitter.util.Eval
import org.sonatype.maven.polyglot.scala.model.{Activation => ScalaActivation, ActivationFile => ScalaActivationFile, ActivationOS => ScalaActivationOS, ActivationProperty => ScalaActivationProperty, Build => ScalaBuild, BuildBase => ScalaBuildBase, CiManagement => ScalaCiManagement, Contributor => ScalaContributor, DependencyManagement => ScalaDependencyManagement, Dependency => ScalaDependency, DeploymentRepository => ScalaDeploymentRepository, Developer => ScalaDeveloper, DistributionManagement => ScalaDistributionManagement, Execution => ScalaExecution, Extension => ScalaExtension, IssueManagement => ScalaIssueManagement, License => ScalaLicense, MailingList => ScalaMailingList, Model => ScalaModel, Notifier => ScalaNotifier, Organization => ScalaOrganization, Parent => ScalaParent, Plugin => ScalaPlugin, PluginManagement => ScalaPluginManagement, Relocation => ScalaRelocation, RepositoryPolicy => ScalaRepositoryPolicy, Repository => ScalaRepository, Resource => ScalaResource, Scm => ScalaScm, Site => ScalaSite, _}
import org.codehaus.plexus.util.{FileUtils, IOUtil}
import java.io._
import org.codehaus.plexus.component.annotations.Component
import scala.xml.Elem
import scala.Some
import java.io.File
import org.apache.maven.model.Model
import org.codehaus.plexus.util.io.RawInputStreamFacade
import org.sonatype.maven.polyglot.PolyglotModelUtil

/**
 * implicit conversions around the "pimp my library" approach for converting Scala models to their Maven types.
 */
object ScalaConverters {
  implicit def enrichScalaActivation(v: ScalaActivation) = new ConvertibleScalaActivation(v)

  implicit def enrichScalaActivationFile(v: ScalaActivationFile) = new ConvertibleScalaActivationFile(v)

  implicit def enrichScalaActivationOS(v: ScalaActivationOS) = new ConvertibleScalaActivationOS(v)

  implicit def enrichScalaActivationProperty(v: ScalaActivationProperty) = new ConvertibleScalaActivationProperty(v)

  implicit def enrichScalaBuild(v: ScalaBuild) = new ConvertibleScalaBuild(v)

  implicit def enrichScalaBuildBase(v: ScalaBuildBase) = new ConvertibleScalaBuildBase(v)

  implicit def enrichScalaCiManagement(v: ScalaCiManagement) = new ConvertibleScalaCiManagement(v)

  implicit def enrichScalaConfig(v: Elem) = new ConvertibleScalaConfig(v)

  implicit def enrichScalaContributor(v: ScalaContributor) = new ConvertibleScalaContributor(v)

  implicit def enrichScalaDependency(v: ScalaDependency) = new ConvertibleScalaDependency(v)

  implicit def enrichScalaDependencyManagement(v: ScalaDependencyManagement) = new ConvertibleScalaDependencyManagement(v)

  implicit def enrichScalaDeploymentRepository(v: ScalaDeploymentRepository) = new ConvertibleScalaDeploymentRepository(v)

  implicit def enrichScalaDeveloper(v: ScalaDeveloper) = new ConvertibleScalaDeveloper(v)

  implicit def enrichScalaDistributionManagement(v: ScalaDistributionManagement) = new ConvertibleScalaDistributionManagement(v)

  implicit def enrichScalaExecution(v: ScalaExecution) = new ConvertibleScalaExecution(v)

  implicit def enrichScalaExtension(v: ScalaExtension) = new ConvertibleScalaExtension(v)

  implicit def enrichScalaIssueManagement(v: ScalaIssueManagement) = new ConvertibleScalaIssueManagement(v)

  implicit def enrichScalaLicense(v: ScalaLicense) = new ConvertibleScalaLicense(v)

  implicit def enrichScalaMailingList(v: ScalaMailingList) = new ConvertibleScalaMailingList(v)

  implicit def enrichScalaModel(v: ScalaModel) = new ConvertibleScalaModel(v)

  implicit def enrichScalaNotifier(v: ScalaNotifier) = new ConvertibleScalaNotifier(v)

  implicit def enrichScalaOrganization(v: ScalaOrganization) = new ConvertibleScalaOrganization(v)

  implicit def enrichScalaParent(v: ScalaParent) = new ConvertibleScalaParent(v)

  implicit def enrichScalaPlugin(v: ScalaPlugin) = new ConvertibleScalaPlugin(v)

  implicit def enrichScalaPluginManagement(v: ScalaPluginManagement) = new ConvertibleScalaPluginManagement(v)

  implicit def enrichScalaPrerequisites(v: Prerequisites) = new ConvertibleScalaPrerequisites(v)

  implicit def enrichScalaProfile(v: Profile) = new ConvertibleScalaProfile(v)

  implicit def enrichScalaReleasePolicy(v: ScalaRepositoryPolicy) = new ConvertibleScalaRepositoryPolicy(v)

  implicit def enrichScalaRelocation(v: ScalaRelocation) = new ConvertibleScalaRelocation(v)

  implicit def enrichScalaRepository(v: ScalaRepository) = new ConvertibleScalaRepository(v)

  implicit def enrichScalaResource(v: ScalaResource) = new ConvertibleScalaResource(v)

  implicit def enrichScalaScm(v: ScalaScm) = new ConvertibleScalaScm(v)

  implicit def enrichScalaSite(v: ScalaSite) = new ConvertibleScalaSite(v)
}

/**
 * Reads a Scala model from a script and produces a Maven Model object.
 * All Scala evaluation is done with files so that checks can be made to determine whether compilation occurs.
 */
@Component(role = classOf[ModelReader], hint = "scala")
class ScalaModelReader extends ModelReader {

  import org.sonatype.maven.polyglot.scala.ScalaConverters._

  def read(reader: Reader, options: util.Map[String, _]): Model = {
    val evalPomFile = locateEvalPomFile(options)
    IOUtil.copy(reader, new FileOutputStream(evalPomFile))
    eval(evalPomFile, evalPomFile).asJava
  }

  def read(input: InputStream, options: util.Map[String, _]): Model = {
    val evalPomFile = locateEvalPomFile(options)
    FileUtils.copyStreamToFile(new RawInputStreamFacade(input), evalPomFile)
    eval(evalPomFile, evalPomFile).asJava
  }

  def read(input: File, options: util.Map[String, _]): Model = {
    val evalPomFile = locateEvalPomFile(options)
    eval(evalPomFile, input).copy(pomFile = Some(input)).asJava
  }

  private def locateEvalPomFile(options: util.Map[String, _]): File = {
    val source = PolyglotModelUtil.getLocation(options)
    val evalTarget = new File(new File(source).getParent, "target" + File.separator + "scalamodel")
    evalTarget.mkdirs()
    new File(evalTarget, "pom.scala")
  }

  private def eval(evalPomFile: File, sourcePomFile: File): ScalaModel = {
    new Eval(Some(evalPomFile.getParentFile)).apply[ScalaModel](sourcePomFile)
  }
}
