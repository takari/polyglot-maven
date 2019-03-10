project {
    build {
        finalName("\${project.artifactId}")
        defaultGoal("clean verify")
        sourceDirectory("src/main/kotlin")
        testSourceDirectory("src/test/kotlin")
        scriptSourceDirectory("src/scripts/kotlin")
        directory("target/generated-sources/kotlin")
        outputDirectory("target/classes")
        testOutputDirectory("target/test-classes")
        filters("buildId", "revision")
        resources {
            resource {
                targetPath("static")
                filtering(false)
                directory("src/main/static")
                includes("**/*.xml", "**/*.txt")
                excludes("**/.tmp", "**/node_modules")
            }
        }
        testResources {
            testResource {
                targetPath("static")
                filtering(false)
                directory("src/test/static")
                includes("**/*.xml", "**/*.txt")
                excludes("**/.tmp", "**/node_modules")
            }
        }
        extensions(
            "com.example.extensions:custom-application-lifecycle",
            "com.example.extensions:another-custom-extension"
        )
    }
}