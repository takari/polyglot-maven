project {
    name = "Polyglot :: Kotlin"
    parent = "io.takari.polyglot:polyglot:0.2.2-SNAPSHOT" relativePath "../../pom.kts"
    artifactId = "polyglot-kotlin"
    packaging = jar

    val kotlinVersion = "1.1.61"

    properties {
        "project.build.sourceEncoding" sameAs "UTF-8"
        "junit.version" += 4.12
    }

    dependencies {
        compile("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
        compile("$groupId:polyglot-common:$version").
                exclusions("org.slf4j:jul-logger")

        test(
                "junit:junit:${it["junit.version"]}" exclusions "org.hamcrest:hamcrest-core",
                "org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion" type jar
        )

        provided("org.apache.maven.plugin-tools:maven-plugin-annotations:LATEST")
        provided(artifactId = "lombok",
                 groupId = "org.projectlombok",
                 version = "1.16.20",
                 type = pom)
    }
}


