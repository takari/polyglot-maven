project {
    name = "Polyglot :: Kotlin"
    parent {
        groupId = "io.takari.polyglot"
        artifactId = "polyglot"
        version = "0.2.2-SNAPSHOT"
    }

    artifactId = "polyglot-kotlin"
    val mavenVersion = "3.5.0"

    modules[
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

    dependencyManagement {
        dependencies {
            arrayOf("polyglot-common", "polyglot-ruby", "polyglot-atom", "polyglot-scala", "polyglot-xml",
                    "polyglot-groovy", "polyglot-yaml", "polyglot-java", "polyglot-kotlin")
                    .map { "io.takari.polyglot:$it:${this@project.version}" }
                    .forEach { compile(it) }
            import(groupId = "org.apache.maven", artifactId = "maven", version = mavenVersion)
            test(groupId = "junit", artifactId = "junit", version = "4.11")
        }
    }

    build {
        pluginManagement {
            plugins {
                plugin(artifactId = "maven-release-plugin", version = "2.5.2") {
                    configuration {
                        "preparationGoals" += "clean install"
                        "mavenExecutorId" += "forked - path"
                    }
                }

                plugin(groupId = "org.eclipse.m2e", artifactId = "lifecycle-mapping", version = "1.0.0") {
                    configuration {
                        "lifecycleMappingMetadata" {
                            "pluginExecutions" {
                                "pluginExecution" {
                                    "pluginExecutionFilter" {
                                        "groupId" += "io.tesla.maven.plugins"
                                        "artifactId" += "tesla-license-plugin"
                                        "versionRange" += "[0.0.1-SNAPSHOT,)"
                                        "goals"[
                                            "check"
                                        ]
                                    }
                                    "action" { +"ignore" }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}