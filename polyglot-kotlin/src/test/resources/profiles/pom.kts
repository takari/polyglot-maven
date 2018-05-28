project {
    name = "Polyglot :: Kotlin"
    groupId = "io.takari.polyglot"
    artifactId = "polyglot-kotlin"
    version = "0.2.2-SNAPSHOT"

    profiles {
        profile("generate-code") {
            activation(default = true, jdk = "1.8") {
                file {
                    exists = "src"
                    missing = "target"
                }
                os(name = "Ubuntu", family = "LTS", version = "16.04", arch = "x64")

                property(name = "propertyName", value = "propertyValue")
            }
            dependencies {
                test("org.jetbrains.kotlin:kotlin-test-junit:1.2.30")
            }
            build {
                plugins {
                    plugin(groupId = "org.codehaus.modello", artifactId = "modello-maven-plugin", version = "1.9.1") {
                        configuration {
                            "models"["src/main/mdo/maven.mdo"]
                        }
                        executions {
                            execution(id = "java-code", goals = arrayOf("xpp3 - writer", "xpp3 - reader")) {
                                configuration {
                                    "outputDirectory" += "\${project.basedir}/generated-src/java"
                                    "version" += "4.1.0"
                                }
                            }
                            execution(id = "xsd", goal = "xsd") {
                                configuration {
                                    "version" += "4.1.0"
                                    "outputDirectory" += "\${project.basedir}/src/main/resources/xsd"
                                    "packageWithVersion" += "true"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}