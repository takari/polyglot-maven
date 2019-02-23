project {
    dependencies {
        compile(
            groupId = "org.springframework.boot",
            artifactId = "spring-boot-starter",
            type = "jar",
            optional = false)
        runtime(
            groupId = "org.springframework.boot",
            artifactId = "spring-boot-devtools",
            optional = true)
        optional(
            groupId = "org.springframework.boot",
            artifactId = "spring-boot-configuration-processor",
            scope = "provided")
        system(
            groupId = "org.example",
            artifactId = "custom-lib",
            classifier = "jdk11",
            systemPath = "lib/custom-lib-1.0.jar")
        test(
            groupId = "org.springframework.boot",
            artifactId = "spring-boot-starter-test")
            .excluding("junit:junit", "org.hamcrest:hamcrest")
    }
}