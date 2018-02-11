Project(name = "Polyglot :: Kotlin",
    parent = "polyglot:io.takari.polyglot:0.2.2-SNAPSHOT",
    artifactId = "polyglot-kotlin",
    packaging = jar,

    properties = are(
        "project.build.sourceEncoding" to "UTF-8",
        "junit.version" to 4.12,
        "kotlin.version" to "1.1.61"
    ),

    dependencies = {
        compile("org.jetbrains.kotlin:kotlin-stdlib:" + it["kotlin.version"])
        compile("${it.groupId}:polyglot-common" + it["kotlin.version"])
                    .excludes(
                        "org.slf4j:jul-logger:LATEST"
                    )
        test(
            "junit:junit:${it["junit.version"]}",
            "org.jetbrains.kotlin:kotlin-test-junit:${it["kotlin.version"]}"
        ).excludes(
                "",
                ""
        )
        provided(
            "org.apache.maven.plugin-tools:maven-plugin-annotations:${it[""]}",
            "org.apache.maven.plugin-tools:maven-plugin-annotations"
        )
    }
)