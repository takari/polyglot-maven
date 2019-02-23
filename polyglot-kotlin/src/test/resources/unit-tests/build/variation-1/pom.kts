project {
    build {
        finalName = "\${project.artifactId}"
        defaultGoal = "clean verify"
        sourceDirectory = "src/main/kotlin"
        testSourceDirectory = "src/test/kotlin"
        scriptSourceDirectory = "src/scripts/kotlin"
        directory = "target/generated-sources/kotlin"
        outputDirectory = "target/classes"
        testOutputDirectory = "target/test-classes"
        filters = listOf("buildId", "revision")
        resources {
            resource {
                targetPath = "static"
                isFiltering = false
                directory = "src/main/static"
                includes = listOf("**/*.xml", "**/*.txt")
                excludes = listOf("**/.tmp", "**/node_modules")
            }
        }
        testResources {
            testResource {
                targetPath = "static"
                isFiltering = false
                directory = "src/test/static"
                includes = listOf("**/*.xml", "**/*.txt")
                excludes = listOf("**/.tmp", "**/node_modules")
            }
        }
        extensions {
            extension {
                groupId = "com.example.extensions"
                artifactId = "custom-application-lifecycle"
            }
            extension {
                groupId("com.example.extensions")
                artifactId("another-custom-extension")
                version("1.0")
            }
        }
    }
}