project("Polyglot :: Kotlin") {

    parent("io.takari.polyglot:polyglot:0.2.2-SNAPSHOT").relativePath("../pom.kts")

    id("com.example:my-project:1.0:jar")

    val junitVersion = 4.12

    properties {
        "maven.compiler.source" to 1.8
        "maven.compiler.target" to 1.8
        "kotlin.version" to "1.3.31"
    }

    dependencies {
        fun get(key: Any) = this@project.properties?.get(key)?.toString() ?: ""

        compile("org.jetbrains.kotlin:kotlin-stdlib:" + get("kotlin.version"))
        compile("org.jetbrains.kotlin:kotlin-runtime:" + get("kotlin.version"))

        runtime(groupId = "io.takari", artifactId = "polyglot-common", version = this@project.version)
                .exclusions("org.slf4j:jul-logger")

        test("junit:junit:$junitVersion").exclusions("org.hamcrest:hamcrest-core")

        provided {
            groupId = "org.apache.maven.plugin-tools"
            artifactId = "maven-plugin-annotations"
            version = "3.4"
        }
    }

    build {
        finalName("\${project.artifactId}")

        sourceDirectory("src/main/kotlin")
        testSourceDirectory("src/test/kotlin")

        plugins {
            plugin(":maven-jar-plugin") {
                configuration {
                    "archive" {
                        "index" to true
                        "manifest" {
                            "addClasspath" to true
                            "mainClass" to "org.test.Main"
                        }
                        "manifestEntries" {
                            "mode" to "development"
                            "url"  to "\${project.url}"
                            "key"  to "value"
                        }
                    }
                }
            }
        }

        // Embedded execute task
        execute(id = "hello", phase = "initialize") {
            log.info("Hello from ${project.name}")
        }

        // External execute task script
        execute(id = "hello-script", phase = "process-resources", script = "src/build/kotlin/hello.task.kts")
    }
}
