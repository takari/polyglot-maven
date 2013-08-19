/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.scala

import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import java.io.{InputStream, StringReader, File}
import scala.collection.JavaConverters._
import org.codehaus.plexus.util.xml.Xpp3DomBuilder
import org.apache.maven.model.building.{ModelSource, ModelProcessor}
import org.specs2.specification.AfterExample
import org.apache.maven.model.Model
import org.codehaus.plexus.util.FileUtils

@RunWith(classOf[JUnitRunner])
class ScalaModelReaderSpec extends Specification with AfterExample {

  val evalFile = File.createTempFile("ScalaModelReaderSpec", "")
  evalFile.createNewFile()

  val modelSource = new ModelSource {
    def getInputStream(): InputStream = null

    def getLocation(): String = evalFile.getCanonicalPath
  }

  val options = Map(ModelProcessor.SOURCE -> modelSource).asJava
  val reader = new ScalaModelReader

  def readScalaModel(pomFile: String): Model = {
    val is = getClass.getClassLoader.getResourceAsStream(pomFile)
    reader.read(is, options)
  }

  def after: Unit = {
    evalFile.delete()
  }

  sequential

  "The reader" should {
    "read in a minimal pom.scala and produce a model" in {
      val m = readScalaModel("minimal-pom.scala")
      m.getGroupId must_== "io.tesla.polyglot"
      m.getArtifactId must_== "tesla-polyglot"
      m.getVersion must_== "0.0.1-SNAPSHOT"
    }
    "read in a full pom.scala and produce a model" in {
      val m = readScalaModel("maximum-props-pom.scala")
      m.getGroupId must_== "io.tesla.polyglot"
      val build = m.getBuild
      build.getSourceDirectory must_== "someSourceDirectory"
      val extensions = build.getExtensions.asScala
      extensions.size must_== 1
      extensions(0).getArtifactId must_== "someArtifactId"
      extensions(0).getGroupId must_== "someGroupId"
      extensions(0).getVersion must_== "someVersion"
      build.getOutputDirectory must_== "someOutputDirectory"
      build.getScriptSourceDirectory must_== "someScriptSourceDirectory"
      build.getSourceDirectory must_== "someSourceDirectory"
      build.getTestOutputDirectory must_== "someTestOutputDirectory"
      build.getTestSourceDirectory must_== "someTestSourceDirectory"
      build.getDefaultGoal must_== "someDefaultGoal"
      build.getDirectory must_== "someDirectory"
      build.getFilters.asScala must_== Seq("someFilter")
      build.getFinalName must_== "someFinalName"
      val pluginManagement = build.getPluginManagement
      val pluginManagementPlugins = pluginManagement.getPlugins.asScala
      pluginManagementPlugins.size must_== 1
      pluginManagementPlugins(0).getArtifactId must_== "someArtifactId"
      val pluginManagementPluginsDependencies = pluginManagementPlugins(0).getDependencies.asScala
      pluginManagementPluginsDependencies.size must_== 1
      pluginManagementPluginsDependencies(0).getArtifactId must_== "someArtifactId"
      pluginManagementPluginsDependencies(0).getClassifier must_== "someClassifier"
      val pluginManagementPluginsDependenciesExclusions = pluginManagementPluginsDependencies(0).getExclusions.asScala
      pluginManagementPluginsDependenciesExclusions.size must_== 1
      pluginManagementPluginsDependenciesExclusions(0).getArtifactId must_== "someArtifactId"
      pluginManagementPluginsDependenciesExclusions(0).getGroupId must_== "someGroupId"
      pluginManagementPluginsDependencies(0).getGroupId must_== "someGroupId"
      pluginManagementPluginsDependencies(0).getOptional.toBoolean must_== true
      pluginManagementPluginsDependencies(0).getScope must_== "someScope"
      pluginManagementPluginsDependencies(0).getSystemPath must_== "someSystemPath"
      pluginManagementPluginsDependencies(0).getType must_== "someType"
      pluginManagementPluginsDependencies(0).getVersion must_== "someVersion"
      val pluginManagementPluginsExecutions = pluginManagementPlugins(0).getExecutions.asScala
      pluginManagementPluginsExecutions.size must_== 1
      val config = Xpp3DomBuilder.build(new StringReader("<someconfiguration>value</someconfiguration>"))
      val pluginManagementPluginsExecutionsGoals = pluginManagementPluginsExecutions(0).getGoals.asScala
      pluginManagementPluginsExecutionsGoals.size must_== 1
      pluginManagementPluginsExecutionsGoals(0) must_== "someGoal"
      pluginManagementPluginsExecutions(0).getId must_== "someId"
      pluginManagementPluginsExecutions(0).getPhase must_== "somePhase"
      pluginManagementPluginsExecutions(0).getConfiguration must_== config
      pluginManagementPluginsExecutions(0).getInherited.toBoolean must_== false
      pluginManagementPlugins(0).getExtensions.toBoolean must_== true
      pluginManagementPlugins(0).getGroupId must_== "someGroupId"
      pluginManagementPlugins(0).getVersion must_== "someVersion"
      pluginManagementPlugins(0).getConfiguration must_== config
      pluginManagementPlugins(0).getInherited.toBoolean must_== false
      val plugins = build.getPlugins.asScala
      plugins.size must_== 1
      plugins(0).getExtensions.toBoolean must_== true
      plugins(0).getGroupId must_== "someGroupId"
      plugins(0).getVersion must_== "someVersion"
      plugins(0).getConfiguration must_== config
      plugins(0).getInherited.toBoolean must_== false
      val resources = build.getResources.asScala
      resources.size must_== 1
      resources(0).getFiltering.toBoolean must_== true
      resources(0).getTargetPath must_== "someTargetPath"
      resources(0).getDirectory must_== "someDirectory"
      val resourcesExcludes = resources(0).getExcludes.asScala
      resourcesExcludes.size must_== 1
      resourcesExcludes(0) must_== "someExclude"
      val resourcesIncludes = resources(0).getIncludes.asScala
      resourcesIncludes.size must_== 1
      resourcesIncludes(0) must_== "someInclude"
      val testTestResources = build.getTestResources.asScala
      testTestResources.size must_== 1
      testTestResources(0).getFiltering.toBoolean must_== true
      testTestResources(0).getTargetPath must_== "someTargetPath"
      testTestResources(0).getDirectory must_== "someDirectory"
      val testTestResourcesExcludes = testTestResources(0).getExcludes.asScala
      testTestResourcesExcludes.size must_== 1
      testTestResourcesExcludes(0) must_== "someExclude"
      val testTestResourcesIncludes = testTestResources(0).getIncludes.asScala
      testTestResourcesIncludes.size must_== 1
      testTestResourcesIncludes(0) must_== "someInclude"
      m.getArtifactId must_== "tesla-polyglot"
      m.getVersion must_== "0.0.1-SNAPSHOT"
      m.getDescription must_== "somedesc"
      val cim = m.getCiManagement
      val cimNotifiers = cim.getNotifiers.asScala
      cimNotifiers.size must_== 1
      val cimNotifiersNotifier = cimNotifiers(0)
      cimNotifiersNotifier.getAddress must_== "someAddress"
      val cimNotifiersNotifierConfig = cimNotifiersNotifier.getConfiguration
      val cimNotifiersNotifierConfigNames = cimNotifiersNotifierConfig.stringPropertyNames().asScala
      cimNotifiersNotifierConfigNames.size must_== 1
      cimNotifiersNotifierConfigNames.contains("someKey") must_== true
      cimNotifiersNotifierConfig.getProperty("someKey") must_== "someValue"
      cimNotifiersNotifier.isSendOnError must_== false
      cimNotifiersNotifier.isSendOnFailure must_== false
      cimNotifiersNotifier.isSendOnSuccess must_== false
      cimNotifiersNotifier.isSendOnWarning must_== false
      cimNotifiersNotifier.getType must_== "someType"
      cim.getSystem must_== "someSystem"
      cim.getUrl must_== "someUrl"
      val contribs = m.getContributors.asScala
      contribs.size must_== 1
      contribs(0).getEmail must_== "someEmail"
      contribs(0).getName must_== "someName"
      contribs(0).getOrganization must_== "someOrganization"
      contribs(0).getOrganizationUrl must_== "someOrganizationUrl"
      val contribsRoles = contribs(0).getRoles.asScala
      contribsRoles.size must_== 1
      contribsRoles(0) must_== "someRole"
      contribs(0).getTimezone must_== "someTimezone"
      contribs(0).getUrl must_== "someUrl"
      val dm = m.getDependencyManagement
      val dependencyManagementDependencies = dm.getDependencies.asScala
      dependencyManagementDependencies.size must_== 1
      dependencyManagementDependencies(0).getArtifactId must_== "someArtifactId"
      dependencyManagementDependencies(0).getClassifier must_== "someClassifier"
      val dependencyManagementDependenciesExclusions = dependencyManagementDependencies(0).getExclusions.asScala
      dependencyManagementDependenciesExclusions.size must_== 1
      dependencyManagementDependenciesExclusions(0).getArtifactId must_== "someArtifactId"
      dependencyManagementDependenciesExclusions(0).getGroupId must_== "someGroupId"
      dependencyManagementDependencies(0).getGroupId must_== "someGroupId"
      dependencyManagementDependencies(0).getOptional.toBoolean must_== true
      dependencyManagementDependencies(0).getScope must_== "someScope"
      dependencyManagementDependencies(0).getSystemPath must_== "someSystemPath"
      dependencyManagementDependencies(0).getType must_== "someType"
      dependencyManagementDependencies(0).getVersion must_== "someVersion"
      val dependencies = m.getDependencies.asScala
      dependencies.size must_== 1
      dependencies(0).getArtifactId must_== "someArtifactId"
      dependencies(0).getClassifier must_== "someClassifier"
      val dependenciesExclusions = dependencies(0).getExclusions.asScala
      dependenciesExclusions.size must_== 1
      dependenciesExclusions(0).getArtifactId must_== "someArtifactId"
      dependenciesExclusions(0).getGroupId must_== "someGroupId"
      dependencies(0).getGroupId must_== "someGroupId"
      dependencies(0).getOptional.toBoolean must_== true
      dependencies(0).getScope must_== "someScope"
      dependencies(0).getSystemPath must_== "someSystemPath"
      dependencies(0).getType must_== "someType"
      dependencies(0).getVersion must_== "someVersion"
      val developers = m.getDevelopers.asScala
      developers.size must_== 1
      developers(0).getId must_== "someId"
      developers(0).getEmail must_== "someEmail"
      developers(0).getName must_== "someName"
      developers(0).getOrganization must_== "someOrganization"
      developers(0).getOrganizationUrl must_== "someOrganizationUrl"
      val developersRoles = developers(0).getRoles.asScala
      developersRoles.size must_== 1
      developersRoles(0) must_== "someRole"
      developers(0).getTimezone must_== "someTimezone"
      developers(0).getUrl must_== "someUrl"
      val distMan = m.getDistributionManagement
      distMan.getDownloadUrl must_== "someDownloadUrl"
      val distManRelocation = distMan.getRelocation
      distManRelocation.getArtifactId must_== "someArtifactId"
      distManRelocation.getGroupId must_== "someGroupId"
      distManRelocation.getVersion must_== "someVersion"
      distManRelocation.getMessage must_== "someMessage"
      val distManRepo = distMan.getRepository
      distManRepo.getId must_== "someId"
      distManRepo.getLayout must_== "someLayout"
      distManRepo.getName must_== "someName"
      val distManRepoRels = distManRepo.getReleases
      distManRepoRels.getChecksumPolicy must_== "someChecksumPolicy"
      distManRepoRels.isEnabled must_== false
      distManRepoRels.getUpdatePolicy must_== "someUpdatePolicy"
      val distManRepoSnaps = distManRepo.getSnapshots
      distManRepoSnaps.getChecksumPolicy must_== "someChecksumPolicy"
      distManRepoSnaps.isEnabled must_== false
      distManRepoSnaps.getUpdatePolicy must_== "someUpdatePolicy"
      distManRepo.getUrl must_== "someUrl"
      val distManSite = distMan.getSite
      distManSite.getId must_== "someId"
      distManSite.getName must_== "someName"
      distManSite.getUrl must_== "someUrl"
      val distManSnaps = distMan.getSnapshotRepository
      distManSnaps.getId must_== "someId"
      distManSnaps.getLayout must_== "someLayout"
      distManSnaps.getName must_== "someName"
      val distManSnapsRels = distManSnaps.getReleases
      distManSnapsRels.getChecksumPolicy must_== "someChecksumPolicy"
      distManSnapsRels.isEnabled must_== false
      distManSnapsRels.getUpdatePolicy must_== "someUpdatePolicy"
      val distManSnapsSnaps = distManSnaps.getSnapshots
      distManSnapsSnaps.getChecksumPolicy must_== "someChecksumPolicy"
      distManSnapsSnaps.isEnabled must_== false
      distManSnapsSnaps.getUpdatePolicy must_== "someUpdatePolicy"
      distManSnaps.getUrl must_== "someUrl"
      distMan.getStatus must_== "someStatus"
      m.getInceptionYear must_== "1988"
      val im = m.getIssueManagement
      im.getSystem must_== "someSystem"
      im.getUrl must_== "someUrl"
      val ls = m.getLicenses.asScala
      ls.size must_== 1
      ls(0).getComments must_== "someComments"
      ls(0).getDistribution must_== "someDistribution"
      ls(0).getName must_== "someName"
      ls(0).getUrl must_== "someUrl"
      val ml = m.getMailingLists.asScala
      ml.size must_== 1
      ml(0).getArchive must_== "someArchive"
      ml(0).getName must_== "someName"
      val mlOtherArchives = ml(0).getOtherArchives.asScala
      mlOtherArchives.size must_== 1
      mlOtherArchives(0) must_== "someOtherArchive"
      ml(0).getPost must_== "somePost"
      ml(0).getSubscribe must_== "someSubscribe"
      ml(0).getUnsubscribe must_== "someUnsubscribe"
      m.getModelEncoding must_== "UTF-16"
      m.getModelVersion must_== "4.0.1"
      val ms = m.getModules.asScala
      ms.size must_== 1
      ms(0) must_== "someModule"
      m.getName must_== "somename"
      val o = m.getOrganization
      o.getName must_== "someName"
      o.getUrl must_== "someUrl"
      m.getPackaging must_== "war"
      val parent = m.getParent
      parent.getArtifactId must_== "someArtifactId"
      parent.getGroupId must_== "someGroupId"
      parent.getVersion must_== "someVersion"
      parent.getRelativePath must_== "someRelativePath"
      val ps = m.getPrerequisites
      ps.getMaven must_== "3.0"
      m.getUrl must_== "someurl"
    }
  }
}
