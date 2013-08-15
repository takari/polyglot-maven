/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.scala

import java.io.Writer
import java.util
import org.sonatype.maven.polyglot.io.ModelWriterSupport
import org.kiama.output.PrettyPrinter
import org.apache.maven.model.io.ModelWriter
import org.codehaus.plexus.component.annotations.Component
import org.apache.maven.model.{Build => MavenBuild, CiManagement => MavenCiManagement, Contributor => MavenContributor, DependencyManagement => MavenDependencyManagement, Dependency => MavenDependency, DeploymentRepository => MavenDeploymentRepository, Developer => MavenDeveloper, DistributionManagement => MavenDistributionManagement, PluginExecution => MavenExecution, Extension => MavenExtension, Model => MavenModel, Notifier => MavenNotifier, Parent => MavenParent, Plugin => MavenPlugin, PluginManagement => MavenPluginManagement, Relocation => MavenRelocation, RepositoryPolicy => MavenRepositoryPolicy, Repository => MavenRepository, Resource => MavenResource, Site => MavenSite}
import org.sonatype.maven.polyglot.scala.model._
import java.util.Properties
import scala.xml.Elem

/**
 * Responsible for printing Scala source.
 */
object ScalaPrettyPrinter extends PrettyPrinter {
  override val defaultIndent = 2

  // Various doc functions that enhance doc construction for our domain.

  def assignString(label: String, value: String): Doc = label <+> equal <+> dquotes(value)

  def assign(label: String, value: Doc): Doc = label <+> equal <+> value

  def `object`(name: String, args: Seq[Doc]): Doc = {
    name <> lparen <> nest(lsep(args, comma)) <@> rparen
  }

  def mapStrings(m: Map[String, String]): Doc = {
    "Map" <> lparen <> nest(lsep(m.map(me => dquotes(me._1) <+> "->" <+> dquotes(me._2)).toSeq, comma)) <@> rparen
  }

  def seqString(s: Seq[String]): Doc = "Seq" <> lparen <> nest(lsep(s.map(dquotes(_)), comma)) <@> rparen

  def seq(s: Seq[Doc]): Doc = "Seq" <> lparen <> nest(lsep(s, comma)) <@> rparen

  implicit def toDoc(f: File): Doc = {
    "new" <+> "File" <> lparen <> dquote <> f.getCanonicalPath <> dquote <> rparen
  }

  // Implicit conversions for prettifying

  implicit def enrichPrettiedBuild(v: Build) = new PrettiedBuild(v)

  implicit def enrichPrettiedCiManagement(v: CiManagement) = new PrettiedCiManagement(v)

  implicit def enrichPrettiedConfig(v: Elem) = new PrettiedConfig(v)

  implicit def enrichPrettiedConfigurationContainer(v: ConfigurationContainer) = new PrettiedConfigurationContainer(v)

  implicit def enrichPrettiedContributor(v: Contributor) = new PrettiedContributor(v)

  implicit def enrichPrettiedDependency(v: Dependency) = new PrettiedDependency(v)

  implicit def enrichPrettiedDependencyManagement(v: DependencyManagement) = new PrettiedDependencyManagement(v)

  implicit def enrichPrettiedDeploymentRepository(v: DeploymentRepository) = new PrettiedDeploymentRepository(v)

  implicit def enrichPrettiedDeveloper(v: Developer) = new PrettiedDeveloper(v)

  implicit def enrichPrettiedDistributionManagement(v: DistributionManagement) = new PrettiedDistributionManagement(v)

  implicit def enrichPrettiedExecution(v: Execution) = new PrettiedExecution(v)

  implicit def enrichPrettiedExtension(v: Extension) = new PrettiedExtension(v)

  implicit def enrichPrettiedGav(v: Gav) = new PrettiedGav(v)

  implicit def enrichPrettiedGroupArtifactId(v: GroupArtifactId) = new PrettiedGroupArtifactId(v)

  implicit def enrichPrettiedModel(v: Model) = new PrettiedModel(v)

  implicit def enrichPrettiedNotifier(v: Notifier) = new PrettiedNotifier(v)

  implicit def enrichPrettiedParent(v: Parent) = new PrettiedParent(v)

  implicit def enrichPrettiedPlugin(v: Plugin) = new PrettiedPlugin(v)

  implicit def enrichPrettiedPluginContainer(v: PluginContainer) = new PrettiedPluginContainer(v)

  implicit def enrichPrettiedPluginManagement(v: PluginManagement) = new PrettiedPluginManagement(v)

  implicit def enrichPrettiedProperties(v: Map[String, String]) = new PrettiedProperties(v)

  implicit def enrichPrettiedRelocation(v: Relocation) = new PrettiedRelocation(v)

  implicit def enrichPrettiedRepositoryPolicy(v: RepositoryPolicy) = new PrettiedRepositoryPolicy(v)

  implicit def enrichPrettiedRepository(v: Repository) = new PrettiedRepository(v)

  implicit def enrichPrettiedResource(v: Resource) = new PrettiedResource(v)

  implicit def enrichPrettiedSite(v: Site) = new PrettiedSite(v)
}

/**
 * implicit conversions around the "pimp my library" approach for converting Maven models to their Scala types.
 */
object MavenConverters {
  implicit def enrichConvertibleBuild(v: MavenBuild) = new ConvertibleMavenBuild(v)

  implicit def enrichConvertibleCiManagement(v: MavenCiManagement) = new ConvertibleMavenCiManagement(v)

  implicit def enrichConvertibleConfig(v: Object) = new ConvertibleMavenConfig(v)

  implicit def enrichConvertibleContributor(v: MavenContributor) = new ConvertibleMavenContributor(v)

  implicit def enrichConvertibleDependency(v: MavenDependency) = new ConvertibleMavenDependency(v)

  implicit def enrichConvertibleDependencyManagement(v: MavenDependencyManagement) = new ConvertibleMavenDependencyManagement(v)

  implicit def enrichConvertibleDeploymentRepository(v: MavenDeploymentRepository) = new ConvertibleMavenDeploymentRepository(v)

  implicit def enrichConvertibleDeveloper(v: MavenDeveloper) = new ConvertibleMavenDeveloper(v)

  implicit def enrichConvertibleDistributionManagement(v: MavenDistributionManagement) = new ConvertibleMavenDistributionManagement(v)

  implicit def enrichConvertibleExecution(v: MavenExecution) = new ConvertibleMavenExecution(v)

  implicit def enrichConvertibleExtension(v: MavenExtension) = new ConvertibleMavenExtension(v)

  implicit def enrichConvertibleGav(v: (String, String, String)) = new ConvertibleMavenGav(v)

  implicit def enrichConvertibleGroupArtifactId(v: (String, String)) = new ConvertibleMavenGroupArtifactId(v)

  implicit def enrichConvertibleModel(v: MavenModel) = new ConvertibleMavenModel(v)

  implicit def enrichConvertibleNotifier(v: MavenNotifier) = new ConvertibleMavenNotifier(v)

  implicit def enrichConvertibleParent(v: MavenParent) = new ConvertibleMavenParent(v)

  implicit def enrichConvertiblePlugin(v: MavenPlugin) = new ConvertibleMavenPlugin(v)

  implicit def enrichConvertiblePluginManagement(v: MavenPluginManagement) = new ConvertibleMavenPluginManagement(v)

  implicit def enrichConvertibleProperties(v: Properties) = new ConvertibleMavenProperties(v)

  implicit def enrichConvertibleRelocation(v: MavenRelocation) = new ConvertibleMavenRelocation(v)

  implicit def enrichConvertibleRepositoryPolicy(v: MavenRepositoryPolicy) = new ConvertibleMavenRepositoryPolicy(v)

  implicit def enrichConvertibleRepository(v: MavenRepository) = new ConvertibleMavenRepository(v)

  implicit def enrichConvertibleResource(v: MavenResource) = new ConvertibleMavenResource(v)

  implicit def enrichConvertibleSite(v: MavenSite) = new ConvertibleMavenSite(v)
}

/**
 * Convert a Maven Model to a Scala Model in source code form.
 */
@Component(role = classOf[ModelWriter], hint = "scala")
class ScalaModelWriter extends ModelWriterSupport {

  def write(writer: Writer, options: util.Map[String, AnyRef], mm: MavenModel): Unit = {
    import MavenConverters._
    import ScalaPrettyPrinter._

    val d = "import" <+> "org.sonatype.maven.polyglot.scala.model._" <@>
      empty <@>
      mm.asScala.asDoc <@>
      empty
    val pp = ScalaPrettyPrinter.pretty(d)
    writer.append(pp)
    writer.flush()
  }

}
