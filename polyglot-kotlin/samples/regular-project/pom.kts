project {
    name = "Polyglot :: Kotlin"
    parent = "io.takari.polyglot:polyglot:0.2.2-SNAPSHOT"
    artifactId = "regular-project"
    packaging = jar

    val junitVersion = 4.12

    properties {
        "project.build.sourceEncoding" assign "UTF-8"
        "junit.version" += 4.12
        "kotlin.version" assign "1.1.61"
    }

    dependencies {
        compile("org.jetbrains.kotlin:kotlin-stdlib:" + it["kotlin.version"])
        compile(it.groupId + ":polyglot-common:" + it.version)
                .exclusions("org.slf4j:jul-logger")

        test(
                "junit:junit:$junitVersion" exclusions "org.hamcrest:hamcrest-core",
                "org.jetbrains.kotlin:kotlin-test-junit:${it["kotlin.version"]}"
        )
        provided(
                "io.takari.polyglot:polyglot-kotlin:0.2.2-SNAPSHOT",
                "org.apache.maven.plugin-tools:maven-plugin-annotations:LATEST"
        )
    }
}