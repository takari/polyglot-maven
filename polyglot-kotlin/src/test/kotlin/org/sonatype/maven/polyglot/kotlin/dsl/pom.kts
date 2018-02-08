Project(name = "Polyglot :: Kotlin",
    artifactId = "polyglot-kotlin",
    parent = "polyglot:io.takari.polyglot:0.2.2-SNAPSHOT",
    packaging = jar,

    properties = {
        "project.build.sourceEncoding" to "UTF-8"
        "junit.version" to 4.12
        "kotlin.version" to "1.1.61"
    },

    dependencies = {
        compile {
            "org.jetbrains.kotlin:kotlin-stdlib:$[kotlin.version]"
            "io.takari.polyglot:polyglot-common"
        }
        test {
            "junit:junit:$[junit.version]"
            "org.jetbrains.kotlin:kotlin-test-junit:$[kotlin.version]"
        }
        provided {
            "org.apache.maven.plugin-tools:maven-plugin-annotations"
        }
    }
)