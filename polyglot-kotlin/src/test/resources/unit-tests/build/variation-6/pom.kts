project {
    build {
        pluginManagement {
            plugins {
                plugin {
                    groupId("org.apache.maven.plugins")
                    artifactId("maven-failsafe-plugin")
                    version("\${maven-failsafe-plugin.version}")
                    execution(phase = "none", goals = listOf("integration-test"))
                    inherited(false)
                    configuration = null
                }
                plugin {
                    groupId("org.apache.maven.plugins")
                    artifactId("maven-enforcer-plugin")
                    version("3.0.0.M2")
                    extensions(true)
                    inherited(false)
                    execution(id = "enforce-revision-is-set", goals = listOf("enforce", "report")) {
                        inherited(false)
                        configuration = null
                    }
                    dependency(groupId = "com.example", artifactId = "sample-lib")
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