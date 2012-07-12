/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
project {
    modelVersion '4.0.0'
    parent {
        artifactId 'forge-parent'
        groupId 'org.sonatype.forge'
        version '6'
    }
    groupId 'org.sonatype.pmaven'
    artifactId 'pmaven'
    version '0.8-SNAPSHOT'
    packaging 'pom'
    name 'Polyglot Maven'
    url 'http://polyglot.sonatype.org/'
    licenses {
        license {
            name 'The Apache Software License, Version 2.0'
            url 'http://www.apache.org/licenses/LICENSE-2.0.txt'
            distribution 'repo'
        }
    }
    developers {
        developer {
            id 'jdillon'
            name 'Jason Dillon'
            email 'jason@planet57.com'
            roles {
                role 'Build Master'
                role 'Developer'
            }
        }
    }
    mailingLists {
        mailingList {
            name 'Development'
            subscribe 'polyglot-subscribe@maven.org'
            post 'polyglot@maven.org'
        }
    }
    modules {
        module 'pmaven-common'
        module 'pmaven-maven-plugin'
        module 'pmaven-groovy'
        module 'pmaven-yaml'
        module 'pmaven-clojure'
        module 'pmaven-jruby'
        module 'pmaven-scala'
        module 'pmaven-cli'
        module 'pmaven-commands'
    }
    scm {
        connection 'scm:git:git://github.com/sonatype/polyglot-maven.git'
        developerConnection 'scm:git:ssh://git@github.com/sonatype/polyglot-maven.git'
        url 'http://github.com/sonatype/polyglot-maven'
    }
    issueManagement {
        system 'JIRA'
        url 'https://issues.sonatype.org/browse/PMAVEN'
    }
    ciManagement {
        system 'Hudson'
        url 'https://grid.sonatype.org/ci/job/Polyglot-Maven'
    }
    distributionManagement {
        site {
            id '${forgeSiteId}'
            url '${forgeSiteUrl}'
        }
    }
    properties {
        forgeSiteId 'forge-sites'
        mavenVersion '3.0-beta-1'
        'project.build.sourceEncoding' 'UTF-8'
        forgeSiteUrl 'dav:http://repository.sonatype.org/content/sites/forge-sites/${project.artifactId}/${project.version}'
    }
    dependencyManagement {
        dependencies {
            dependency {
                groupId 'junit'
                artifactId 'junit'
                version '4.8.1'
            }
            dependency {
                groupId 'org.apache.maven'
                artifactId 'apache-maven'
                version '${mavenVersion}'
                type 'zip'
                classifier 'bin'
            }
            dependency {
                groupId 'org.apache.maven'
                artifactId 'maven-model-builder'
                version '${mavenVersion}'
            }
            dependency {
                groupId 'org.apache.maven'
                artifactId 'maven-embedder'
                version '${mavenVersion}'
            }
            dependency {
                groupId 'org.apache.maven'
                artifactId 'maven-plugin-api'
                version '${mavenVersion}'
            }
            dependency {
                groupId 'org.codehaus.groovy'
                artifactId 'groovy'
                version '1.7.3'
                exclusions {
                    exclusion {
                        artifactId 'jline'
                        groupId 'jline'
                    }
                    exclusion {
                        artifactId 'junit'
                        groupId 'junit'
                    }
                    exclusion {
                        artifactId 'ant'
                        groupId 'org.apache.ant'
                    }
                    exclusion {
                        artifactId 'ant-launcher'
                        groupId 'org.apache.ant'
                    }
                }
            }
            dependency {
                groupId 'com.google.inject'
                artifactId 'guice'
                version '2.0'
            }
            dependency {
                groupId 'org.sonatype.maven.shell'
                artifactId 'mvnsh-maven'
                version '0.10'
            }
            dependency {
                groupId 'org.sonatype.gshell'
                artifactId 'gshell-core'
                version '2.5'
                classifier 'tests'
            }
            dependency {
                groupId 'org.sonatype.pmaven'
                artifactId 'pmaven-common'
                version '0.8-SNAPSHOT'
            }
            dependency {
                groupId 'org.sonatype.pmaven'
                artifactId 'pmaven-cli'
                version '0.8-SNAPSHOT'
            }
            dependency {
                groupId 'org.sonatype.pmaven'
                artifactId 'pmaven-maven-plugin'
                version '0.8-SNAPSHOT'
            }
            dependency {
                groupId 'org.sonatype.pmaven'
                artifactId 'pmaven-groovy'
                version '0.8-SNAPSHOT'
            }
            dependency {
                groupId 'org.sonatype.pmaven'
                artifactId 'pmaven-yaml'
                version '0.8-SNAPSHOT'
            }
            dependency {
                groupId 'org.sonatype.pmaven'
                artifactId 'pmaven-jruby'
                version '0.8-SNAPSHOT'
            }
            dependency {
                groupId 'org.sonatype.pmaven'
                artifactId 'pmaven-commands'
                version '0.8-SNAPSHOT'
            }
            dependency {
                groupId 'org.sonatype.pmaven'
                artifactId 'pmaven-clojure'
                version '0.8-SNAPSHOT'
            }
            dependency {
                groupId 'org.sonatype.pmaven'
                artifactId 'pmaven-scala'
                version '0.8-SNAPSHOT'
            }
        }
    }
    dependencies {
        dependency {
            groupId 'junit'
            artifactId 'junit'
            scope 'test'
        }
        dependency {
            groupId 'org.codehaus.groovy'
            artifactId 'groovy'
            scope 'test'
        }
    }
    repositories {
        repository {
            id 'sonatype-public-grid'
            url 'http://repository.sonatype.org/content/groups/sonatype-public-grid'
        }
    }
    pluginRepositories {
        pluginRepository {
            id 'sonatype-public-grid'
            url 'http://repository.sonatype.org/content/groups/sonatype-public-grid'
        }
    }
    build {
        defaultGoal 'install'
        pluginManagement {
            plugins {
                plugin {
                    artifactId 'maven-site-plugin'
                    version '2.1'
                }
            }
        }
        plugins {
            plugin {
                artifactId 'maven-surefire-plugin'
                version '2.5'
                configuration {
                    redirectTestOutputToFile 'true'
                    forkMode 'once'
                    argLine '-ea'
                    failIfNoTests 'false'
                    workingDirectory '${project.build.directory}'
                    excludes {
                        exclude '**/Abstract*.java'
                        exclude '**/Test*.java'
                    }
                    includes {
                        include '**/*Test.java'
                    }
                }
            }
            plugin {
                artifactId 'maven-compiler-plugin'
                version '2.3.1'
                configuration {
                    source '1.5'
                    target '1.5'
                }
            }
            plugin {
                groupId 'org.codehaus.gmaven'
                artifactId 'gmaven-plugin'
                version '1.2'
                executions {
                    execution {
                        goals {
                            goal 'generateStubs'
                            goal 'compile'
                            goal 'generateTestStubs'
                            goal 'testCompile'
                        }
                    }
                }
                configuration {
                    providerSelection '1.7'
                }
            }
            plugin {
                groupId 'org.codehaus.plexus'
                artifactId 'plexus-component-metadata'
                version '1.5.4'
                executions {
                    execution {
                        goals {
                            goal 'generate-metadata'
                            goal 'generate-test-metadata'
                        }
                    }
                }
            }
            plugin {
                artifactId 'maven-release-plugin'
                version '2.0'
                configuration {
                    useReleaseProfile 'false'
                    goals 'deploy'
                    arguments '-B -Prelease'
                    autoVersionSubmodules 'true'
                }
            }
            plugin {
                artifactId 'maven-scm-plugin'
                version '1.3'
            }
        }
    }
    reporting {
        plugins {
            plugin {
                artifactId 'maven-javadoc-plugin'
                version '2.7'
                configuration {
                    source '1.5'
                    encoding '${project.build.sourceEncoding}'
                }
            }
            plugin {
                groupId 'org.codehaus.mojo'
                artifactId 'cobertura-maven-plugin'
                version '2.4'
                inherited 'false'
            }
        }
    }
}
