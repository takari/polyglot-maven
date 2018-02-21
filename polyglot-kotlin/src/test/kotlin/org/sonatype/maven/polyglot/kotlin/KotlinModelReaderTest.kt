import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import kotlin.test.assertTrue

class KotlinModelReaderTest {
    val modelReader = KotlinModelReader()

    @Test fun readOneLinerParentInKotlinObjectModel() {
        //GIVEN
        val komReader = """
        project {
            name = "simple parent example kom"
            parent = "io.takari.polyglot:polyglot:0.2.2-SNAPSHOT" relativePath "../../pom.kts"
            artifactId = "simpleId"
        }
        """.trimIndent().reader()

        //WHEN
        val poModel = modelReader.read(komReader, mutableMapOf<String, Any>())

        //THEN
        assertThat(poModel.parent.artifactId, equalTo("polyglot"))
        assertThat(poModel.parent.groupId, equalTo("io.takari.polyglot"))
        assertThat(poModel.parent.version, equalTo("0.2.2-SNAPSHOT"))
        assertThat(poModel.parent.relativePath, equalTo("../../pom.kts"))
    }

    @Test fun readFullyQualifiedParentInKotlinObjectModel() {
        //GIVEN
        val komReader = """
        project {
            name = "simple parent example kom"
            parent {
                groupId = "io.takari.polyglot"
                artifactId = "polyglot"
                version = "0.2.2-SNAPSHOT"
                relativePath = "../../pom.kts"
            }
            artifactId = "simpleId"
        }
        """.trimIndent().reader()

        //WHEN
        val poModel = modelReader.read(komReader, mutableMapOf<String, Any>())

        //THEN
        assertThat(poModel.parent.artifactId, equalTo("polyglot"))
        assertThat(poModel.parent.groupId, equalTo("io.takari.polyglot"))
        assertThat(poModel.parent.version, equalTo("0.2.2-SNAPSHOT"))
        assertThat(poModel.parent.relativePath, equalTo("../../pom.kts"))
    }

    @Test fun readBasicKotlinObjectModel() {
        //GIVEN
        val resource = this.javaClass.getResourceAsStream("/pom.kts")

        //WHEN
        val poModel = modelReader.read(resource, mutableMapOf<String, Any>())

        //THEN
        assertThat(poModel.name, equalTo("Polyglot :: Kotlin"))
        assertThat(poModel.artifactId, equalTo("polyglot-kotlin"))

        assertThat(poModel.parent.artifactId, equalTo("polyglot"))
        assertThat(poModel.parent.groupId, equalTo("io.takari.polyglot"))
        assertThat(poModel.parent.version, equalTo("0.2.2-SNAPSHOT"))
        assertThat(poModel.parent.relativePath, equalTo("../../pom.kts"))

        assertThat(poModel.packaging, equalTo("jar"))
    }

    @Test fun readKomProperties() {
        //GIVEN
        val resource = this.javaClass.getResourceAsStream("/pom.kts")

        //WHEN
        val poModel = modelReader.read(resource, mutableMapOf<String, Any>())

        //THEN
        assertThat(poModel.properties["junit.version"] as String, equalTo("4.12"))
        assertThat(poModel.properties["kotlin.version"] as String, equalTo("1.1.61"))
    }

    @Test fun readKomDependencies() {
        //GIVEN
        val resource = this.javaClass.getResourceAsStream("/pom.kts")

        //WHEN
        val poModel = modelReader.read(resource, mutableMapOf<String, Any>())

        //THEN
        assertTrue(poModel.dependencies.size > 0)
        poModel.dependencies.forEach {
            assertThat(it.artifactId, anyOf(
                    equalTo("kotlin-stdlib"),
                    equalTo("polyglot-common"),
                    equalTo("junit"),
                    equalTo("kotlin-test-junit"),
                    equalTo("maven-plugin-annotations")
            ))
            assertThat(it.groupId, anyOf(
                    equalTo("org.jetbrains.kotlin"),
                    equalTo("junit"),
                    equalTo("io.takari.polyglot"),
                    equalTo("org.jetbrains.kotlin"),
                    equalTo("org.apache.maven.plugin-tools")
            ))
            assertThat(it.version, anyOf(
                    equalTo("1.1.61"),
                    equalTo("0.2.2-SNAPSHOT"),
                    equalTo("4.12"),
                    equalTo("1.1.61"),
                    equalTo("LATEST")
            ))
            assertThat(it.scope, anyOf(
                    equalTo("compile"),
                    equalTo("provided"),
                    equalTo("test")
            ))
        }
    }
}