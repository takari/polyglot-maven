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
        compile(it.groupId + ":polyglot-common:" + it.version).type(jar).optional().classifier("jdk6")
                .exclusions("org.slf4j:jul-logger")

        test(
                "junit:junit:${it["junit.version"]}",
                "org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion" type jar optional true classifier "jdk7"
                        exclusions "org.hamcrest:hamcrest-core"
        )

        provided("org.apache.maven.plugin-tools:maven-plugin-annotations:LATEST")

        system(groupId = "org.projectlombok", artifactId = "lombok", version = "1.16.20",
               type = pom,
               optional = true,
               classifier = "jdk8",
               systemPath = "../libs/"
        )
    }

    build {
        sourceDirectory = "src/main/kotlin"
        testSourceDirectory = "src/test/kotlin"
        finalName = "polyglot-kotlin"

        plugins {
            plugin("org.jetbrains.kotlin:kotlin-maven-plugin:$kotlinVersion") {
                executions {
                    execution(id = compile, phase = compile, goal = compile)
                    execution(id = test_compile, phase = test_compile, goal = test_compile)
                }
            }

            plugin(groupId = "org.apache.maven.plugins", artifactId = "maven-surefire-plugin", version = "2.20.1") {
                configuration {
                    "includes" [
                        "%regex[.*Spec.*]",
                        "%regex[.*Test.*]"
                    ]
                    "argLine" += "-Xmx256m"
                }
            }

            plugin(groupId = "org.eclipse.m2e", artifactId = "lifecycle-mapping", version = "1.0.0") {
                configuration {
                    "lifecycleMappingMetadata" {
                        "pluginExecutions" {
                            "pluginExecution" {
                                "pluginExecutionFilter" {
                                    "groupId" += "net.alchim31.maven"
                                    "artifactId" += "scala-maven-plugin"
                                    "versionRange" += "[3.3.0,)"
                                    "goals" [
                                        "add-source",
                                        "compile",
                                        "testCompile"
                                    ]
                                }
                                "action" { +"ignore" }
                            }
                        }
                    }
                }
            }
        }
    }
}