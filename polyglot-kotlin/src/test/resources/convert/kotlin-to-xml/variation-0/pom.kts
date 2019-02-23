import org.apache.maven.model.*
import org.codehaus.plexus.util.xml.Xpp3DomBuilder

Model().apply {

    modelVersion = "4.0.0"
    modelEncoding = "UTF-8"

    artifactId = "polyglot-demo"
    groupId = "com.example"
    version = "${'$'}{revision}"
    packaging = "jar"

    name = "Polyglot :: Kotlin :: Demo"
    description = "A demo of Maven Polyglot with Kotlin"

    licenses = listOf(License().apply {
        name = "The Eclipse Public License, Version 1.0"
        url = "http://www.eclipse.org/legal/epl-v10.html"
        distribution = "repo"
    })

    parent = Parent().apply {
        groupId = "org.springframework.boot"
        artifactId = "spring-boot-starter-parent"
        version = "2.1.0.RELEASE"
        relativePath = "../../pom.xml"
    }

    properties = org.sonatype.maven.polyglot.kotlin.util.Properties().apply {
        putAll(listOf(
            "java.version" to "11",
            "revision" to "${'$'}{release.version}",
            "release.version" to "1.0.0-SNAPSHOT",
            "kotlin.version" to "1.3.21",
            "spring-boot-admin.version" to "2.1.1",
            "project.build.sourceEncoding" to "UTF-8",
            "project.reporting.outputEncoding" to "UTF-8"
        ))
    }

    dependencyManagement = DependencyManagement().apply {
        dependencies = listOf(
            Dependency().apply {
                groupId = "org.jetbrains.kotlin"
                artifactId = "kotlin-bom"
                version = "${'$'}{kotlin.version}"
                scope = "import"
                type = "pom"
            },
            Dependency().apply {
                groupId = "org.springframework.boot"
                artifactId = "spring-boot-dependencies"
                version = "${'$'}{spring-boot.version}"
                scope = "import"
                type = "pom"
            }
        )
    }

    dependencies = listOf(
        Dependency().apply {
            groupId = "org.springframework.boot"
            artifactId = "spring-boot-starter-actuator"
        },
        Dependency().apply {
            groupId = "org.springframework.boot"
            artifactId = "spring-boot-starter-cache"
        },
        Dependency().apply {
            groupId = "org.springframework.boot"
            artifactId = "spring-boot-starter-hateoas"
        },
        Dependency().apply {
            groupId = "org.springframework.boot"
            artifactId = "spring-boot-starter-oauth2-client"
        },
        Dependency().apply {
            groupId = "org.springframework.boot"
            artifactId = "spring-boot-starter-security"
        },
        Dependency().apply {
            groupId = "org.springframework.boot"
            artifactId = "spring-boot-starter-validation"
        },
        Dependency().apply {
            groupId = "org.springframework.boot"
            artifactId = "spring-boot-starter-web"
        },
        Dependency().apply {
            groupId = "org.springframework.boot"
            artifactId = "spring-boot-configuration-processor"
            optional = "true"
        },
        Dependency().apply {
            groupId = "org.springframework.boot"
            artifactId = "spring-boot-devtools"
            optional = "true"
        },

        Dependency().apply {
            groupId = "de.codecentric"
            artifactId = "spring-boot-admin-starter-client"
            version = "\${spring-boot-admin.version}"
            scope = "compile"
        },
        Dependency().apply {
            groupId = "de.codecentric"
            artifactId = "spring-boot-admin-starter-server"
            version = "\${spring-boot-admin.version}"
            scope = "compile"
        },

        Dependency().apply {
            groupId = "org.springframework.boot"
            artifactId = "spring-boot-starter-test"
            scope = "test"
            exclusions = listOf(Exclusion().apply {
                groupId = "junit"
                artifactId = "junit"
            })
        },
        Dependency().apply {
            groupId = "org.springframework.security"
            artifactId = "spring-security-test"
            scope = "test"
        },
        Dependency().apply {
            groupId = "org.junit.jupiter"
            artifactId = "junit-jupiter-engine"
            scope = "test"
        },
        Dependency().apply {
            groupId = "org.jetbrains.kotlin"
            artifactId = "kotlin-test-junit"
            scope = "test"
            exclusions = listOf(Exclusion().apply {
                groupId = "junit"
                artifactId = "junit"
            })
        }
    )

    build = Build().apply {
        finalName = "${'$'}{project.artifactId}"
        defaultGoal = "clean verify"
        extensions = listOf(Extension().apply {
            groupId = "com.example.extensions"
            artifactId = "custom-application-lifecycle"
        })
        pluginManagement = PluginManagement().apply {
            plugins = listOf(
                Plugin().apply {
                    groupId = "org.apache.maven.plugins"
                    artifactId = "maven-enforcer-plugin"
                    executions = listOf(
                        PluginExecution().apply {
                            id = "enforce-revision-is-set"
                            goals = listOf("enforce")
                            configuration = Xpp3DomBuilder.build("""
                                <configuration>
                                    <rules>
                                        <requireMavenVersion>
                                            <version>[3.5,)</version>
                                        </requireMavenVersion>
                                        <requireJavaVersion>
                                            <version>[1.8,9.0)</version>
                                        </requireJavaVersion>
                                    </rules>
                                </configuration>
                                """.reader()
                            )
                        }
                    )
                },
                Plugin().apply {
                    groupId = "org.apache.maven.plugins"
                    artifactId = "maven-failsafe-plugin"
                    version = "${'$'}{maven-failsafe-plugin.version}"
                    configuration = Xpp3DomBuilder.build("""
                        <configuration>
                            <testFailureIgnore>true</testFailureIgnore>
                        </configuration>
                        """.reader()
                    )
                    executions = listOf(PluginExecution().apply {
                        id = "default"
                        phase = "none"
                    })
                },
                Plugin().apply {
                    groupId = "org.jetbrains.kotlin"
                    artifactId = "kotlin-maven-plugin"
                    version = "${'$'}{kotlin.version}"
                    dependencies = listOf(
                        Dependency().apply {
                            groupId = "org.jetbrains.kotlin"
                            artifactId = "kotlin-maven-allopen"
                            version = "${'$'}{kotlin.version}"
                        },
                        Dependency().apply {
                            groupId = "org.jetbrains.kotlin"
                            artifactId = "kotlin-maven-noarg"
                            version = "${'$'}{kotlin.version}"
                        }
                    )
                    executions = listOf(
                        PluginExecution().apply {
                            id = "compile"
                            phase = "compile"
                            goals = listOf("compile")
                        },
                        PluginExecution().apply {
                            id = "test-compile"
                            phase = "test-compile"
                            goals = listOf("test-compile")
                        }
                    )
                    configuration = Xpp3DomBuilder.build("""
                        <configuration>
                            <jvmTarget>1.8</jvmTarget>
                            <javaParameters>true</javaParameters>
                            <compilerPlugins>
                                <plugin>spring</plugin>
                                <plugin>jpa</plugin>
                            </compilerPlugins>
                        </configuration>
                        """.reader()
                    )
                },
                Plugin().apply {
                    groupId = "org.apache.maven.plugins"
                    artifactId = "maven-compiler-plugin"
                    executions = listOf(
                        PluginExecution().apply {
                            id = "default-compile"
                            phase = "none"
                        },
                        PluginExecution().apply {
                            id = "default-testCompile"
                            phase = "none"
                        },
                        PluginExecution().apply {
                            id = "java-compile"
                            phase = "compile"
                            goals = listOf("compile")
                        },
                        PluginExecution().apply {
                            id = "java-test-compile"
                            phase = "test-compile"
                            goals = listOf("testCompile")
                        }
                    )
                }
            )
        }
        plugins = listOf(
            Plugin().apply {
                groupId = "org.apache.maven.plugins"
                artifactId = "maven-enforcer-plugin"
            },
            Plugin().apply {
                groupId = "org.springframework.boot"
                artifactId = "spring-boot-maven-plugin"
            },
            Plugin().apply {
                groupId = "org.jetbrains.kotlin"
                artifactId = "kotlin-maven-plugin"
            },
            Plugin().apply {
                groupId = "org.apache.maven.plugins"
                artifactId = "maven-compiler-plugin"
            }
        )
    }

    profiles = listOf(
        Profile().apply {
            id = "release"
            build = Build().apply {
                pluginManagement = PluginManagement().apply {
                    plugins = listOf(
                        Plugin().apply {
                            groupId = "org.apache.maven.plugins"
                            artifactId = "maven-enforcer-plugin"
                            executions = listOf(
                                PluginExecution().apply {
                                    id = "enforce-revision-is-set"
                                    goals = listOf("enforce")
                                    configuration = Xpp3DomBuilder.build("""
                                        <configuration>
                                            <rules>
                                                <requireProperty>
                                                    <property>release.version</property>
                                                    <regex>v\d{4}\.\d{2}\.\d{2}</regex>
                                                </requireProperty>
                                            </rules>
                                        </configuration>
                                        """.reader()
                                    )
                                }
                            )
                        }
                    )
                }
            }
        },
        Profile().apply {
            id = "code-coverage"
            build = Build().apply {
                plugins = listOf(
                    Plugin().apply {
                        groupId = "org.jacoco"
                        artifactId = "jacoco-maven-plugin"
                        version = "0.8.2"
                        executions = listOf(
                            PluginExecution().apply {
                                id = "prepare-agent"
                                goals = listOf("prepare-agent")
                            },
                            PluginExecution().apply {
                                id = "prepare-agent-integration"
                                goals = listOf("prepare-agent-integration")
                            },
                            PluginExecution().apply {
                                id = "reports"
                                goals = listOf("report", "report-integration")
                            }
                        )
                    }
                )
            }
        }
    )

    repositories = listOf(
        Repository().apply {
            id = "mvn-main-repo"
            name = "Demo Main Repository"
            url = "https://repo.example.com/mvn-main-repo"
            releases = RepositoryPolicy().apply {
                checksumPolicy = "fail"
            }
            snapshots = RepositoryPolicy().apply {
                checksumPolicy = "warn"
            }
        }
    )

    pluginRepositories = listOf(
        Repository().apply {
            id = "mvn-plugin-repo"
            name = "Demo Plugin Repository"
            url = "https://repo.example.com/mvn-plugin-repo"
            releases = RepositoryPolicy().apply {
                checksumPolicy = "warn"
            }
            snapshots = RepositoryPolicy().apply {
                checksumPolicy = "ignore"
            }
        }
    )
}
