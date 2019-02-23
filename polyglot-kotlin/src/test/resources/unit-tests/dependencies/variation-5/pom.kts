project {
    dependencies {
        compile(
            groupId = "org.springframework.boot",
            artifactId = "spring-boot-starter",
            version = "2.1.2.RELEASE",
            type = "jar",
            optional = false)
        runtime(
            groupId = "org.springframework.boot",
            artifactId = "spring-boot-devtools",
            version = "2.1.2.RELEASE",
            optional = true)
        provided(
            groupId = "org.springframework.boot",
            artifactId = "spring-boot-configuration-processor",
            version = "2.1.2.RELEASE",
            optional = true)
        system(
            groupId = "org.example",
            artifactId = "custom-lib",
            version = "1.0",
            classifier = "jdk11",
            systemPath = "lib/custom-lib-1.0.jar")
        test(
            groupId = "org.springframework.boot",
            artifactId = "spring-boot-starter-test")
            .excluding("junit:junit", "org.hamcrest:hamcrest")
    }
}