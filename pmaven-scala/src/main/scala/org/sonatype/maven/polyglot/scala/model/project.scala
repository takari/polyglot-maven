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

package org.sonatype.maven.polyglot.scala.model

import org.apache.maven.model.{
    ModelBase => ApacheModelBase,
    Model => ApacheModel,
    Contributor => ApacheContributor,
    Developer => ApacheDeveloper,
    License => ApacheLicense,
    MailingList => ApacheMailingList,
    Profile => ApacheProfile,
    Dependency => ApacheDependency,
    Exclusion => ApacheExclusion,
    Activation => ApacheActivation,
    ActivationOS => ApacheActivationOS,
    ActivationProperty => ApacheActivationProperty,
    ActivationFile => ApacheActivationFile,
    DistributionManagement => ApacheDistributionManagement,
    Relocation => ApacheRelocation,
    RepositoryBase => ApacheRepositoryBase,
    Repository => ApacheRepository,
    RepositoryPolicy => ApacheRepositoryPolicy,
    DeploymentRepository => ApacheDeploymentRepository,
    Resource => ApacheResource,
    IssueManagement => ApacheIssueManagement,
    Scm => ApacheScm,
    Site => ApacheSite,
    CiManagement => ApacheCiManagement,
    Notifier => ApacheNotifier,
    Prerequisites => ApachePrerequisites,
    BuildBase => ApacheBuildBase,
    Build => ApacheBuild,
    Extension => ApacheExtension,
    Reporting => ApacheReporting,
    PluginManagement => ApachePluginManagement,
    ConfigurationContainer => ApacheConfigurationContainer,
    PluginExecution => ApachePluginExecution,
    Plugin => ApachePlugin,
    PluginConfiguration => ApachePluginConfiguration,
    PluginContainer => ApachePluginContainer,
    ReportPlugin => ApacheReportPlugin,
    ReportSet => ApacheReportSet,
    Parent => ApacheParent,
    Organization => ApacheOrganization,
    DependencyManagement => ApacheDependencyManagement
}

import scala.collection.JavaConversions._
import scala.collection.mutable.{Buffer, Map}

import java.io.StringReader
import project._

import org.codehaus.plexus.util.xml._

object project {

  def apply(body: (Model) => Unit): Model = {
    val m = new Model
    body(m)
    m
  }
  
  def apply(coordinates: String): Model = {
    val m = new Model
    m coords coordinates
    m
  }

  /**
   * Convert Scala XML elements to DOM Elements implicitly. Needed for the
   * several places where Maven requires DOM Elements, and the DSL supports Scala
   * literal XML Elems. Need to convert Elem to DOM Element automatically.
   **/
  implicit def scalaElem2Xpp3Dom(sel: scala.xml.Elem): Xpp3Dom = {
    Xpp3DomBuilder.build(new StringReader(sel.toString))
  }
}

trait ModelBaseProps {
  self: ApacheModelBase =>

  def distributionManagement: ApacheDistributionManagement = getDistributionManagement
  def distributionManagement(body: (DistributionManagement) => Unit): DistributionManagement = {
    val d = new DistributionManagement
    body(d)
    setDistributionManagement(d)
    d
  }
  
  def modules = (getModules: Buffer[String])
  
  def repositories = (getRepositories: Buffer[ApacheRepository])
  def repository(body: (Repository) => Unit): Repository = {
    val r = new Repository
    r.apply(body)
    addRepository(r)
    r
  }
  def repository(id: String): Repository =
    repository( _.id_=(id) )
  
  def pluginRepositories = (getPluginRepositories: Buffer[ApacheRepository])
  def pluginRepository(body: (Repository) => Unit): Repository = {
    val r = new Repository
    r.apply(body)
    addPluginRepository(r)
    r
  }
  
  def dependencies = (getDependencies: Buffer[ApacheDependency])
  def dependency(body: (Dependency) => Unit): Dependency = {
    val d = new Dependency
    d.apply(body)
    addDependency(d)
    d
  }
  def dependency(aref: String): Dependency =
    dependency(_ artifactRef aref)
  
  def reporting: ApacheReporting = getReporting
  def reporting(body: (Reporting) => Unit): Reporting = {
    val r = new Reporting
    body(r)
    setReporting(r)
    r
  }
  
  def dependencyManagement: ApacheDependencyManagement = getDependencyManagement
  def dependencyManagement(body: (DependencyManagement) => Unit): DependencyManagement = {
    val dm = new DependencyManagement
    body(dm)
    setDependencyManagement(dm)
    dm
  }

  def properties = (getProperties: Map[java.lang.Object, java.lang.Object])
}

class Model extends ApacheModel with ModelBaseProps with WithCoords[Model] {
  def modelVersion: String = getModelVersion
  def modelVersion_=(s: String) = setModelVersion(s)
  
  def artifactId: String = getArtifactId
  def artifactId_=(s: String) = setArtifactId(s)
  
  def groupId: String = getGroupId
  def groupId_=(s: String) = setGroupId(s)
  
  def version: String = getVersion
  def version_=(s: String) = setVersion(s)
  
  def description: String = getDescription
  def description_=(s: String) = setDescription(s)
  
  def inceptionYear: String = getInceptionYear
  def inceptionYear_=(s: String) = setInceptionYear(s)
  
  def organization: ApacheOrganization = getOrganization
  def organization(body: (Organization) => Unit): Organization = {
    val o = new Organization
    body(o)
    setOrganization(o)
    o
  }
  
  def modelEncoding: String = getModelEncoding
  def modelEncoding_=(s: String) = setModelEncoding(s)
  
  def name: String = getName
  def name_=(s: String) = setName(s)
  
  def packaging: String = getPackaging
  def packaging_=(s: String) = setPackaging(s)
  
  def url: String = getUrl
  def url_=(s: String) = setUrl(s)
  
  def contributors = (getContributors: Buffer[ApacheContributor])
  def contributor(body: (ContributorProps) => Unit): ContributorProps = {
    val c = new ApacheContributor with ContributorProps
    body(c)
    addContributor(c)
    c
  }
  def contributor(name: String): ContributorProps =
    contributor (_.name = name)
    
  def parent: ApacheParent = getParent
  def parent(body: (Parent) => Unit): Parent = {
    val par = new Parent
    body(par)
    setParent(par)
    par
  }
  def parent(coordinates: String): Parent =
    parent(_ coords coordinates)

  def developers = (getDevelopers: Buffer[ApacheDeveloper])
  def developer(body: (Developer) => Unit): Developer = {
    val d = new Developer
    body(d)
    addDeveloper(d)
    d
  }
  def developer(id: String): Developer =
    developer (_.id = id)

  def licenses = (getLicenses: Buffer[ApacheLicense])
  def license(body: (License) => Unit): License = {
    val l = new License
    body(l)
    addLicense(l)
    l
  }
  def license(iname: String): License =
    license (_.name = name)

  def mailingLists = (getMailingLists: Buffer[ApacheMailingList])
  def mailingList(body: (MailingList) => Unit): MailingList = {
    val ml = new MailingList
    body(ml)
    addMailingList(ml)
    ml
  }
  def mailingList(name: String): MailingList =
    mailingList (_.name = name)


  def profiles = (getProfiles: Buffer[ApacheProfile])
  def profile(body: (Profile) => Unit): Profile = {
    val p = new Profile
    body(p)
    addProfile(p)
    p
  }
  def profile(id: String): Profile =
    profile(_.id_=(name))
  
  def issueManagement: ApacheIssueManagement = getIssueManagement
  def issueManagement(body: (IssueManagement) => Unit): IssueManagement = {
    val i = new IssueManagement
    body(i)
    setIssueManagement(i)
    i
  }
  
  def scm: ApacheScm = getScm
  def scm(body: (Scm) => Unit): Scm = {
    val s = new Scm
    body(s)
    setScm(s)
    s
  }
  
  def ciManagement: ApacheCiManagement = getCiManagement
  def ciManagement(body: (CiManagement) => Unit): CiManagement = {
    val c = new CiManagement
    body(c)
    setCiManagement(c)
    c
  }
  
  def prerequisites: ApachePrerequisites = getPrerequisites
  def prerequisites(body: (Prerequisites) => Unit): Prerequisites = {
    val p = new Prerequisites
    body(p)
    setPrerequisites(p)
    p
  }
  
  def build: ApacheBuild = getBuild
  def build(body: (Build) => Unit): Build = {
    val b = new Build
    body(b)
    setBuild(b)
    b
  }
  
  def apply(body: (Model) => Unit): Model = {
    body(this)
    this
  }
  
  /**
   * <p>
   * Convenience function to configure the Model object with the most common
   * Scala plugin configuration options, as well as reference to the scala-tools
   * repository. That is, the following Scala DSL code
   * </p>
   *
   * <p><blockquote><pre>
   *   project { proj =>
   *     proj includesScalaSourceCode "2.7.7"
   *   }
   * </pre></blockquote></p>
   *
   * <p>
   * Is equivalent to the following Maven XML configuration options
   * added to the model:
   * </p>
   *
   * <p><blockquote><pre>
   *   &lt;project>
   *
   *     &lt;repositories>
   *       &lt;repository>
   *         &lt;id>scala-tools.org&lt;/id>
   *         &lt;name>Scala-tools Maven2 Repository&lt;/name>
   *         &lt;url>http://scala-tools.org/repo-releases&lt;/url>
   *       &lt;/repository>
   *     &lt;/repositories>
   *
   *     &lt;pluginRepositories>
   *       &lt;pluginRepository>
   *         &lt;id>scala-tools.org</id>
   *         &lt;name>Scala-tools Maven2 Repository&lt;/name>
   *         &lt;url>http://scala-tools.org/repo-releases&lt;/url>
   *       &lt;/pluginRepository>
   *     &lt;/pluginRepositories>
   *
   *     &lt;build>
   *       &lt;sourceDirectory>src/main/scala&lt;/sourceDirectory>
   *       &lt;testSourceDirectory>src/test/scala&lt;/testSourceDirectory>
   *
   *       &lt;plugins>
   *         &lt;plugin>
   *           &lt;groupId>org.scala-tools&/groupId>
   *           &lt;artifactId>maven-scala-plugin&lt;/artifactId>
   *           &lt;executions>
   *             &lt;execution>
   *               &lt;goals>
   *                 &lt;goal>compile&lt;/goal>
   *                 &lt;goal>testCompile&lt;/goal>
   *               &lt;/goals>
   *             &lt;/execution>
   *           &lt;/executions>
   *           &lt;configuration>
   *             &lt;scalaVersion><i><b>version</b></i>&lt;/scalaVersion>
   *           &lt;/configuration>
   *         &lt;/plugin>
   *       &lt;plugins>
   *     &lt;/build>
   *
   *   &lt;/project>
   * </pre></blockquote></p>
   **/
  def includesScalaSourceCode(version: String): Unit = {
    repository { repo =>
      repo.id = "scala-tools.org"
      repo.name = "Scala-tools Maven2 Repository"
      repo.url = "http://scala-tools.org/repo-releases"
    }
    
    pluginRepository { repo =>
      repo.id = "scala-tools.org"
      repo.name = "Scala-tools Maven2 Repository"
      repo.url = "http://scala-tools.org/repo-releases"
    }
    
    build { b =>
      b.plugin { pi =>
        pi.groupId = "org.scala-tools"
        pi.artifactId = "maven-scala-plugin"
        pi.execution { e =>
          e.goals += ("compile", "testCompile")
          e.configuration =
            <configuration>
              <scalaVersion>{version}</scalaVersion>
            </configuration>
        }
      }
    }
  }
}

trait WithCoords[This] {
  self: This =>
  
  def groupId_=(s: String): Unit
  def artifactId_=(s: String): Unit
  def version_=(s: String): Unit

  /**
   * parses the input string as a Maven coordinate "groupId:artifactId(:version)?" triple,
   * and sets the values of this object accordingly.
   **/
  def coords(coordinates: String): This = {
    val cpatt = "([^:]+):([^:]+)(:(.+))?".r
    coordinates match {
      case cpatt(gid, aid, _, ver) if ver.length > 0 =>
        groupId_=(gid)
        artifactId_=(aid)
        version_=(ver)
      
      case cpatt(gid, aid, _, _) =>
        groupId_=(gid)
        artifactId_=(aid)
    }
    this
  }
}
  
class Parent extends ApacheParent with WithCoords[Parent] {
  def groupId: String = getGroupId
  def groupId_=(s: String) = setGroupId(s)
  
  def artifactId: String = getArtifactId
  def artifactId_=(s: String) = setArtifactId(s)
  
  def version: String = getVersion
  def version_=(s: String) = setVersion(s)
  
  def relativePath: String = getRelativePath
  def relativePath_=(s: String) = setRelativePath(s)
  
  def apply(body: (Parent) => Unit): Parent = {
    body(this)
    this
  }
}

trait ContributorProps {
  self: ApacheContributor =>
  
  def email: String = getEmail
  def email_=(s: String) = setEmail(s)
  
  def name: String = getName
  def name_=(s: String) = setName(s)
  
  def organization: String = getOrganization
  def organization_=(s: String) = setOrganization(s)
  
  def organizationUrl: String = getOrganizationUrl
  def organizationUrl_=(s: String) = setOrganizationUrl(s)
  
  def timezone: String = getTimezone
  def timezone_=(s: String)= setTimezone(s)
  
  def roles = (getRoles: Buffer[String])
  def properties = (getProperties: Map[java.lang.Object, java.lang.Object])
}

class Developer extends ApacheDeveloper with ContributorProps {
  def id: String = getId
  def id_=(s: String) = setId(s)
  
  def apply(body: (Developer) => Unit): Developer = {
    body(this)
    this
  }
}

class License extends ApacheLicense {
  def name: String = getName
  def name_=(s: String) = setName(s)
  
  def url: String = getUrl
  def url_=(s: String) = setUrl(s)
  
  def distribution: String = getDistribution
  def distribution_=(s: String) = setDistribution(s)
  
  def comments: String = getComments
  def comments_=(s: String) = setComments(s)
}

class MailingList extends ApacheMailingList {
  def name: String = getName
  def name_=(s: String) = setName(s)
  
  def subscribe: String = getSubscribe
  def subscribe_=(s: String) = setSubscribe(s)
  
  def unsubscribe: String = getUnsubscribe
  def unsubscribe_=(s: String) = setUnsubscribe(s)
  
  def post: String = getPost
  def post_=(s: String) = setPost(s)
  
  def archive: String = getArchive
  def archive_=(s: String) = setArchive(s)
  
  val otherArchives = (getOtherArchives: Buffer[String])
}

class DependencyManagement extends ApacheDependencyManagement {
  def dependencies = (getDependencies: Buffer[ApacheDependency])
  def dependency(body: (Dependency) => Unit): Dependency = {
    val d = new Dependency
    d.apply(body)
    addDependency(d)
    d
  }
  def dependency(aref: String): Dependency =
    dependency(_ artifactRef aref)
  
}

class Dependency extends ApacheDependency {
  def artifactRef(coordinates: String): Dependency = {
    val deppatt = "([^:]+):([^:]+)(:([^:]+)(:([^:]+)(:([^:]+))?)?)?".r
    coordinates match {
      case deppatt(gid, aid, _, typ, _, classifier, _, version) if version != null && version.length > 0 =>
        coords(gid, aid, typ, classifier, version)
      case deppatt(gid, aid, _, typ, _, version,_, _) if version != null && version.length > 0 =>
        coords(gid, aid, typ, version)
      case deppatt(gid, aid, _, version, _, _, _, _) if version != null && version.length > 0 =>
        coords(gid, aid, version)
      case deppatt(gid, aid, _, _, _, _, _, _) =>
        coords(gid, aid)
    }
    this
  }
  
  def apply(body: (Dependency) => Unit): Dependency = {
    body(this)
    this
  }
  
  def coords(gid: String, aid: String, typ: String, classifier: String, ver: String): Unit = {
    groupId_=(gid)
    artifactId_=(aid)
    version_=(ver)
    classifier_=(classifier)
    _type_=(typ)
  }
  def coords(gid: String, aid: String, typ: String, ver: String): Unit =
    coords(gid, aid, typ, null, ver)
  def coords(gid: String, aid: String, ver: String): Unit =
    coords(gid, aid, null, null, ver)
  def coords(gid: String, aid: String): Unit =
    coords(gid, aid, null, null, null)
  
  def groupId: String = getGroupId
  def groupId_=(s: String) = setGroupId(s)

  def artifactId: String = getArtifactId
  def artifactId_=(s: String) = setArtifactId(s)
  
  def version: String = getVersion
  def version_=(s: String) = setVersion(s)
  
  /**
   * Using symbol "_type" to avoid Scala keyword "type" collision
   **/
  def _type: String = getType
  def _type_=(s: String) = setType(s)
  
  def classifier: String = getClassifier
  def classifier_=(s: String) = setClassifier(s)
  
  def scope: String = getScope
  def scope_=(s: String) = setScope(s)
  
  def systemPath: String = getSystemPath
  def systemPath_=(s: String) = setSystemPath(s)
  
  def optional: String = getOptional
  def optional_=(s: String) = setOptional(s)
  def optional_=(b: Boolean) = setOptional(b)
  
  def exclusions = (getExclusions: Buffer[ApacheExclusion])
  def exclusion(body: (Exclusion) => Unit): Exclusion = {
    val e = new Exclusion
    e(body)
    addExclusion(e)
    e
  }
  def exclusion(coordinates: String): Exclusion =
    exclusion {_.artifactRef(coordinates) }
}

class Exclusion extends ApacheExclusion {
  def artifactRef(coordinates: String): Exclusion = {
    val deppatt = "([^:]+):([^:]+)".r
    coordinates match {
      case deppatt(gid, aid) =>
        coords(gid, aid)
    }
    this
  }
  
  def apply(body: (Exclusion) => Unit): Exclusion = {
    body(this)
    this
  }
  
  def coords(gid: String, aid: String): Unit = {
    groupId_=(gid)
    artifactId_=(aid)
  }
  
  def groupId: String = getGroupId
  def groupId_=(s: String) = setGroupId(s)
  
  def artifactId: String = getArtifactId
  def artifactId_=(s: String) = setArtifactId(s)
}

class Activation extends ApacheActivation {
  def activeByDefault: Boolean = isActiveByDefault
  def activeByDefault_=(f: Boolean) = setActiveByDefault(f)
  
  def jdk: String = getJdk
  def jdk_=(s: String) = setJdk(s)
  
  def os: ApacheActivationOS = getOs
  def os(body: (ActivationOS) => Unit): ActivationOS = {
    val a = new ActivationOS
    body(a)
    setOs(a)
    a
  }
  
  def property: ApacheActivationProperty = getProperty
  def property(body: (ActivationProperty) => Unit): ActivationProperty = {
    val a = new ActivationProperty
    body(a)
    setProperty(a)
    a
  }
  
  def file: ApacheActivationFile = getFile
  def file(body: (ActivationFile) => Unit): ActivationFile = {
    val a = new ActivationFile
    body(a)
    setFile(a)
    a
  }
}

class ActivationOS extends ApacheActivationOS {
  def name: String = getName
  def name_=(s: String) = setName(s)
  
  def family: String = getFamily
  def family_=(s: String) = setFamily(s)
  
  def arch: String = getArch
  def arch_=(s: String) = setArch(s)
  
  def version: String = getVersion
  def version_=(s: String) = setVersion(s)
}

class ActivationProperty extends ApacheActivationProperty{
  def name: String = getName
  def name_=(s: String) = setName(s)
  
  def value: String = getValue
  def value_=(s: String) = setValue(s)
}

class ActivationFile extends ApacheActivationFile {
  def exists: String = getExists
  def exists_=(s: String) = setExists(s)
  
  def missing: String = getMissing
  def missing_=(s: String) = setMissing(s)
}

class DistributionManagement extends ApacheDistributionManagement {
  def repository: ApacheDeploymentRepository = getRepository
  def repository(body: (DeploymentRepository) => Unit): DeploymentRepository = {
    val d = new DeploymentRepository
    body(d)
    setRepository(d)
    d
  }
  def repository(id: String): DeploymentRepository =
    repository(_.id_=(id))
  
  def snapshotRepository: ApacheDeploymentRepository = getSnapshotRepository
  def snapshotRepository(body: (DeploymentRepository) => Unit): DeploymentRepository = {
    val d = new DeploymentRepository
    body(d)
    setSnapshotRepository(d)
    d
  }  
  def snapshotRepository(id: String): DeploymentRepository =
    snapshotRepository(_.id_=(id))
  
  
  def site: ApacheSite = getSite
  def site(body: (Site) => Unit) = {
    val s = new Site
    body(s)
    setSite(s)
    s
  }
  def site(id: String): Site =
    site(_.id_=(id))
  
  def downloadUrl: String = getDownloadUrl
  def downloadUrl_=(s: String) = setDownloadUrl(s)
  
  def status: String = getStatus
  def status_=(s: String) = setStatus(s)
  
  def relocation: ApacheRelocation = getRelocation
  def relocation(body: (Relocation) => Unit): Relocation = {
    val r = new Relocation
    body(r)
    setRelocation(r)
    r
  }
}

class DeploymentRepository extends ApacheDeploymentRepository with RepositoryBaseProps {
  def uniqueVersion: Boolean = isUniqueVersion
  def uniqueVersion_=(b: Boolean) = setUniqueVersion(b)
  
  def apply(body: (DeploymentRepository) => Unit): DeploymentRepository = {
    body(this)
    this
  }
}

class Relocation extends ApacheRelocation {
  def groupId: String = getGroupId
  def groupId_=(s: String) = setGroupId(s)
  
  def artifactId: String = getArtifactId
  def artifactId_=(s: String) = setArtifactId(s)
  
  def version: String = getVersion
  def version_=(s: String) = setVersion(s)
  
  def message: String = getMessage
  def message_=(s: String) = setMessage(s)
}

trait RepositoryBaseProps {
  self: ApacheRepositoryBase =>
  
  def id: String = getId
  def id_=(s: String) = setId(s)
  
  def name: String = getName
  def name_=(s: String) = setName(s)
  
  def url: String = getUrl
  def url_=(s: String) = setUrl(s)
  
  def layout: String = getLayout
  def layout_=(s: String) = setLayout(s)
}

class Repository extends ApacheRepository with RepositoryBaseProps {
  def apply(body: (Repository) => Unit): Repository = {
    body(this);
    this
  }
  
  def releases: ApacheRepositoryPolicy = getReleases
  def releases(body: (RepositoryPolicy) => Unit): RepositoryPolicy = {
    val p = new RepositoryPolicy
    body(p)
    setReleases(p)
    p
  }

  def snapshots: ApacheRepositoryPolicy = getReleases
  def snapshots(body: (RepositoryPolicy) => Unit): RepositoryPolicy = {
    val p = new RepositoryPolicy
    body(p)
    setSnapshots(p)
    p
  }
}

class RepositoryPolicy extends ApacheRepositoryPolicy {
 def enabled: String = getEnabled
 def enabled_=(s: String) = setEnabled(s)
 
 def updatePolicy: String = getUpdatePolicy
 def updatePolicy_=(s: String) = setUpdatePolicy(s)
 
 def checksumPolicy: String = getChecksumPolicy
 def checksumPolicy_=(s: String) = setChecksumPolicy(s)
}

class Profile extends ApacheProfile with ModelBaseProps {
  def id: String = getId
  def id_=(s: String) = setId(s)
  
  def activation: ApacheActivation = getActivation
  def activation(body: (Activation) => Unit): Activation = {
    val a = new Activation
    body(a)
    setActivation(a)
    a
  }
  
  def build: ApacheBuildBase = getBuild
  def build(body: (Build) => Unit): Build = {
    val b = new Build
    body(b)
    setBuild(b)
    b
  }
  
  def apply(body: (Profile) => Unit): Profile = {
    body(this)
    this
  }
}

trait BuildBaseProps extends PluginConfigurationProps {
  self: ApacheBuildBase =>
  
  def defaultGoal: String = getDefaultGoal
  def defaultGoal_=(s: String) = setDefaultGoal(s)
  
  def directory: String = getDirectory
  def directory_=(s: String) = setDirectory(s)
  
  def finalName: String = getFinalName
  def finalName_=(s: String) = setFinalName(s)
  
  def resources = (getResources: Buffer[ApacheResource])
  def resource(body: (Resource) => Unit): Resource = {
    val r = new Resource
    body(r)
    addResource(r)
    r
  }

  def testResources = (getTestResources: Buffer[ApacheResource])  
  def testResource(body: (Resource) => Unit): Resource = {
    val r = new Resource
    body(r)
    addTestResource(r)
    r
  }
  
  def filters = (getFilters: Buffer[String])
}

class Build extends ApacheBuild with BuildBaseProps {
  def sourceDirectory: String = getSourceDirectory
  def sourceDirectory_=(s: String) = setSourceDirectory(s)
  
  def scriptSourceDirectory: String = getScriptSourceDirectory
  def scriptSourceDirectory_=(s: String) = setScriptSourceDirectory(s)
  
  def testSourceDirectory: String = getTestSourceDirectory
  def testSourceDirectory_=(s: String) = setTestSourceDirectory(s)
  
  def testOutputDirectory: String = getTestOutputDirectory
  def testOutputDirectory_=(s: String) = setTestOutputDirectory(s)
  
  def extensions = (getExtensions: Buffer[ApacheExtension])
  def extension(body: (Extension) => Unit): Extension = {
    val e = new Extension
    body(e)
    addExtension(e)
    e
  }
}

class Resource extends ApacheResource {
  def targetPath: String = getTargetPath
  def targetPath_=(s: String) = setTargetPath(s)
  
  def filtering: String = getFiltering
  def filtering_=(s: String) = setFiltering(s)
}

class IssueManagement extends ApacheIssueManagement {
  def system: String = getSystem
  def system_=(s: String) = setSystem(s)
  
  def url: String = getUrl
  def url_=(s: String) = setUrl(s)
}

class Scm extends ApacheScm {
  def connection: String = getConnection
  def connection_=(s: String) = setConnection(s)
  
  def developerConnection: String = getDeveloperConnection
  def developerConnection_=(s: String) = setDeveloperConnection(s)
  
  def tag: String = getTag
  def tag_=(s: String) = setTag(s)
  
  def url: String = getUrl
  def url_=(s: String) = setUrl(s)
}

class CiManagement extends ApacheCiManagement {
  def system: String = getSystem
  def system_=(s: String) = setSystem(s)
 
  def url: String = getUrl
  def url_=(s: String) = setUrl(s)
 
  def notifiers = (getNotifiers: Buffer[ApacheNotifier])
  def notifier(body: (Notifier) => Unit): Notifier = {
    val n = new Notifier
    body(n)
    addNotifier(n)
    n
  }
}

class Notifier extends ApacheNotifier {
  /**
   * Using symbol "_type" to avoid Scala keyword collision
   **/
  def _type: String = getType
  def _type_=(s: String) = setType(s)
  
  def sendOnError: Boolean = isSendOnError
  def sendOnError_=(b: Boolean) = setSendOnError(b)
  
  def sendOnFailure: Boolean = isSendOnFailure
  def sendOnFailure_=(b: Boolean) = setSendOnFailure(b)
  
  def sendOnSuccess: Boolean = isSendOnSuccess
  def sendOnSuccess_=(b: Boolean) = setSendOnSuccess(b)
  
  def sendOnWarning: Boolean = isSendOnWarning
  def sendOnWarning_=(b: Boolean) = setSendOnWarning(b)

  def address: String = getAddress
  def address_=(s: String) = setAddress(s)
  
  def configuration = (getConfiguration: Map[java.lang.Object, java.lang.Object])
}

class Prerequisites extends ApachePrerequisites {
  def maven: String = getMaven
  def maven_=(s: String) = setMaven(s)
}

class Extension extends ApacheExtension with WithCoords[Extension] {
  def groupId: String = getGroupId
  def groupId_=(s: String) = setGroupId(s)
  
  def artifactId: String = getArtifactId
  def artifactId_=(s: String) = setArtifactId(s)
  
  def version: String = getVersion
  def version_=(s: String) = setVersion(s)
}

class Reporting extends ApacheReporting {
  def excludeDefaults: String = getExcludeDefaults
  def excludeDefaults_=(s: String) = setExcludeDefaults(s)
  
  def outputDirectory: String = getOutputDirectory
  def outputDirectory_=(s: String) = setOutputDirectory(s)
  
  def plugins = (getPlugins: Buffer[ApacheReportPlugin])
  def plugin(body: (ReportPlugin) => Unit): ReportPlugin = {
    val r = new ReportPlugin
    body(r)
    addPlugin(r)
    r
  }
  def plugin(coordinates: String): ReportPlugin =
    plugin(_ coords coordinates)
}

trait PluginContainerProps {
  self: ApachePluginContainer =>
  
  def plugins = (getPlugins: Buffer[ApachePlugin])
  def plugin(body: (Plugin) => Unit): Plugin = {
    val p = new Plugin
    p.apply(body)
    addPlugin(p)
    p
  }
  def plugin(coordinates: String): Plugin =
    plugin((pi: Plugin) => pi.coords(coordinates))
}

trait PluginConfigurationProps extends PluginContainerProps {
  self: ApachePluginConfiguration =>

  def pluginManagement: ApachePluginManagement = getPluginManagement
  def pluginManagement(body: (PluginManagement) => Unit): PluginManagement = {
    val m = new PluginManagement
    body(m)
    setPluginManagement(m)
    m
  }
}

class Plugin extends ApachePlugin with ConfigurationContainerProps with WithCoords[Plugin] {
  /**
   * <p>
   * Intended to support creation and an initialization closure in the DSL.
   * For example, the following creates and initializes a Plugin from a PluginContainerProps
   * instance "cont":
   * </p>
   *
   * <p><blockquote><pre>
   *   ...
   *   cont.plugin("org.whatever:some-plugin:0.1-SNAPSHOT") { plugin =>
   *     plugin execution { ex =>
   *       //...initialization of the PluginExecution object...
   *     }
   *   }
   *   ...
   * </pre></blockquote></p>
   **/
  def apply(body: (Plugin) => Unit): Plugin = {
    body(this)
    this
  }
  
  def groupId: String = getGroupId
  def groupId_=(s: String) = setGroupId(s)
  
  def artifactId: String = getArtifactId
  def artifactId_=(s: String) = setArtifactId(s)
  
  def version: String = getVersion
  def version_=(s: String) = setVersion(s)
  
  def extensions: String = getExtensions
  def extensions_=(s: String) = setExtensions(s)
  def extensions_=(b: Boolean) = setExtensions(b)
  
  def executions = (getExecutions: Buffer[ApachePluginExecution])
  def execution(body: (PluginExecution) => Unit): PluginExecution = {
    val e = new PluginExecution
    body(e)
    addExecution(e)
    e
  }
  
  def dependencies = (getDependencies: Buffer[ApacheDependency])
  def dependency(body: (Dependency) => Unit): Dependency = {
    val d = new Dependency
    body(d)
    addDependency(d)
    d
  }
}

class PluginExecution extends ApachePluginExecution with ConfigurationContainerProps {
  def id: String = getId
  def id_=(s: String) = setId(id)
  
  def phase: String = getPhase
  def phase_=(s: String) = setPhase(s)
  
  def priority: Int = getPriority
  def priority_=(i: Int) = setPriority(i)
  
  def goals = (getGoals: Buffer[String])
}


trait ConfigurationContainerProps {
  self: ApacheConfigurationContainer =>
  
  def inherited: String = getInherited
  def inherited_=(s: String) = setInherited(s)
  def inherited_=(b: Boolean) = setInherited(b)
  
  def configuration: java.lang.Object = getConfiguration
  /**
   * Statically, base type defines this is an Object, but semantically it
   * is supposed to be a W3C DOM Element node. So this mutator method
   * adds a little type safety by requiring use of a W3C DOM Element.
   **/
  def configuration_=(elem: Xpp3Dom):Unit = setConfiguration(elem)
  def configuration_=(element: scala.xml.Elem): Unit = configuration_=(element: Xpp3Dom)
}

class PluginManagement extends ApachePluginManagement with PluginContainerProps {
  //nothing additional added to the inherited members.
}

class Site extends ApacheSite {
  def id: String = getId
  def id_=(s: String) = setId(s)
  
  def name: String = getName
  def name_=(s: String) = setName(s)
  
  def url: String = getUrl
  def url_=(s: String) = setUrl(s)
  
  def apply(body: (Site) => Unit): Site = {
    body(this)
    this
  }
}

// Funny this doesn't inherit from ConfigurationContainer. Seems like an
// oversight in the Apache Maven model codebase.
class ReportPlugin extends ApacheReportPlugin with WithCoords[ReportPlugin] {
  def groupId: String = getGroupId
  def groupId_=(s: String) = setGroupId(s)
  
  def artifactId: String = getArtifactId
  def artifactId_=(s: String) = setArtifactId(s)
  
  def version: String = getVersion
  def version_=(s: String) = setVersion(s)
  
  def inherited: Boolean = isInherited
  def inherited_=(b: Boolean) = setInherited(b)
  
  def reportSets = (getReportSets: Buffer[ApacheReportSet])
  def reportSet(body: (ReportSet) => Unit): ReportSet = {
    val s = new ReportSet
    body(s)
    addReportSet(s)
    s
  }

  def configuration: java.lang.Object = getConfiguration
  /**
   * Statically, base type defines this is an Object, but semantically it
   * is supposed to be a W3C DOM Element node. So this mutator method
   * adds a little type safety by requiring use of a W3C DOM Element.
   **/
  def configuration_=(elem: Xpp3Dom): Unit = setConfiguration(elem)
  def configuration_=(element: scala.xml.Elem): Unit = configuration_=(element: Xpp3Dom)  
  
  def apply(body: (ReportPlugin) => Unit): ReportPlugin = {
    body(this)
    this
  }
}

// Funny this doesn't inherit from ConfigurationContainer. Seems like an
// oversight in the Apache Maven model codebase.
class ReportSet extends ApacheReportSet {
  def id: String = getId
  def id_=(s: String) = setId(s)
  
  def reports = (getReports: Buffer[String])
  
  def configuration: java.lang.Object = getConfiguration
  /**
   * Statically, base type defines this is an Object, but semantically it
   * is supposed to be a W3C DOM Element node. So this mutator method
   * adds a little type safety by requiring use of a W3C DOM Element.
   **/
  def configuration_=(elem: Xpp3Dom): Unit = setConfiguration(elem)
  def configuration_=(element: scala.xml.Elem): Unit = configuration_=(element: Xpp3Dom)
}

class Organization extends ApacheOrganization {
  def name: String = getName
  def name_=(s: String) = setName(s)
  
  def url: String = getUrl
  def url_=(s: String) = setUrl(s)
}
