project {
    dependencies {
        dependency(
            groupId = "org.springframework.boot",
            artifactId = "spring-boot-starter",
            version = "2.1.2.RELEASE",
            type = "jar",
            scope = "compile",
            optional = false)
        dependency(
            groupId = "org.springframework.boot",
            artifactId = "spring-boot-devtools",
            version = "2.1.2.RELEASE",
            scope = "runtime",
            optional = true)
        dependency(
            groupId = "org.springframework.boot",
            artifactId = "spring-boot-configuration-processor",
            version = "2.1.2.RELEASE",
            scope = "provided",
            optional = true)
        dependency(
            groupId = "org.example",
            artifactId = "custom-lib",
            version = "1.0",
            classifier = "jdk11",
            scope = "system",
            systemPath = "lib/custom-lib-1.0.jar")
        dependency(
            groupId = "org.springframework.boot",
            artifactId = "spring-boot-starter-test",
            scope = "test").exclusions("junit:junit", "org.hamcrest:hamcrest")
    }
}