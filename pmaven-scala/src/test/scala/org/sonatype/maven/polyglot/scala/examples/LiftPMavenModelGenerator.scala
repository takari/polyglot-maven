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

// This is a copy of the Scala-ized Lift 1.1-SNAPSHOT POM, placed
// in a compilable class.

import org.sonatype.maven.polyglot.scala.model._

object LiftPMavenModelGenerator {
  def generateModel: Model = {

/**********
 ** Tranlsation of the Lift Web 1.1-SNAPSHOT POM to Scala PMaven
 **********/

//
// Copyright 2006-2009 WorldWide Conferencing, LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

project { p =>

  p.modelVersion = "4.0.0"

  /**********
  Shared parent. Define the settings common to all projects at Lift.

  This includes a standard release profile that all projects can use.
  If the profile is not appropriate for your project, define your own release profile
  and change the release plugin configuration parameter <arguments> </arguments> to
  activate your profile instead of the "release" profile.

  Dependencies and plugins are also pre-configured with standard settings,
  these may be overridden by individual projects as well.
   **********/

  /**********
    ~ The Basics
   **********/
  p.groupId = "net.liftweb"
  p.artifactId = "lift"
  p.version = "1.1-SNAPSHOT"
  p.packaging = "pom"

  p.properties += (
    /* Common plugin settings */
    "project.build.sourceEncoding" -> "UTF-8",
    "maven.compiler.source" -> "1.5",
    "maven.compiler.target" -> "${maven.compiler.source}",
    
    /* Necessary until we move to maven-compiler-plugin-2.1 (MCOMPILER-70) */
    "maven.compiler.encoding" -> "${project.build.sourceEncoding}",
    "project.reporting.outputEncoding" -> "${project.build.sourceEncoding}",
    
    /* TODO: for lift-build_date.
         http://maven.apache.org/guides/introduction/introduction-to-the-pom.html#Available_Variables */
    /* <maven.build.timestamp.format/> */

    /* "scala.version" -> "[2.7.5,2.7.6),(2.7.6,2.7.7]", */
    "scala.version" -> "2.7.7",
    "slf4j.version" -> "[1.5.6,)",

    "vscaladoc.links.liftweb.baseurl" -> "http://scala-tools.org/mvnsites-snapshots/liftweb"
  )

  /**********
    ~ More Project Information
   **********/
  p.name = "Lift Web Framework"
  p.description = """
    Lift is an expressive and elegant framework for writing web applications.
    Lift stresses the importance of security, maintainability, scalability
    and performance while allowing for high levels of developer productivity.
    Lift is a Scala web framework.
  """
  p.url = "http://dev.liftweb.net"
  p.inceptionYear = "2006"
  
  p.license { lic =>
    lic.name = "Apache License, Version 2.0"
    lic.url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
    lic.distribution = "repo"
    lic.comments = "Lift open source software is licensed under an Apache 2.0 license."
  }
  
  p.organization { org =>
    org.name  = "WorldWide Conferencing, LLC"
    org.url = "http://www.liftweb.net"
  }
  
  p.developer("dpp") { dev =>
    dev.name = "David Pollak"; dev.timezone = "-8";
    dev.email= "dpp [at] liftweb.net"; dev.roles += ("BDFL","Feeder of the Bears");
  }

  p.developer("Burak.Emir") { dev =>
    dev.name = "Burak Emir";
  }
  
  p.developer("philipp.schmidt") { dev =>
    dev.name = "philipp.schmidt";
  }
  
  p.developer("cwilkes") { dev =>
    dev.name = "cwilkes";
  }
  
  p.developer("julien.wetterwald") { dev =>
    dev.name = "julien.wetterwald";
  }
  
  p.developer("leppoc") { dev =>
    dev.name = "leppoc";
  }
  
  p.developer("stepan.koltsov") { dev =>
    dev.name = "stephan.koltsov";
  }
  
  p.developer("jorge.ortiz") { dev =>
    dev.name = "Jorge Ortiz"; dev.timezone = "-8";
    dev.email = "jorge [at] liftweb.net";
  }
  
  p.developer("stevej") { dev =>
    dev.name = "Steve Jenson";
  }
  
  p.developer("alex.boisvert") { dev =>
    dev.name = "Alex Boisvert";
  }
  
  p.developer("OctoberDan")
  
  p.developer("viktor.klang") { dev =>
    dev.name = "Viktor Klang a.k.a. Sevikkla";
    dev.timezone = "+1"
    dev.roles += ("Enhancement specialist", "Funny guy")
  }
  
  p.developer("david.bernard.31") { dev =>
    dev.name = "David Bernard"; dev.email = "dwayne [at] liftweb.net";
    dev.timezone = "+1"
    dev.roles += ("maven support")
  }
  
  p.developer("mstarzyk") { dev =>
    dev.name = "Maciek Starzyk";
  }

  p.developer("etorreborre") { dev =>
    dev.name = "Eric Torreborre"; dev.timezone = "+9";
  }
  
  p.developer("marius.danciu") { dev =>
    dev.name = "Marius Danciu"; dev.timezone = "+2";
  }
  
  p.developer("tyler.weir") { dev =>
    dev.name = "Tyler Weir"; dev.timezone = "-5";
  }
  
  p.developer("timperrett") { dev =>
    dev.name = "Tim Perrett"; dev.email = "hello [at] timperrett.com";
    dev.timezone = "0"; dev.roles += ("Installation and Deployment", "Advanced Localization");
  }
  
  p.developer("dchenbecker") { dev =>
    dev.name = "Derek Chen-Becker"; dev.email = "java [at] chen-becker.org";
    dev.timezone = "-7";
  }
  
  p.developer("jboner") { dev =>
    dev.name = "Jonas Bon&#233;r"; dev.email = "jonas [at] jonasboner [dot] com";
    dev.timezone = "+1";
  }

  p.developer("heiko.seeberger") { dev =>
    dev.name = "Heiko Seeberger"; dev.email = "heiko [dot] seeberger [at] googlemail [dot] com";
    dev.timezone = "+1"; dev.roles += ("OSGi expert and Scala enthusiast");
  }
  
  p.developer("indrjitr") { dev =>
    dev.name = "Indrajit Raychaudhuri"; dev.email = "irc [at] indrajit [dot] com";
    dev.timezone = "+5.5";
  }

  /**********
    ~ Environment Settings
   **********/
  p.issueManagement { im =>
    im.system = "github"; im.url = "http://github.com/dpp/liftweb/issues/";
  }
  
  p.ciManagement { cim =>
    cim.system = "hudson"; cim.url = "http://hudson.scala-tools.org/job/Lift/";
    /* TODO: Configure
    cim.notifiers += List(???) */
  }

  p.mailingList { ml =>
    ml.name = "User and Developer Discussion List"
    ml.archive = "http://groups.google.com/group/liftweb"
    ml.post = "liftweb@googlegroups.com"
    ml.subscribe = "liftweb+subscribe@googlegroups.com"
    ml.unsubscribe = "liftweb+unsubscribe@googlegroups.com"
  }
  
  p.mailingList { ml =>
    ml.name = "Committer Discussion List"
    ml.archive = "http://groups.google.com/group/lift-committers"
    /* ml.post = "lift-committers@googlegroups.com" */
    /* ml.subscribe = "lift-committers+subscribe@googlegroups.com" */
    /* ml.unsubscribe = "lift-committers+unsubscribe@googlegroups.com" */
  }
  
  p.mailingList { ml =>
    ml.name = "Announcement List"
    ml.archive = "http://groups.google.com/group/lift-announce"
    /* ml.post = "lift-announce@googlegroups.com" */
    ml.subscribe = "lift-announce+subscribe@googlegroups.com"
    ml.unsubscribe = "lift-announce+unsubscribe@googlegroups.com"
  }
  
  p.scm { scm =>
    scm.connection= "scm:git:git://github.com/dpp/liftweb.git"
    scm.developerConnection="scm:git:git@github.com:dpp/liftweb.git"
    scm.url="http://github.com/dpp/liftweb/tree/master"
  }
  
  p.repository { repo =>
    repo.id = "scala-tools.release"
    repo.name = "Scala-Tools Release Repository"
    repo.url = "http://scala-tools.org/repo-releases"
  }
  
  p.repository { repo =>
    repo.id = "scala-tools.snapshot"
    repo.name = "Scala-Tools Snapshot Repository"
    repo.url = "http://scala-tools.org/repo-snapshots"
  }
  
  p.pluginRepository { repo =>
    repo.id = "scala-tools.release"
    repo.name = "Scala-Tools Release Repository"
    repo.url = "http://scala-tools.org/repo-releases"
  }
  
  p.pluginRepository { repo =>
    repo.id = "scala-tools.snapshot"
    repo.name = "Scala-Tools Snapshot Repository"
    repo.url = "http://scala-tools.org/repo-snapshots"
  }
  
  p.distributionManagement { dm =>
    dm.snapshotRepository { repo =>
      repo.id = "nexus.scala-tools.org"
      repo.name = "Scala-Tools Snapshot Distribution Repository"
      repo.url = "http://nexus.scala-tools.org/content/repositories/snapshots"
      repo.uniqueVersion = false
    }
  }
  
  /* Site omitted, each project/profile must provide their own */

  /**********
    ~ Dependency Settings
   **********/
  p.dependencyManagement { dm =>
    /**********
      ~ Compile scope: available in all classpath, transitive
     **********/
      dm.dependency("org.scala-lang:scala-library:${scala.version}")
      dm.dependency("org.scala-libs:scalajpa:1.1")
      dm.dependency("javax.mail:mail:[1.4,1.4.3)")
      dm.dependency("commons-codec:commons-codec:1.3")
                     /* version [1.3,1.4]  */
      dm.dependency("commons-collections:commons-collections:3.2.1")
      dm.dependency("commons-fileupload:commons-fileupload:1.2.1")
      dm.dependency("commons-httpclient:commons-httpclient:3.1")
      dm.dependency("log4j:log4j:1.2.14")
                     /* version [1.2.14,1.2.15] */
      dm.dependency("org.slf4j:slf4j-api:${slf4j.version}")
                     /* option true */
                     
    /**********
      ~ Provided scope: provided by container, available only in compile and test classpath, non-transitive
     **********/
      dm.dependency("javax.servlet:servlet-api:2.5") { _.scope = "provided" }
      dm.dependency("javax.persistence:persistence-api:1.0") { _.scope = "provided"; }
      dm.dependency("org.apache.felix:org.osgi.compendium:1.2.0") { _.scope = "provided"; }
      dm.dependency("org.scalamodules:scalamodules-core:1.1-SNAPSHOT") { _.scope= "provided"; }
      dm.dependency("org.ops4j.pax.web:pax-web-bundle:0.6.0") { _.scope = "provided"; }
      dm.dependency("org.ops4j.pax.swissbox:pax-swissbox-extender:0.2.0") { _.scope = "provided"; }
      
    /**********
      ~ Runtime scope: provided in runtime, available only in runtime and test classpath, not compile classpath
     **********/
      /* <dependency/> */
      
    /**********
      ~ Test scope: available only in test classpath
     **********/
      dm.dependency("org.scala-tools.testing:specs:1.6.1") { _.scope = "test"; }
                     /* version [1.4.4,1.6.0] */
      dm.dependency("org.scala-tools.testing:scalacheck:1.6") { _.scope = "test"; }
      dm.dependency("junit:junit:4.5") { _.scope = "test"; }
                     /* TODO: push to 4.7 */
      dm.dependency("org.slf4j:slf4j-simple:${slf4j.version}") { _.scope = "test"; }
      dm.dependency("org.mortbay.jetty:jetty:[6.1.6,7.0)") { _.scope = "test"; }
      dm.dependency("net.sourceforge.jwebunit:jwebunit-htmlunit-plugin:2.2")
        { dep =>
          dep.scope = "test";
          dep.exclusion("javax.servlet:servlet-api")
        }
    }
    
  /**********
    ~ Modules Setting
   **********/   
  p.modules += (
    "lift-base", "lift-persistence", "lift-modules", "lift-archtypes", "lift-examples", "lift-core" )

  /**********
    ~ Build Settings
   **********/
  p.build { b =>
    b.extension { ex => ex coords "org.apache.maven.wagon:wagon-webdav:1.0-beta-2"; }
    
    b.pluginManagement { pm =>
      pm.plugin("org.apache.maven.plugins:maven-enforcer-plugin:1.0-beta-1")
         { pi =>
           pi.configuration =
               <configuration>
                 <rules>
                   <requireMavenVersion>
                     <version>[2.1.0,3.0)</version>
                   </requireMavenVersion>
                 </rules>
               </configuration>
           pi execution { ex =>
               ex.id = "default-enforce"
               ex.goals += ("enforce")
           }
         }
      
      pm.plugin("org.scala-tools:maven-scala-plugin:2.12.2")
          { pi =>
            pi.configuration =
                <configuration>
                  <jvmArgs>
                    <jvmArg>-Xmx1024m</jvmArg>
                  </jvmArgs>
                  <args>
                    <arg>-Xno-varargs-conversion</arg>
                  </args>
                </configuration>
            pi.execution { ex =>
                ex.id = "scala-compile"
                ex.phase = "process-resources"
                ex.goals += ("compile")
            }
            pi execution { ex =>
                ex.id = "scala-testCompile"
                ex.phase = "process-test-resources"
                ex.goals += ("testCompile")
            }
          }
      
      pm.plugin("org.apache.maven.plugins:maven-jar-plugin:2.2")
          { pi =>
            pi.configuration =
                <configuration>
                  <archive>
                    <manifest>
                      <addDefaultSpecificationEntries>true</addDefaultSpecificationEntries>
                      <addDefaultImplementationEntries>true</addDefaultImplementationEntries>
                    </manifest>
                    <manifestEntries>
                      <!-- TODO: Deprecate and use Default Implementation Entries (Implementation-Version) -->
                      <lift_version>{"${pom.version}"}</lift_version>
                      <lift_build_date>{"${maven.build.timestamp}"}</lift_build_date>
                    </manifestEntries>
                  </archive>
                </configuration>
          }
      
      pm.plugin("org.apache.maven.plugins:maven-source-plugin:2.1.1")
          { pi =>
            pi.execution { ex =>
                ex.goals += ("jar-no-fork")
            }
          }
      
      pm.plugin("org.apache.maven.plugins:maven-resources-plugin:2.4.1")
          { pi =>
            pi execution { ex =>
                ex.id = "default-copy-resources"
                ex.phase = "pre-site"
                ex.goals += ("copy-resources")
                ex.configuration =
                    <configuration>
                      <overwrite>true</overwrite>
                      <outputDirectory>{"${project.build.directory}"}</outputDirectory>
                      <resources>
                        <resource>
                          <directory>{"${project.basedir}/src"}</directory>
                          <includes>
                            <include>packageLinkDefs.properties</include>
                          </includes>
                          <filtering>true</filtering>
                        </resource>
                      </resources>
                    </configuration>
            }
          }

      pm.plugin("org.apache.maven.plugins:maven-site-plugin:2.0.1")
      
      pm.plugin("org.apache.maven.plugins:maven-changes-plugin:2.1")
          { pi =>
            pi.execution { ex =>
                ex.id = "default-changes-validate"
                ex.goals += ("changes-validate")
                ex.configuration = 
                    <configuration>
                      <failOnError>true</failOnError>
                    </configuration>
            }
          }

      pm.plugin("org.apache.felix:maven-bundle-plugin:2.0.1")
          { pi =>
            pi.extensions = "true"
            pi.configuration = 
                <configuration>
                  <instructions>
                    <Bundle-RequiredExecutionEnvironment>J2SE-1.5,JavaSE-1.6</Bundle-RequiredExecutionEnvironment>
                    <_versionpolicy>[$(@),$(version;=+;$(@)))</_versionpolicy>
                    <DynamicImport-Package>*</DynamicImport-Package>
                  </instructions>
                </configuration>
            pi.execution { ex =>
                ex.id = "default-bundle"
                ex.phase = "package"
                ex.goals += ("bundle")
            }
          }
      
      pm.plugin("net.sf.alchim:yuicompressor-maven-plugin:0.7.1")
          { pi =>
            pi execution { ex =>
                ex.goals += ("compress")
            }
          }
      
      pm.plugin("org.apache.rat:apache-rat-plugin:0.6")
          { pi =>
            pi.configuration = 
                <configuration>
                  <excludes>
                    <exclude>*.log</exclude>
                    <exclude>packageLinkDefs.properties</exclude>
                  </excludes>
                </configuration>
            pi execution { ex =>
                ex.phase = "verify"
                ex.goals += ("check")
            }
          }
      
      pm.plugin("org.apache.maven.plugins:maven-idea-plugin:2.2")
          { pi =>
            pi.configuration =
                <configuration>
                  <downloadSources>true</downloadSources>
                </configuration>
          }

      pm.plugin("org.apache.maven.plugins:maven-eclipse-plugin:2.7")
          { pi =>
            pi.configuration =
                <configuration>
                  <downloadSources>true</downloadSources>
                  <additionalProjectnatures>
                    <projectnature>ch.epfl.lamp.sdt.core.scalanature</projectnature>
                  </additionalProjectnatures>
                  <additionalBuildcommands>
                    <buildcommand>ch.epfl.lamp.sdt.core.scalabuilder</buildcommand>
                  </additionalBuildcommands>
                  <classpathContainers>
                    <classpathContainer>ch.epfl.lamp.sdt.launching.SCALA_CONTAINER</classpathContainer>
                    <classpathContainer>org.eclipse.jdt.launching.JRE_CONTAINER</classpathContainer>
                  </classpathContainers>
                </configuration>
          }
    }
    
    b.plugin("org.apache.maven.plugins:maven-enforcer-plugin")
    b.plugin("org.scala-tools:maven-scala-plugin")
    b.plugin("org.apache.maven.plugins:maven-source-plugin")
    b.plugin("org.apache.maven.plugins:maven-resources-plugin")
    b.plugin("org.apache.maven.plugins:maven-changes-plugin")
  }

  /**********
    ~ Reporting Settings
   **********/
  p.reporting { r =>
    r.excludeDefaults = "true"
    r.plugin("org.apache.maven.plugins:maven-project-info-reports-plugin:2.1.2")
        { pi =>
          pi.reportSet { rs =>
              rs.reports += (
                  "cim", "dependencies", "dependency-convergence",
                  "index", "issue-tracking", "license", "mailing-list",
                  "plugins", "project-team", "scm", "summary"
                  /* "dependency-management" */
                  /* "plugin-management" */
              )
          }
        }
        
    r.plugin("org.scala-tools:maven-scala-plugin:2.12.2")
        { pi =>
          pi.configuration =
            <configuration>
              <charset>{"${project.build.sourceEncoding}"}</charset>
              <!--<bottom>Copyright &#169; {inceptionYear}-{currentYear} {organizationName}. All Rights Reserved.</bottom>-->
              <vscaladocVersion>1.2-SNAPSHOT</vscaladocVersion>
              <jvmArgs>
                <jvmArg>-Xmx1024m</jvmArg>
                <jvmArg>-DpackageLinkDefs=file://{"${project.build.directory}"}/packageLinkDefs.properties</jvmArg>
              </jvmArgs>
              <!--FIXME: see that sxr plugin works -->
              <!--
              <compilerPlugins>
                <compilerPlugin>
                  <groupId>org.scala-tools.sxr</groupId>
                  <artifactId>sxr_{"${scala.version}"}</artifactId>
                  <version>0.2.3</version>
                 </compilerPlugin>
              </compilerPlugins>
              -->
            </configuration>
        }
        
    r.plugin("org.apache.maven.plugins:maven-javadoc-plugin:2.6.1")
        { pi =>
          pi.configuration =
            <configuration>
              <detectLinks>true</detectLinks>
              <linksource>true</linksource>
            </configuration>
          pi.reportSet { rs =>
              rs.reports += (
                  "javadoc", "test-javadoc"
                  /* "aggregate" */
              )
          }
        }

    r.plugin("org.apache.maven.plugins:maven-jxr-plugin:2.1")
        { pi =>
          pi.configuration =
            <configuration>
              <inputEncoding>{"${project.build.sourceEncoding}"}</inputEncoding>
              <outputEncoding>{"${project.build.sourceEncoding}"}</outputEncoding>
            </configuration>
        }
        
    r.plugin("org.apache.maven.plugins:maven-changes-plugin:2.1")
        { pi =>
          pi.inherited = false
          pi.configuration =
              <configuration>
                <issueLinkTemplatePerSystem>
                  <github>%URL%/%ISSUE%/find</github>
                </issueLinkTemplatePerSystem>
              </configuration>
          pi.reportSet { rs =>
              rs.reports += ("changes-report")
          }
        }
        
    r.plugin("org.apache.maven.plugins:maven-surefire-report-plugin:2.4.3")
        { pi =>
          pi.reportSet { rs => rs.reports += ("report-only") }
        }
  }

  /**********
    ~ Profile Settings
   **********/
   
  p.profile("detail")
      { prof =>
        prof.build { b =>
            b.pluginManagement
                { pm =>
                  pm.plugin("org.scala-tools:maven-scala-plugin")
                      { pi =>
                        pi.configuration =
                            <configuration>
                              <args>
                                <arg>-unchecked</arg>
                                <arg>-deprecation</arg>
                                <arg>-Xno-varargs-conversion</arg>
                              </args>
                            </configuration>
                      }
                }
        }
      }
      
  p.profile("release")
      { prof =>
        prof.properties += ("vscaladoc.links.liftweb.baseurl" -> "http://scala-tools.org/mvnsites/liftweb")
        prof.build { b =>
            b.plugin("org.apache.rat:apache-rat-plugin")
        }
        prof.distributionManagement { dm =>
            dm.repository("nexus.scala-tools.org") { repo =>
                repo.name = "Scala-Tools Release Distribution Repository"
                repo.url = "http://nexus.scala-tools.org/content/repositories/releases"
            }
            dm.site("scala-tools.dist.site") { site =>
                /* dav protocol isn't optimized for site-deploy (very long for api) */
                /* site.url = "dav:http://dav.scala-tools.org/mvnsites/liftweb" */
            }
        }
      }
      
  p.profile("hudson")
      { prof =>
        /*
        prof.repository("nexus-scala-tools.org") { repo =>
            repo.url = "http://nexus.scala-tools.org/content/repositories/releases"
        }
         */
        prof.distributionManagement { dm =>
            dm.snapshotRepository("nexus.scala-tools.org") { repo =>
                repo.url = "http://nexus.scala-tools.org/content/repositories/snapshots"
                repo.uniqueVersion = false
            }
            dm.site("nexus.scala-tools.org") { site =>
                site.url = "file:///home/scala-tools.org/www/mvnsites-snapshots/liftweb"
            }
        }
      }
}

  }
}