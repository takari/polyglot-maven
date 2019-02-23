project {
    build {
        pluginManagement {
            plugins {
                plugin {
                    groupId("org.apache.maven.plugins")
                    artifactId("maven-failsafe-plugin")
                    version("\${maven-failsafe-plugin.version}")
                    execution(phase = "none", goals = listOf("integration-test"))
                    configuration {
                        "testFailureIgnore" to true
                    }
                }
                plugin {
                    groupId("org.apache.maven.plugins")
                    artifactId("maven-enforcer-plugin")
                    version("3.0.0.M2")
                    extensions(true)
                    execution(id = "enforce-revision-is-set", goals = listOf("enforce", "report")) {
                        configuration {
                            "rules" {
                                "requireMavenVersion" {
                                    "version" to "[3.5,)"
                                }
                                "requireJavaVersion" {
                                    "version" to "[1.8,9.0)"
                                }
                            }
                        }
                    }
                    dependency("com.example:sample-lib")
                }
            }
        }
        plugins {
            plugin {
                groupId("org.apache.maven.plugins")
                artifactId("maven-compiler-plugin")
            }
            plugin {
                groupId("org.springframework.boot")
                artifactId("spring-boot-maven-plugin")
                version("2.1.2.RELEASE")
                extensions(true)
            }
        }
    }
}