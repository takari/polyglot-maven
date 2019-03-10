project {
    profiles {
        profile {
            id("other")
            activation {
                activeByDefault()
            }
            dependencyManagement {
                dependencies {
                    import("org.jetbrains.kotlin:kotlin-bom:\${kotlin.version}") {
                        type("pom")
                        scope("import")
                    }
                    import(groupId = "org.springframework.boot", artifactId = "spring-boot-dependencies", version = "\${spring-boot.version}", classifier = "java8") {
                        type("pom")
                        scope("import")
                    }
                }
            }
        }
    }
}