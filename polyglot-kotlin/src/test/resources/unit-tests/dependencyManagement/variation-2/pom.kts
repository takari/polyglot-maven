project {
    dependencyManagement {
        dependencies {
            dependency {
                groupId("org.jetbrains.kotlin")
                artifactId("kotlin-bom")
                version("\${kotlin.version}")
                type("pom")
                scope("import")
            }
            dependency {
                groupId("org.springframework.boot")
                artifactId("spring-boot-dependencies")
                version("\${spring-boot.version}")
                type("pom")
                scope("import")
                classifier("java8")
            }
        }
    }
}