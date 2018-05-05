package org.sonatype.maven.polyglot.kotlin

import org.apache.maven.model.*
import org.apache.maven.shared.utils.xml.Xpp3Dom
import org.junit.Test
import java.io.StringWriter
import java.util.*
import kotlin.test.assertEquals

class KotlinModelWriterTest {
    val writer = KotlinModelWriter()

    @Test fun writeProjectRequiredDetails() {
        //GIVEN
        val model = Model().apply {
            groupId = "io.takari.polyglot"
            artifactId = "polyglot-kotlin"
            version = "0.2.2-SNAPSHOT"
        }
        val result = StringWriter()

        //WHEN
        writer.write(result, mutableMapOf(), model)

        //THEN
        assertEquals("""
            project {
                groupId = "io.takari.polyglot"
                artifactId = "polyglot-kotlin"
                packaging = jar
            }
        """.trimIndent(), result.toString())
    }

    @Test fun writeProjectDetails() {
        //GIVEN
        val model = Model().apply {
            name = "Polyglot :: Kotlin"
            groupId = "io.takari.polyglot"
            artifactId = "polyglot-kotlin"
            version = "0.2.2-SNAPSHOT"
        }
        val result = StringWriter()

        //WHEN
        writer.write(result, mutableMapOf(), model)

        //THEN
        assertEquals("""
            project {
                name = "Polyglot :: Kotlin"
                groupId = "io.takari.polyglot"
                artifactId = "polyglot-kotlin"
                packaging = jar
            }
        """.trimIndent(), result.toString())
    }

    @Test fun writeProjectParent() {
        //GIVEN
        val model = Model().apply {
            name = "Polyglot :: Kotlin"
            artifactId = "polyglot-kotlin"
            parent = Parent().apply {
                groupId = "io.takari.polyglot"
                artifactId = "polyglot"
                version = "0.2.2-SNAPSHOT"
            }
        }
        val result = StringWriter()

        //WHEN
        writer.write(result, mutableMapOf(), model)

        //THEN
        assertEquals("""
            project {
                name = "Polyglot :: Kotlin"
                parent = "io.takari.polyglot:polyglot:0.2.2-SNAPSHOT"
                artifactId = "polyglot-kotlin"
                packaging = jar
            }
        """.trimIndent(), result.toString())
    }

    @Test fun writeProjectProperties() {
        //GIVEN
        val model = Model().apply {
            name = "Polyglot :: Kotlin"
            groupId = "io.takari.polyglot"
            artifactId = "polyglot-kotlin"
            version = "0.2.2-SNAPSHOT"

            properties = Properties().apply {
                put("project.build.sourceEncoding", "UTF-8")
                put("kotlin.version", "1.2.30")
            }
        }
        val result = StringWriter()

        //WHEN
        writer.write(result, mutableMapOf(), model)

        //THEN
        assertEquals("""
            project {
                name = "Polyglot :: Kotlin"
                groupId = "io.takari.polyglot"
                artifactId = "polyglot-kotlin"
                packaging = jar
                properties {
                    "project.build.sourceEncoding" assign "UTF-8"
                    "kotlin.version" assign "1.2.30"
                }
            }
        """.trimIndent(), result.toString())
    }

    @Test fun writeProjectDependencies() {
        //GIVEN
        val model = Model().apply {
            groupId = "io.takari.polyglot"
            artifactId = "polyglot-kotlin"
            version = "0.2.2-SNAPSHOT"

            dependencies = listOf(Dependency().apply {
                scope = "import"
                groupId = "org.apache.maven"
                artifactId = "maven"
                version = "3.0"
            },
            Dependency().apply {
                scope = "compile"
                groupId = "io.takari.polyglot"
                artifactId = "polyglot-common"
                version = "0.2.2-SNAPSHOT"
            },
            Dependency().apply {
                scope = "test"
                groupId = "junit"
                artifactId = "junit"
                version = "4.11"

                type = "ear"
                classifier = "jdk1.4"
                exclusions = listOf(Exclusion().apply {
                    groupId = "org.hamcrest"
                    artifactId = "hamcrest-core"
                })
            },
            Dependency().apply {
                scope = "test"
                groupId = "org.jetbrains.kotlin"
                artifactId = "kotlin-test-junit"
                version = "1.1.61"

                optional = "true"
                exclusions = listOf(
                        Exclusion().apply {
                            groupId = "org.hamcrest"
                            artifactId = "hamcrest-core"
                        },
                        Exclusion().apply {
                            groupId = "junit"
                            artifactId = "junit"
                        }
                )
            }
            )
        }
        val result = StringWriter()

        //WHEN
        writer.write(result, mutableMapOf(), model)

        //THEN
        assertEquals("""
            project {
                groupId = "io.takari.polyglot"
                artifactId = "polyglot-kotlin"
                packaging = jar
                dependencies {
                    import("org.apache.maven:maven:3.0")
                    compile("io.takari.polyglot:polyglot-common:0.2.2-SNAPSHOT")
                    test(
                        "junit:junit:4.11" type ear classifier jdk1.4 exclusions "org.hamcrest:hamcrest-core",
                        "org.jetbrains.kotlin:kotlin-test-junit:1.1.61" optional true exclusions arrayOf("org.hamcrest:hamcrest-core", "junit:junit")
                    )
                }
            }
        """.trimIndent(), result.toString())
    }

    @Test fun writeProjectBuildDetails() {
        val model = Model().apply {
            groupId = "io.takari.polyglot"
            artifactId = "polyglot-kotlin"
            version = "0.2.2-SNAPSHOT"

            build = Build().apply {
                sourceDirectory = "src/main/kotlin"
                testSourceDirectory = "src/test/kotlin"
                finalName = "polyglot-kotlin"
                scriptSourceDirectory = "src/main/scripts"
                outputDirectory = "target/classes"
                testOutputDirectory = "target/test-classes"
                directory = "target"

                filters = listOf("*.java", "*.scala")
            }
        }
        val result = StringWriter()

        //WHEN
        writer.write(result, mutableMapOf(), model)

        //THEN
        assertEquals("""
            project {
                groupId = "io.takari.polyglot"
                artifactId = "polyglot-kotlin"
                packaging = jar
                build {
                    sourceDirectory = "src/main/kotlin"
                    testSourceDirectory = "src/test/kotlin"
                    finalName = "polyglot-kotlin"
                    scriptSourceDirectory = "src/main/scripts"
                    outputDirectory = "target/classes"
                    testOutputDirectory = "target/test-classes"
                    directory = "target"
                    filters["*.java", "*.scala"]
                }
            }
        """.trimIndent(), result.toString())
    }

    @Test fun writeProjectBuildPlugins() {
        val model = Model().apply {
            groupId = "io.takari.polyglot"
            artifactId = "polyglot-kotlin"
            version = "0.2.2-SNAPSHOT"

            build = Build().apply {
                plugins = listOf(Plugin().apply {
                    groupId = "org.jetbrains.kotlin"
                    artifactId = "kotlin-maven-plugin"
                    version = "1.1.61"
                    executions = listOf(PluginExecution().apply {
                        id = "compile"
                        phase = "compile"
                        addGoal("compile")
                    }, PluginExecution().apply {
                        id = "test_compile"
                        phase = "test_compile"
                        addGoal("test_compile")
                    })
                    dependencies = listOf(Dependency().apply {
                        groupId = "org.jetbrains.kotlinx"
                        artifactId = "kotlinx-coroutines-core"
                        version = "0.22.5"
                        scope = "compile"
                    })
                })
            }
        }
        val result = StringWriter()

        //WHEN
        writer.write(result, mutableMapOf(), model)

        //THEN
        assertEquals("""
            project {
                groupId = "io.takari.polyglot"
                artifactId = "polyglot-kotlin"
                packaging = jar
                build {
                    plugins {
                        plugin("org.jetbrains.kotlin:kotlin-maven-plugin:1.1.61") {
                            executions {
                                execution(id = "compile", phase = "compile", goals = arrayOf("compile"))
                                execution(id = "test_compile", phase = "test_compile", goals = arrayOf("test_compile"))
                            }
                            dependencies {
                                compile("org.jetbrains.kotlinx:kotlinx-coroutines-core:0.22.5")
                            }
                        }
                    }
                }
            }
        """.trimIndent(), result.toString())
    }

    @Test fun writeProjectBuildPluginConfiguration() {
        val model = Model().apply {
            groupId = "io.takari.polyglot"
            artifactId = "polyglot-kotlin"
            version = "0.2.2-SNAPSHOT"

            build = Build().apply {
                plugins = listOf(Plugin().apply {
                    groupId = "org.apache.maven.plugins"
                    artifactId = "maven-surefire-plugin"
                    version = "2.20.1"
                    configuration = Xpp3Dom("configuration").apply {
                        val includes = Xpp3Dom("includes")
                        val spec = Xpp3Dom("include")
                        spec.value = "%regex[.*Spec.*]"
                        includes.addChild(spec)
                        val test = Xpp3Dom("include")
                        test.value = "%regex[.*Test.*]"
                        includes.addChild(test)
                        addChild(includes)

                        val argLine = Xpp3Dom("argLine")
                        argLine.value = "-Xmx256m"
                        addChild(argLine)
                    }
                }, Plugin().apply {
                    groupId = "org.eclipse.m2e"
                    artifactId = "lifecycle-mapping"
                    version = "1.0.0"
                    configuration = Xpp3Dom("configuration").apply {
                        val lifecycleMappingMetadata = Xpp3Dom("lifecycleMappingMetadata")
                        val pluginExecutions = Xpp3Dom("pluginExecutions")
                        val pluginExecution = Xpp3Dom("pluginExecution")

                        val pluginExecutionFilter = Xpp3Dom("pluginExecutionFilter")

                        val groupId = Xpp3Dom("groupId")
                        groupId.value = "net.alchim31.maven"
                        pluginExecutionFilter.addChild(groupId)

                        val artifactId = Xpp3Dom("artifactId")
                        artifactId.value = "scala-maven-plugin"
                        pluginExecutionFilter.addChild(artifactId)

                        val versionRange = Xpp3Dom("versionRange")
                        versionRange.value = "[3.3.0,)"
                        pluginExecutionFilter.addChild(versionRange)
                        val goals = Xpp3Dom("goals")
                        val addSource = Xpp3Dom("goal")
                        addSource.value = "add-source"
                        goals.addChild(addSource)
                        val compile = Xpp3Dom("goal")
                        compile.value = "compile"
                        goals.addChild(compile)
                        val testCompile = Xpp3Dom("goal")
                        testCompile.value = "testCompile"
                        goals.addChild(testCompile)
                        pluginExecutionFilter.addChild(goals)


                        val action = Xpp3Dom("action")
                        action.addChild(Xpp3Dom("ignore"))

                        pluginExecution.addChild(pluginExecutionFilter)
                        pluginExecution.addChild(action)
                        pluginExecutions.addChild(pluginExecution)
                        lifecycleMappingMetadata.addChild(pluginExecutions)
                        addChild(lifecycleMappingMetadata)
                    }
                })
            }
        }
        val result = StringWriter()

        //WHEN
        writer.write(result, mutableMapOf(), model)

        //THEN
        assertEquals("""
            project {
                groupId = "io.takari.polyglot"
                artifactId = "polyglot-kotlin"
                packaging = jar
                build {
                    plugins {
                        plugin("org.apache.maven.plugins:maven-surefire-plugin:2.20.1") {
                            configuration {
                                "includes" [
                                    "%regex[.*Spec.*]",
                                    "%regex[.*Test.*]"
                                ]
                                "argLine" += "-Xmx256m"
                            }
                        }
                        plugin("org.eclipse.m2e:lifecycle-mapping:1.0.0") {
                            configuration {
                                "lifecycleMappingMetadata" {
                                    "pluginExecutions" {
                                        "pluginExecution" {
                                            "pluginExecutionFilter" {
                                                "groupId" += "net.alchim31.maven"
                                                "artifactId" += "scala-maven-plugin"
                                                "versionRange" += "[3.3.0,)"
                                                "goals" [
                                                    "add-source",
                                                    "compile",
                                                    "testCompile"
                                                ]
                                            }
                                            "action" {
                                                +"ignore"
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        """.trimIndent(), result.toString())
    }

    @Test fun writeProjectModules() {
        //GIVEN
        val model = Model().apply {
            name = "Polyglot :: Kotlin"
            artifactId = "polyglot-kotlin"
            parent = Parent().apply {
                groupId = "io.takari.polyglot"
                artifactId = "polyglot"
                version = "0.2.2-SNAPSHOT"
            }

            modules = listOf("polyglot - common",
                    "polyglot - atom",
                    "polyglot - ruby",
                    "polyglot - scala",
                    "polyglot - groovy",
                    "polyglot - yaml",
                    "polyglot - clojure",
                    "polyglot - xml",
                    "polyglot - java",
                    "polyglot - kotlin",
                    "polyglot - maven - plugin",
                    "polyglot - translate - plugin")
        }
        val result = StringWriter()

        //WHEN
        writer.write(result, mutableMapOf(), model)

        //THEN
        assertEquals("""
            project {
                name = "Polyglot :: Kotlin"
                parent = "io.takari.polyglot:polyglot:0.2.2-SNAPSHOT"
                artifactId = "polyglot-kotlin"
                packaging = jar
                modules [
                    "polyglot - common",
                    "polyglot - atom",
                    "polyglot - ruby",
                    "polyglot - scala",
                    "polyglot - groovy",
                    "polyglot - yaml",
                    "polyglot - clojure",
                    "polyglot - xml",
                    "polyglot - java",
                    "polyglot - kotlin",
                    "polyglot - maven - plugin",
                    "polyglot - translate - plugin"
                ]
            }
        """.trimIndent(), result.toString())
    }
}