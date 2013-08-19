/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.scala

import java.util.{Properties, Collections}
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import org.apache.maven.model._
import java.io.{StringReader, File, StringWriter}
import org.codehaus.plexus.util.IOUtil
import scala.collection.JavaConverters._
import org.codehaus.plexus.util.xml.Xpp3DomBuilder

@RunWith(classOf[JUnitRunner])
class ScalaModelWriterSpec extends Specification {
  val writer = new ScalaModelWriter

  "The writer" should {
    "write a pom.scala from a minimal Maven model" in {
      val sw = new StringWriter
      val m = new Model
      m.setArtifactId("tesla-polyglot")
      m.setGroupId("io.tesla.polyglot")
      m.setVersion("0.0.1-SNAPSHOT")
      writer.write(sw, Collections.emptyMap[String, AnyRef](), m)
      sw.toString must_== IOUtil.toString(getClass.getClassLoader.getResourceAsStream("minimal-pom.scala"))
    }
    "write a pom.scala from a typical Maven model" in {
      val sw = new StringWriter
      val m = new Model
      m.setArtifactId("tesla-polyglot")
      m.setGroupId("io.tesla.polyglot")
      m.setVersion("0.0.1-SNAPSHOT")
      val dependency1 = new Dependency
      dependency1.setArtifactId("someArtifactId")
      dependency1.setGroupId("someGroupId")
      dependency1.setVersion("someVersion")
      val dependency2 = new Dependency
      dependency2.setArtifactId("someArtifactId")
      dependency2.setGroupId("someGroupId")
      dependency2.setVersion("someVersion")
      dependency2.setScope("test")
      val dependency3 = new Dependency
      dependency3.setArtifactId("someArtifactId")
      dependency3.setGroupId("someGroupId")
      dependency3.setScope("test")
      m.setDependencies(Seq(dependency1, dependency2, dependency3).asJava)
      writer.write(sw, Collections.emptyMap[String, AnyRef](), m)
      sw.toString must_== IOUtil.toString(getClass.getClassLoader.getResourceAsStream("typical-pom.scala"))
    }
    "write a pom.scala from a full Maven model" in {
      val sw = new StringWriter
      val extension = new Extension
      extension.setArtifactId("someArtifactId")
      extension.setGroupId("someGroupId")
      extension.setVersion("someVersion")
      val exclusion = new Exclusion
      exclusion.setArtifactId("someArtifactId")
      exclusion.setGroupId("someGroupId")
      val dependency = new Dependency
      dependency.setArtifactId("someArtifactId")
      dependency.setClassifier("someClassifier")
      dependency.setExclusions(Seq(exclusion).asJava)
      dependency.setGroupId("someGroupId")
      dependency.setOptional(true)
      dependency.setScope("someScope")
      dependency.setSystemPath("someSystemPath")
      dependency.setType("someType")
      dependency.setVersion("someVersion")
      val config = Xpp3DomBuilder.build(
        new StringReader("<someconfiguration>value</someconfiguration>"))
      val pluginExecution = new PluginExecution
      pluginExecution.setGoals(Seq("someGoal").asJava)
      pluginExecution.setId("someId")
      pluginExecution.setPhase("somePhase")
      pluginExecution.setConfiguration(config)
      pluginExecution.setInherited(false)
      val plugin = new Plugin
      plugin.setArtifactId("someArtifactId")
      plugin.setDependencies(Seq(dependency).asJava)
      plugin.setExecutions(Seq(pluginExecution).asJava)
      plugin.setExtensions(true)
      plugin.setGoals(Seq("someGoal").asJava)
      plugin.setGroupId("someGroupId")
      plugin.setVersion("someVersion")
      plugin.setConfiguration(config)
      plugin.setInherited(false)
      val pluginManagement = new PluginManagement
      pluginManagement.setPlugins(Seq(plugin).asJava)
      val resource = new Resource
      resource.setFiltering(true)
      resource.setTargetPath("someTargetPath")
      resource.setDirectory("someDirectory")
      resource.setExcludes(Seq("someExclude").asJava)
      resource.setIncludes(Seq("someInclude").asJava)
      val build = new Build
      build.setExtensions(Seq(extension).asJava)
      build.setOutputDirectory("someOutputDirectory")
      build.setScriptSourceDirectory("someScriptSourceDirectory")
      build.setSourceDirectory("someSourceDirectory")
      build.setTestOutputDirectory("someTestOutputDirectory")
      build.setTestSourceDirectory("someTestSourceDirectory")
      build.setDefaultGoal("someDefaultGoal")
      build.setDirectory("someDirectory")
      build.setFilters(Seq("someFilter").asJava)
      build.setFinalName("someFinalName")
      build.setPluginManagement(pluginManagement)
      build.setPlugins(Seq(plugin).asJava)
      build.setResources(Seq(resource).asJava)
      build.setTestResources(Seq(resource).asJava)
      val m = new Model
      m.setArtifactId("tesla-polyglot")
      m.setGroupId("io.tesla.polyglot")
      m.setVersion("0.0.1-SNAPSHOT")
      m.setBuild(build)
      val cim = new CiManagement
      val notifier = new Notifier
      notifier.setAddress("someAddress")
      val notifierConfig = new Properties()
      notifierConfig.setProperty("someKey", "someValue")
      notifier.setConfiguration(notifierConfig)
      notifier.setSendOnError(false)
      notifier.setSendOnFailure(false)
      notifier.setSendOnSuccess(false)
      notifier.setSendOnWarning(false)
      notifier.setType("someType")
      cim.setNotifiers(Seq(notifier).asJava)
      cim.setSystem("someSystem")
      cim.setUrl("someUrl")
      m.setCiManagement(cim)
      val contributor = new Contributor
      contributor.setEmail("someEmail")
      contributor.setName("someName")
      contributor.setOrganization("someOrganization")
      contributor.setOrganizationUrl("someOrganizationUrl")
      contributor.setRoles(Seq("someRole").asJava)
      contributor.setTimezone("someTimezone")
      contributor.setUrl("someUrl")
      m.setContributors(Seq(contributor).asJava)
      m.setDescription("somedesc")
      val dm = new DependencyManagement
      dm.setDependencies(Seq(dependency).asJava)
      m.setDependencyManagement(dm)
      m.setDependencies(Seq(dependency).asJava)
      val developer = new Developer
      developer.setId("someId")
      developer.setEmail("someEmail")
      developer.setName("someName")
      developer.setOrganization("someOrganization")
      developer.setOrganizationUrl("someOrganizationUrl")
      developer.setRoles(Seq("someRole").asJava)
      developer.setTimezone("someTimezone")
      developer.setUrl("someUrl")
      m.setDevelopers(Seq(developer).asJava)
      val dim = new DistributionManagement
      dim.setDownloadUrl("someDownloadUrl")
      val rel = new Relocation
      rel.setArtifactId("someArtifactId")
      rel.setGroupId("someGroupId")
      rel.setMessage("someMessage")
      rel.setVersion("someVersion")
      dim.setRelocation(rel)
      val dr = new DeploymentRepository
      dr.setUniqueVersion(false)
      dr.setId("someId")
      dr.setLayout("someLayout")
      dr.setName("someName")
      val rp = new RepositoryPolicy
      rp.setChecksumPolicy("someChecksumPolicy")
      rp.setEnabled(false)
      rp.setUpdatePolicy("someUpdatePolicy")
      dr.setReleases(rp)
      dr.setSnapshots(rp)
      dr.setUrl("someUrl")
      dim.setRepository(dr)
      val site = new Site
      site.setId("someId")
      site.setName("someName")
      site.setUrl("someUrl")
      dim.setSite(site)
      dim.setSnapshotRepository(dr)
      dim.setStatus("someStatus")
      m.setDistributionManagement(dim)
      m.setInceptionYear("1988")
      val im = new IssueManagement
      im.setSystem("someSystem")
      im.setUrl("someUrl")
      m.setIssueManagement(im)
      val l = new License
      l.setComments("someComments")
      l.setDistribution("someDistribution")
      l.setName("someName")
      l.setUrl("someUrl")
      m.setLicenses(Seq(l).asJava)
      val ml = new MailingList
      ml.setArchive("someArchive")
      ml.setName("someName")
      ml.setOtherArchives(Seq("someOtherArchive").asJava)
      ml.setPost("somePost")
      ml.setSubscribe("someSubscribe")
      ml.setUnsubscribe("someUnsubscribe")
      m.setMailingLists(Seq(ml).asJava)
      m.setModelEncoding("UTF-16")
      m.setModelVersion("4.0.1")
      m.setModules(Seq("someModule").asJava)
      m.setName("somename")
      val o = new Organization
      o.setName("someName")
      o.setUrl("someUrl")
      m.setOrganization(o)
      m.setPackaging("war")
      val parent = new Parent
      parent.setArtifactId("someArtifactId")
      parent.setGroupId("someGroupId")
      parent.setVersion("someVersion")
      parent.setRelativePath("someRelativePath")
      m.setParent(parent)
      val ps = new Prerequisites
      ps.setMaven("3.0")
      m.setPrerequisites(ps)
      m.setPomFile(new File("/somefile"))
      m.setUrl("someurl")
      writer.write(sw, Collections.emptyMap[String, AnyRef](), m)
      sw.toString must_== IOUtil.toString(getClass.getClassLoader.getResourceAsStream("maximum-props-pom.scala"))
    }
  }
}
