/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.scala.model

class Model(
             val gav: Gav,
             val build: Option[Build],
             val ciManagement: Option[CiManagement],
             val contributors: Seq[Contributor],
             val dependencyManagement: Option[DependencyManagement],
             val dependencies: Seq[Dependency],
             val description: Option[String],
             val developers: Seq[Developer],
             val distributionManagement: Option[DistributionManagement],
             val inceptionYear: Option[String],
             val issueManagement: Option[IssueManagement],
             val licenses: Seq[License],
             val mailingLists: Seq[MailingList],
             val modelEncoding: String,
             val modelVersion: Option[String],
             val modules: Seq[String],
             val name: Option[String],
             val organization: Option[Organization],
             val packaging: String,
             val parent: Option[Parent],
             val pomFile: Option[File],
             val prerequisites: Option[Prerequisites],
             val url: Option[String]
             ) {
  def copy(pomFile: Option[File]): Model =
    new Model(
      gav,
      build,
      ciManagement,
      contributors,
      dependencyManagement,
      dependencies,
      description,
      developers,
      distributionManagement,
      inceptionYear,
      issueManagement,
      licenses,
      mailingLists,
      modelEncoding,
      modelVersion,
      modules,
      name,
      organization,
      packaging,
      parent,
      pomFile,
      prerequisites,
      url
    )
}

object Model {
  def apply(
             gav: Gav,
             build: Build = null,
             ciManagement: CiManagement = null,
             contributors: Seq[Contributor] = Nil,
             dependencyManagement: DependencyManagement = null,
             dependencies: Seq[Dependency] = Nil,
             description: String = null,
             developers: Seq[Developer] = Nil,
             distributionManagement: DistributionManagement = null,
             inceptionYear: String = null,
             issueManagement: IssueManagement = null,
             licenses: Seq[License] = Nil,
             mailingLists: Seq[MailingList] = Nil,
             modelEncoding: String = "UTF-8",
             modelVersion: String = null,
             modules: Seq[String] = Nil,
             name: String = null,
             organization: Organization = null,
             packaging: String = "jar",
             parent: Parent = null,
             pomFile: File = null,
             prerequisites: Prerequisites = null,
             url: String = null
             ) =
    new Model(
      gav,
      Option(build),
      Option(ciManagement),
      contributors,
      Option(dependencyManagement),
      dependencies,
      Option(description),
      developers,
      Option(distributionManagement),
      Option(inceptionYear),
      Option(issueManagement),
      licenses,
      mailingLists,
      modelEncoding,
      Option(modelVersion),
      modules,
      Option(name),
      Option(organization),
      packaging,
      Option(parent),
      Option(pomFile),
      Option(prerequisites),
      Option(url)
    )
}


import org.sonatype.maven.polyglot.scala.ScalaPrettyPrinter._

class PrettiedModel(m: Model) {
  def asDoc: Doc = {
    val args = scala.collection.mutable.ListBuffer(m.gav.asDoc)
    Some(m.packaging).filterNot(_ == "jar").foreach(args += assignString("packaging", _))
    m.name.foreach(args += assignString("name", _))
    m.description.foreach(args += assignString("description", _))
    m.url.foreach(args += assignString("url", _))
    m.prerequisites.foreach(ps => args += assign("prerequisites", ps.asDoc))
    m.issueManagement.foreach(im => args += assign("issueManagement", im.asDoc))
    m.ciManagement.foreach(ci => args += assign("ciManagement", ci.asDoc))
    m.inceptionYear.foreach(args += assignString("inceptionYear", _))
    Some(m.mailingLists).filterNot(_.isEmpty).foreach(ds => args += assign("mailingLists", seq(ds.map(_.asDoc))))
    Some(m.developers).filterNot(_.isEmpty).foreach(ds => args += assign("developers", seq(ds.map(_.asDoc))))
    Some(m.contributors).filterNot(_.isEmpty).foreach(cs => args += assign("contributors", seq(cs.map(_.asDoc))))
    Some(m.licenses).filterNot(_.isEmpty).foreach(ls => args += assign("licenses", seq(ls.map(_.asDoc))))
    m.organization.foreach(o => args += assign("organization", o.asDoc))
    m.parent.foreach(p => args += assign("parent", p.asDoc))
    Some(m.modules).filterNot(_.isEmpty).foreach(m => args += assign("modules", seqString(m)))
    Some(m.dependencies).filterNot(_.isEmpty).foreach(es => args += assign("dependencies", seq(es.map(_.asDoc))))
    m.dependencyManagement.foreach(dm => args += assign("dependencyManagement", dm.asDoc))
    m.distributionManagement.foreach(dm => args += assign("distributionManagement", dm.asDoc))
    m.build.foreach(b => args += assign("build", b.asDoc))
    Some(m.modelEncoding).filterNot(_ == "UTF-8").foreach(args += assignString("modelEncoding", _))
    m.modelVersion.foreach(args += assignString("modelVersion", _))
    `object`("Model", args)
  }
}


import org.sonatype.maven.polyglot.scala.MavenConverters._
import scala.collection.JavaConverters._
import org.apache.maven.model.{Model => MavenModel}

class ConvertibleMavenModel(mm: MavenModel) {
  def asScala: Model = {
    Model(
      (mm.getGroupId, mm.getArtifactId, mm.getVersion).asScala,
      Option(mm.getBuild).map(_.asScala).orNull,
      Option(mm.getCiManagement).map(_.asScala).orNull,
      mm.getContributors.asScala.map(_.asScala),
      Option(mm.getDependencyManagement).map(_.asScala).orNull,
      mm.getDependencies.asScala.map(_.asScala),
      mm.getDescription,
      mm.getDevelopers.asScala.map(_.asScala),
      Option(mm.getDistributionManagement).map(_.asScala).orNull,
      mm.getInceptionYear,
      Option(mm.getIssueManagement).map(_.asScala).orNull,
      mm.getLicenses.asScala.map(_.asScala),
      mm.getMailingLists.asScala.map(_.asScala),
      mm.getModelEncoding,
      mm.getModelVersion,
      mm.getModules.asScala,
      mm.getName,
      Option(mm.getOrganization).map(_.asScala).orNull,
      mm.getPackaging,
      Option(mm.getParent).map(_.asScala).orNull,
      mm.getPomFile,
      Option(mm.getPrerequisites).map(_.asScala).orNull,
      mm.getUrl
    )
  }
}

import org.sonatype.maven.polyglot.scala.ScalaConverters._

class ConvertibleScalaModel(m: Model) {
  def asJava: MavenModel = {
    val mm = new MavenModel
    mm.setArtifactId(m.gav.artifactId)
    mm.setBuild(m.build.map(_.asJava).orNull)
    mm.setCiManagement(m.ciManagement.map(_.asJava).orNull)
    mm.setContributors(m.contributors.map(_.asJava).asJava)
    mm.setDescription(m.description.orNull)
    mm.setDevelopers(m.developers.map(_.asJava).asJava)
    mm.setDependencies(m.dependencies.map(_.asJava).asJava)
    mm.setDependencyManagement(m.dependencyManagement.map(_.asJava).orNull)
    mm.setDistributionManagement(m.distributionManagement.map(_.asJava).orNull)
    mm.setGroupId(m.gav.groupId.orNull)
    mm.setInceptionYear(m.inceptionYear.orNull)
    mm.setIssueManagement(m.issueManagement.map(_.asJava).orNull)
    mm.setLicenses(m.licenses.map(_.asJava).asJava)
    mm.setMailingLists(m.mailingLists.map(_.asJava).asJava)
    mm.setModules(m.modules.asJava)
    mm.setModelEncoding(m.modelEncoding)
    mm.setModelVersion(m.modelVersion.orNull)
    mm.setName(m.name.orNull)
    mm.setOrganization(m.organization.map(_.asJava).orNull)
    mm.setPackaging(m.packaging)
    mm.setParent(m.parent.map(_.asJava).orNull)
    mm.setPomFile(m.pomFile.orNull)
    mm.setPrerequisites(m.prerequisites.map(_.asJava).orNull)
    //mm.setProfiles(m.profiles)
    //mm.setPluginRepositories(m.pluginRepositories)
    //mm.setProperties(m.properties)
    //mm.setReporting(m.reporting)
    //mm.setReports(m.reports)
    //mm.setRepositories(m.repositories)
    //mm.setScm(m.scm)
    mm.setUrl(m.url.orNull)
    mm.setVersion(m.gav.version.orNull)
    mm
  }
}