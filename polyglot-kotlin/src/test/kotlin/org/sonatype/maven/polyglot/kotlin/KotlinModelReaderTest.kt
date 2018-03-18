
import PomGenerator.pluginWithConfiguration
import assertk.assert
import assertk.assertions.isEqualTo
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

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
        with(poModel.parent) {
            assertThat(artifactId, equalTo("polyglot"))
            assertThat(groupId, equalTo("io.takari.polyglot"))
            assertThat(version, equalTo("0.2.2-SNAPSHOT"))
            assertThat(relativePath, equalTo("../../pom.kts"))
        }
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
        with(poModel.parent) {
            assertThat(artifactId, equalTo("polyglot"))
            assertThat(groupId, equalTo("io.takari.polyglot"))
            assertThat(version, equalTo("0.2.2-SNAPSHOT"))
            assertThat(relativePath, equalTo("../../pom.kts"))
        }
    }

    @Test fun readBasicKotlinObjectModel() {
        //GIVEN
        val resource = this.javaClass.getResourceAsStream("/pom.kts")

        //WHEN
        val poModel = modelReader.read(resource, mutableMapOf<String, Any>())

        //THEN
        with(poModel) {
            assertThat(name, equalTo("Polyglot :: Kotlin"))
            assertThat(artifactId, equalTo("polyglot-kotlin"))

            assertThat(parent.artifactId, equalTo("polyglot"))
            assertThat(parent.groupId, equalTo("io.takari.polyglot"))
            assertThat(parent.version, equalTo("0.2.2-SNAPSHOT"))
            assertThat(parent.relativePath, equalTo("../../pom.kts"))

            assertThat(packaging, equalTo("jar"))
        }
    }

    @Test fun readKomProperties() {
        //GIVEN
        val resource = this.javaClass.getResourceAsStream("/pom.kts")

        //WHEN
        val poModel = modelReader.read(resource, mutableMapOf<String, Any>())

        //THEN
        with(poModel.properties) {
            assert(this["junit.version"] as String).isEqualTo("4.12")
            assert(this["project.build.sourceEncoding"] as String).isEqualTo("UTF-8")
        }
    }

    @Test fun readKomDependencies() {
        //GIVEN
        val resource = this.javaClass.getResourceAsStream("/pom.kts")

        //WHEN
        val poModel = modelReader.read(resource, mutableMapOf<String, Any>())

        //THEN
        assert(poModel.dependencies) {
            containsArtifact("org.jetbrains.kotlin:kotlin-stdlib:1.1.61", "compile")
            containsArtifact("io.takari.polyglot:polyglot-common:0.2.2-SNAPSHOT", "compile", classifier = "jdk6", optional = true)

            containsArtifact("junit:junit:4.12", "test")
            containsArtifact("org.jetbrains.kotlin:kotlin-test-junit:1.1.61", "test", jar, "jdk7", optional = true)

            containsArtifact("org.apache.maven.plugin-tools:maven-plugin-annotations:LATEST", "provided")
            containsArtifact("org.projectlombok:lombok:1.16.20", "system", pom, "jdk8", "../libs/", true)
        }
    }

    @Test fun readKomBuild() {
        //GIVEN
        val resource = this.javaClass.getResourceAsStream("/pom.kts")

        //WHEN
        val poModel = modelReader.read(resource, mutableMapOf<String, Any>())

        //THEN
        with(poModel.build){
            assert(sourceDirectory).isEqualTo("src/main/kotlin")
            assert(testSourceDirectory).isEqualTo("src/test/kotlin")
            assert(finalName).isEqualTo("polyglot-kotlin")
        }
    }

    @Test fun readBuildPlugins() {
        //GIVEN
        val resource = this.javaClass.getResourceAsStream("/pom.kts")

        //WHEN
        val poModel = modelReader.read(resource, mutableMapOf<String, Any>())

        //THEN
        with(poModel.build.plugins.first()) {
            assert(groupId).isEqualTo("org.hetbrains.kotlin")
            assert(artifactId).isEqualTo("kotlin-maven-plugin")
            assert(version).isEqualTo("1.1.61")
        }
    }

    @Test fun readBuildPluginExecutions() {
        //GIVEN
        val resource = this.javaClass.getResourceAsStream("/pom.kts")

        //WHEN
        val poModel = modelReader.read(resource, mutableMapOf<String, Any>())

        //THEN
        assert(poModel.build.plugins.first().executions) {
            hasExecution(id = "compile", phase = "compile", goal = "compile")
            hasExecution(id = "test-compile", phase = "test-compile", goal = "test-compile")
        }
    }

    @Test fun readBuildPluginConfigurationValues() {
        //GIVEN
        val resource = this.javaClass.getResourceAsStream("/pom.kts")

        //WHEN
        val poModel = modelReader.read(resource, mutableMapOf<String, Any>())

        //THEN
        assert(poModel.build.plugins.find {  it.groupId == "org.apache.maven.plugins" }!!) {
            hasConfiguration(pluginWithConfiguration("""
                <includes>
                    <include>%regex[.*Spec.*]</include>
                    <include>%regex[.*Test.*]</include>
                </includes>
            """))
        }
    }

    @Test fun readBuildPluginComplexConfigurationValues() {
        //GIVEN
        val resource = this.javaClass.getResourceAsStream("/pom.kts")

        //WHEN
        val poModel = modelReader.read(resource, mutableMapOf<String, Any>())

        //THEN
        assert(poModel.build.plugins.find {  it.artifactId == "lifecycle-mapping" }!!) {
            hasConfiguration(pluginWithConfiguration("""
                <lifecycleMappingMetadata>
                  <pluginExecutions>
                    <pluginExecution>
                      <pluginExecutionFilter>
                        <groupId>net.alchim31.maven</groupId>
                        <artifactId>scala-maven-plugin</artifactId>
                        <versionRange>[3.3.0,)</versionRange>
                        <goals>
                          <goal>add-source</goal>
                          <goal>compile</goal>
                          <goal>testCompile</goal>
                        </goals>
                      </pluginExecutionFilter>
                      <action>
                        <ignore />
                      </action>
                    </pluginExecution>
                  </pluginExecutions>
                </lifecycleMappingMetadata>
            """))
        }
    }

    @Test fun readPluginDependencies(){
        //GIVEN
        val resource = this.javaClass.getResourceAsStream("/pom.kts")

        //WHEN
        val poModel = modelReader.read(resource, mutableMapOf<String, Any>())

        //THEN
        assert(poModel.build.plugins.find {  it.artifactId == "kotlin-maven-plugin" }!!.dependencies) {
            containsArtifact("org.jetbrains.kotlinx:kotlinx-coroutines-core:0.22.5", "compile")
        }
    }
}