package org.sonatype.maven.polyglot.kotlin

import org.apache.maven.model.*
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
                    "project.build.sourceEncoding" sameAs "UTF-8"
                    "kotlin.version" sameAs "1.2.30"
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
}