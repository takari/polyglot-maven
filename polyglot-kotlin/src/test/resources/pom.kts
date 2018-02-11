project {
    name = "Polyglot :: Kotlin"
    parent = "io.takari.polyglot:polyglot:0.2.2-SNAPSHOT"
    groupId = "io.takari.polyglot"
    artifactId = "polyglot-kotlin"
    version = "0.2.2-SNAPSHOT"
    packaging = jar

    val junitVersion = 4.12

    properties {
        "project.build.sourceEncoding" sameAs "UTF-8"
        "junit.version" += 4.12
        "kotlin.version" sameAs "1.1.61"
    }

    dependencies {
        compile("org.jetbrains.kotlin:kotlin-stdlib:" + it["kotlin.version"])
        compile("$groupId:polyglot-common:$version")
                .exclusions("org.slf4j:jul-logger")

        test(
                "junit:junit:$junitVersion" exclusions "org.hamcrest:hamcrest-core",
                "org.jetbrains.kotlin:kotlin-test-junit:${it["kotlin.version"]}"
        )
        provided("org.apache.maven.plugin-tools:maven-plugin-annotations:LATEST")
    }
}