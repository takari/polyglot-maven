project {
    dependencies {
        dependency {
            groupId = "org.springframework.boot"
            artifactId = "spring-boot-starter"
            version = "2.1.2.RELEASE"
            type = "jar"
            scope = "compile"
            isOptional = false
        }
        dependency {
            groupId = "org.springframework.boot"
            artifactId = "spring-boot-devtools"
            version = "2.1.2.RELEASE"
            scope = "runtime"
            isOptional = true
        }
        dependency {
            groupId = "org.springframework.boot"
            artifactId = "spring-boot-configuration-processor"
            version = "2.1.2.RELEASE"
            scope = "provided"
            isOptional = true
        }
        dependency {
            groupId = "org.example"
            artifactId = "custom-lib"
            version = "1.0"
            classifier = "jdk11"
            scope = "system"
            systemPath = "lib/custom-lib-1.0.jar"
        }
        dependency {
            groupId = "org.springframework.boot"
            artifactId = "spring-boot-starter-test"
            scope = "test"
            exclusions {
                exclusion {
                    groupId = "junit"
                    artifactId = "junit"
                }
                exclusion {
                    groupId = "org.hamcrest"
                    artifactId = "hamcrest"
                }
            }
        }
    }
}