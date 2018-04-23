package org.sonatype.maven.polyglot.kotlin

import org.apache.maven.model.Dependency
import org.apache.maven.model.Model
import org.apache.maven.model.Parent
import org.junit.Test
import java.io.StringWriter
import java.util.*
import kotlin.test.assertEquals

class KotlinModelWriterTest {
    val writer = KotlinModelWriter()

    @Test fun shouldWriteProjectRequiredDetails() {
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

    @Test fun shouldWriteProjectDetails() {
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

    @Test fun shouldWriteProjectParent() {
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

    @Test fun shouldWriteProjectProperties() {
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

    @Test fun shouldWriteProjectDependencies() {
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
            },
            Dependency().apply {
                scope = "test"
                groupId = "org.jetbrains.kotlin"
                artifactId = "kotlin-test-junit"
                version = "1.1.61"
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
                        "junit:junit:4.11",
                        "org.jetbrains.kotlin:kotlin-test-junit:1.1.61"
                    )
                }
            }
        """.trimIndent(), result.toString())
    }
}