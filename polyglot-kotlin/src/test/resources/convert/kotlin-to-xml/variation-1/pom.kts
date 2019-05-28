project {
    artifactId = "polyglot-demo"
    groupId = "com.example"
    version = "\${revision}"
    packaging = "jar"

    name = "Polyglot :: Kotlin :: Demo"
    description = "A demo of Maven Polyglot with Kotlin"

    licenses {
        license {
            name = "The Eclipse Public License, Version 1.0"
            url = "http://www.eclipse.org/legal/epl-v10.html"
            distribution = "repo"
        }
    }

    parent {
        groupId = "org.springframework.boot"
        artifactId = "spring-boot-starter-parent"
        version = "2.1.0.RELEASE"
        relativePath = "../../pom.kts"
    }

    properties {
        "empty.property" to ""
        "boolean.property.true" to true
        "boolean.property.false" to false
        "integer.property.min" to Int.MIN_VALUE
        "integer.property.max" to Int.MAX_VALUE
        "long.property.min" to Long.MAX_VALUE
        "long.property.max" to Long.MAX_VALUE
        "double.property.min" to Double.MAX_VALUE
        "double.property.max" to Double.MAX_VALUE
        "java.version" to "11"
        "revision" to "\${release.version}"
        "release.version" to "1.0.0-SNAPSHOT"
        "kotlin.version" to "1.3.31"
        "spring-boot-admin.version" to "2.1.1"
        "project.build.sourceEncoding" to "UTF-8"
        "project.reporting.outputEncoding" to "UTF-8"
    }

    dependencyManagement {
        dependencies {
            import("org.jetbrains.kotlin:kotlin-bom:\${kotlin.version}")
            import("org.springframework.boot:spring-boot-dependencies:\${spring-boot.version}")
        }
    }

    dependencies {
        dependency("org.springframework.boot:spring-boot-starter-actuator")
        dependency("org.springframework.boot:spring-boot-starter-cache")
        dependency("org.springframework.boot:spring-boot-starter-hateoas")
        dependency("org.springframework.boot:spring-boot-starter-oauth2-client")
        dependency("org.springframework.boot:spring-boot-starter-security")
        dependency("org.springframework.boot:spring-boot-starter-validation")
        dependency("org.springframework.boot:spring-boot-starter-web")

        optional("org.springframework.boot:spring-boot-configuration-processor")
        optional("org.springframework.boot:spring-boot-devtools")

        compile("de.codecentric:spring-boot-admin-starter-client:\${spring-boot-admin.version}")
        compile("de.codecentric:spring-boot-admin-starter-server:\${spring-boot-admin.version}")

        test("org.springframework.boot:spring-boot-starter-test").exclusions("junit:junit")
        test("org.springframework.security:spring-security-test")
        test("org.junit.jupiter:junit-jupiter-engine")
        test("org.jetbrains.kotlin:kotlin-test-junit").exclusions("junit:junit")
    }

    build {
        finalName = "\${project.artifactId}"
        defaultGoal = "clean verify"
        extensions("com.example.extensions:custom-application-lifecycle")
        pluginManagement {
            plugins {
                plugin("org.apache.maven.plugins:maven-enforcer-plugin") {
                    execution(id = "enforce-revision-is-set", goals = listOf("enforce")) {
                        configuration {
                            "rules"{
                                "requireMavenVersion" {
                                    "version" to "[3.5,)"
                                }
                                "requireJavaVersion" {
                                    "version" to "[1.8,9.0)"
                                }
                            }
                        }
                    }
                }
                plugin("org.apache.maven.plugins:maven-failsafe-plugin:\${maven-failsafe-plugin.version}") {
                    configuration("""<testFailureIgnore>true</testFailureIgnore>""")
                    execution(id = "default", phase = "none")
                }
                plugin("org.jetbrains.kotlin:kotlin-maven-plugin:\${kotlin.version}") {
                    dependency("org.jetbrains.kotlin:kotlin-maven-allopen:\${kotlin.version}")
                    dependency("org.jetbrains.kotlin:kotlin-maven-noarg:\${kotlin.version}")
                    execution(id = "compile", phase = "compile", goals = listOf("compile"))
                    execution(id = "test-compile", phase = "test-compile", goals = listOf("test-compile"))
                    configuration {
                        "jvmTarget" to "1.8"
                        "javaParameters" to "true"
                        "compilerPlugins" {
                            "plugin" to "spring"
                            "plugin" to "jpa"
                        }
                    }
                }
                plugin("org.apache.maven.plugins:maven-compiler-plugin") {
                    execution(id = "default-compile", phase = "none")
                    execution(id = "default-testCompile", phase = "none")
                    execution(id = "java-compile", phase = "compile", goals = listOf("compile"))
                    execution(id = "java-test-compile", phase = "test-compile", goals = listOf("testCompile"))
                }
            }
        }
        plugins {
            plugin("org.apache.maven.plugins:maven-enforcer-plugin")
            plugin("org.springframework.boot:spring-boot-maven-plugin")
            plugin("org.jetbrains.kotlin:kotlin-maven-plugin")
            plugin("org.apache.maven.plugins:maven-compiler-plugin")
        }

        execute(id = "hello", phase = "validate") {
            println("[validate] Hello, from $script")
        }

        execute(id = "hello", phase = "compile") {
            println("[compile] Hello, from $script")
        }

        execute(id = "hello", phase = "test-compile") {
            println("[test-compile] Hello, from $script")
        }

        execute(id = "hello", phase = "test") {
            println("[test] Hello, from $script")
        }

        execute(id = "hello", phase = "package") {
            println("[package] Hello, from $script")
        }

        execute(id = "hello", phase = "integration-test") {
            println("[integration-test] Hello, from $script")
        }

        execute(id = "hello", phase = "verify") {
            println("[verify] Hello, from $script")
        }

        execute(id = "hello", phase = "install") {
            println("[install] Hello, from $script")
        }

        execute(id = "hello", phase = "deploy") {
            println("[deploy] Hello, from $script")
        }
    }

    profiles {
        profile {
            id = "release"
            build {
                pluginManagement {
                    plugins {
                        plugin("org.apache.maven.plugins:maven-enforcer-plugin") {
                            execution(id = "enforce-revision-is-set", goals = listOf("enforce")) {
                                configuration {
                                    "rules" {
                                        "requireProperty" {
                                            "property" to "release.version"
                                            "regex" to """v\d{4}\.\d{2}\.\d{2}"""
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        profile {
            id = "code-coverage"
            build {
                plugins {
                    plugin("org.jacoco:jacoco-maven-plugin:0.8.2") {
                        execution(id = "prepare-agent", goals = listOf("prepare-agent"))
                        execution(id = "prepare-agent-integration", goals = listOf("prepare-agent-integration"))
                        execution(id = "reports", goals = listOf("report", "report-integration"))
                    }
                }
            }
        }
    }

    repositories {
        repository {
            id = "mvn-main-repo"
            name = "Demo Main Repository"
            url = "https://repo.example.com/mvn-main-repo"
            releases { checksumPolicy = "fail" }
            snapshots { checksumPolicy = "warn" }
        }
    }
    pluginRepositories {
        pluginRepository {
            id = "mvn-plugin-repo"
            name = "Demo Plugin Repository"
            url = "https://repo.example.com/mvn-plugin-repo"
            releases { checksumPolicy = "warn" }
            snapshots { checksumPolicy = "ignore" }
        }
    }
}
