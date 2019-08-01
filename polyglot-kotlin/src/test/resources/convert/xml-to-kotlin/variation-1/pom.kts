/*
 * KotlinModelWriterTests
 * convert/xml-to-kotlin/variation-1
 */
project("Polyglot Kotlin Demo") {

    parent(
        "org.springframework.boot",
        "spring-boot-starter-parent",
        "2.1.0.RELEASE",
        "../../pom.kts"
    )

    id("com.example:polyglot-demo:\${revision}:jar")

    description("A demo of Maven Polyglot with Kotlin")
    url("https://github.com/takari/polyglot-maven")
    inceptionYear("2012")

    organization("takari", "http://takari.io/")

    licenses {
        license("The Eclipse Public License, Version 1.0") {
            url("http://www.eclipse.org/legal/epl-v10.html")
            distribution("repo")
            comments("See LICENSE.txt at the root of this repository.")
        }
    }

    developers {
        developer("Robert Thornton <thorntonrp@example.com>") {
            url("https://github.com/thorntonrp")
            organization("Demo Organization")
            organizationUrl("http://www.example.com")
            roles("Software Engineer")
            timezone("UTC")
            properties {
                "phone" to "1-XXX-XXX-XXXX"
            }
        }
        developer("jdoe <jdoe@example.com>") {
            id("johndoe")
        }
        developer("daffy <daffyduck@example.com>") {
            id("daffy")
        }
        developer("Daffy Duck") {
            id("daffy")
        }
        developer("Daffy Duck <daffyduck@example.com>") {
            id("Daffy Duck")
        }
        developer("bugs <bugs@example.com>")
        developer("Bugs Bunny") {
            id("bugs")
        }
        developer("Bugs Bunny <bugs@example.com>")
    }

    contributors {
        contributor("Boris Karloff") {
            email("franky@example.com")
            url("https://github.com/franky13241")
            organization("Demo Organization")
            organizationUrl("http://www.example.com")
            roles("Software Engineer")
            timezone("UTC")
            properties {
                "phone" to "1-XXX-XXX-XXXX"
            }
        }
    }

    mailingLists {
        mailingList("polyglot-kotlin") {
            subscribe("mailto:polyglot-kotlin@example.com?subject=subscribe")
            unsubscribe("mailto:polyglot-kotlin@example.com?subject=unsubscribe")
            post("mailto:polyglot-kotlin@example.com")
            archive("http://www.example.com/mail/polyglot-kotlin/")
            otherArchives("http://mail.example.com/polyglot-kotlin/")
        }
    }

    prerequisites {
        maven("3.5.0")
    }

    modules(
        "demo-kotlin-library",
        "demo-kotlin-plugin",
        "demo-kotlin-app",
        "demo-kotlin-parent"
    )

    scm {
        url("https://code.example.com/git/scm/takari/polyglot-kotlin-demo.git")
        connection("scm:https://code.example.com/git/scm/takari/polyglot-kotlin-demo.git")
        developerConnection("scm:git:ssh://git@code.example.com:7999/takari/polyglot-kotlin-demo.git")
        tag("v1.1.1")
    }

    issueManagement {
        system("JIRA")
        url("http://jira.example.com")
    }

    ciManagement {
        system("Jenkins")
        url("http://jenkins.example.com")
        notifiers {
            notifier {
                type("email")
                address("build-manager@example.com")
                sendOnError(false)
                sendOnFailure(false)
                sendOnSuccess(false)
                sendOnWarning(false)
                configuration {
                    "projectId" to 1234
                }
            }
        }
    }

    distributionManagement {
        repository("Example Release Repository") {
            id("example-releases")
            url("http://repo.example.com/maven/releases/")
            layout("legacy")
            uniqueVersion(false)
        }
        snapshotRepository("Example Snapshot Repository") {
            id("example-snapshots")
            url("http://repo.example.com/maven/snapshots/")
        }
        site("Example Documents") {
            id("example-documents")
            url("http://repo.example.com/maven/documents/")
        }
        downloadUrl("http://repo.example.com/maven/artifacts")
        relocation {
            groupId("org.example.polyglot")
            artifactId("polyglot-kotlin-demo")
            version("1.0.1")
            message("The group has been renamed")
        }
        status("none")
    }

    properties {
        "empty.property" to ""
        "java.version" to 11
        "kotlin.version" to "1.3.31"
        "project.build.sourceEncoding" to "UTF-8"
        "project.reporting.outputEncoding" to "UTF-8"
        "release.version" to "1.0.0-SNAPSHOT"
        "revision" to "\${release.version}"
        "spring-boot-admin.version" to "2.1.1"
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
        compile("org.springframework.boot:spring-boot-configuration-processor") {
            optional(true)
        }
        runtime("org.springframework.boot:spring-boot-devtools") {
            optional(true)
        }
        compile("de.codecentric:spring-boot-admin-starter-client:\${spring-boot-admin.version}")
        compile("de.codecentric:spring-boot-admin-starter-server:\${spring-boot-admin.version}")
        test("org.springframework.boot:spring-boot-starter-test") {
            exclusions("junit:junit")
        }
        test("org.springframework.security:spring-security-test")
        test("org.junit.jupiter:junit-jupiter-engine")
    }

    repositories {
        repository("Demo Main Repository") {
            id("mvn-main-repo")
            url("https://repo.example.com/mvn-main-repo")
            releases {
                checksumPolicy("fail")
            }
            snapshots {
                checksumPolicy("warn")
            }
        }
    }

    pluginRepositories {
        pluginRepository("Demo Plugin Repository") {
            id("mvn-plugin-repo")
            url("https://repo.example.com/mvn-plugin-repo")
            releases {
                checksumPolicy("warn")
            }
            snapshots {
                checksumPolicy("ignore")
            }
        }
    }

    build {
        finalName("\${project.artifactId}")
        defaultGoal("clean verify")
        sourceDirectory("src/main/kotlin")
        testSourceDirectory("src/test/kotlin")
        scriptSourceDirectory("src/scripts/kotlin")
        directory("target/generated-sources/kotlin")
        outputDirectory("target/classes")
        testOutputDirectory("target/test-classes")
        filters("buildId", "revision")
        resources {
            resource {
                targetPath("static")
                filtering(false)
                directory("src/main/static")
                includes("**/*.xml", "**/*.txt")
                excludes("**/.tmp", "**/node_modules")
            }
        }
        testResources {
            testResource {
                targetPath("static")
                filtering(false)
                directory("src/test/static")
                includes("**/*.xml", "**/*.txt")
                excludes("**/.tmp", "**/node_modules")
            }
        }
        extensions {
            extension("com.example.extensions:custom-application-lifecycle")
            extension("com.example.extensions:another-custom-extension")
        }
        pluginManagement {
            plugins {
                plugin("org.apache.maven.plugins:maven-enforcer-plugin") {
                    executions {
                        execution("enforce-revision-is-set") {
                            goals("enforce")
                            configuration {
                                "rules" {
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
                }
                plugin("org.apache.maven.plugins:maven-failsafe-plugin:\${maven-failsafe-plugin.version}") {
                    executions {
                        execution {
                            phase("none")
                        }
                    }
                    configuration {
                        "testFailureIgnore" to true
                    }
                }
                plugin("org.jetbrains.kotlin:kotlin-maven-plugin:\${kotlin.version}") {
                    executions {
                        execution("compile") {
                            phase("compile")
                            goals("compile")
                        }
                        execution("test-compile") {
                            phase("test-compile")
                            goals("test-compile")
                        }
                    }
                    dependencies {
                        dependency("org.jetbrains.kotlin:kotlin-maven-allopen:\${kotlin.version}")
                        dependency("org.jetbrains.kotlin:kotlin-maven-noarg:\${kotlin.version}")
                    }
                    configuration {
                        "jvmTarget" to 1.8
                        "javaParameters" to true
                        "compilerPlugins" {
                            "plugin" to "spring"
                            "plugin" to "jpa"
                        }
                    }
                }
                plugin("org.apache.maven.plugins:maven-compiler-plugin") {
                    executions {
                        execution("default-compile") {
                            phase("none")
                        }
                        execution("default-testCompile") {
                            phase("none")
                        }
                        execution("java-compile") {
                            phase("compile")
                            goals("compile")
                        }
                        execution("java-test-compile") {
                            phase("test-compile")
                            goals("testCompile")
                        }
                    }
                }
            }
        }
        plugins {
            plugin("org.apache.maven.plugins:maven-compiler-plugin")
            plugin("org.apache.maven.plugins:maven-enforcer-plugin")
            plugin("org.springframework.boot:spring-boot-maven-plugin")
            plugin("org.jetbrains.kotlin:kotlin-maven-plugin")
        }
    }

    reports {
        "note" to """The "reports" section consists of arbitrary XML"""
        "coverage" to "62%"
    }

    reporting {
        excludeDefaults(false)
        outputDirectory("target/reports")
        plugins {
            plugin("org.codehaus.reports:reports-maven-plugin:1.2.3-SNAPSHOT") {
                reportSets {
                    reportSet("reports") {
                        id("reports")
                        reports("issues", "dependencies", "coverage")
                        inherited(false)
                        configuration {
                            "args" to """The "configuration" section consists of arbitrary XML"""
                        }
                    }
                }
                configuration {
                    "args" to """The "configuration" section consists of arbitrary XML"""
                }
            }
        }
    }

    profiles {
        profile("release") {
            activation {
                activeByDefault(true)
                jdk("1.8")
                os("Windows 10") {
                    family("windows")
                    arch("x64")
                    version("8.0.0")
                }
                property("foo") {
                    value("bar")
                }
                file {
                    missing("bar.txt")
                    exists("foo.txt")
                }
            }
            distributionManagement {
                repository("Example Release Repository") {
                    id("example-releases")
                    url("http://repo.example.com/maven/releases/")
                    layout("legacy")
                    uniqueVersion(false)
                }
                snapshotRepository("Example Snapshot Repository") {
                    id("example-snapshots")
                    url("http://repo.example.com/maven/snapshots/")
                }
                site("Example Documents") {
                    id("example-documents")
                    url("http://repo.example.com/maven/documents/")
                }
                downloadUrl("http://repo.example.com/maven/artifacts")
                relocation {
                    groupId("org.example.polyglot")
                    artifactId("polyglot-kotlin-demo")
                    version("1.0.1")
                    message("The group has been renamed")
                }
                status("none")
            }
            properties {
                "empty.property" to ""
                "java.version" to 11
                "kotlin.version" to "1.3.31"
                "project.build.sourceEncoding" to "UTF-8"
                "project.reporting.outputEncoding" to "UTF-8"
                "release.version" to "1.0.0-SNAPSHOT"
                "revision" to "\${release.version}"
                "spring-boot-admin.version" to "2.1.1"
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
                compile("org.springframework.boot:spring-boot-configuration-processor") {
                    optional(true)
                }
                compile("org.springframework.boot:spring-boot-devtools") {
                    optional(true)
                }
                compile("de.codecentric:spring-boot-admin-starter-client:\${spring-boot-admin.version}")
                compile("de.codecentric:spring-boot-admin-starter-server:\${spring-boot-admin.version}")
                test("org.springframework.boot:spring-boot-starter-test") {
                    exclusions("junit:junit")
                }
                test("org.springframework.security:spring-security-test")
                test("org.junit.jupiter:junit-jupiter-engine")
            }
            repositories {
                repository("Demo Main Repository") {
                    id("mvn-main-repo")
                    url("https://repo.example.com/mvn-main-repo")
                    releases {
                        checksumPolicy("fail")
                    }
                    snapshots {
                        checksumPolicy("warn")
                    }
                }
            }
            pluginRepositories {
                pluginRepository("Demo Plugin Repository") {
                    id("mvn-plugin-repo")
                    url("https://repo.example.com/mvn-plugin-repo")
                    releases {
                        checksumPolicy("warn")
                    }
                    snapshots {
                        checksumPolicy("ignore")
                    }
                }
            }
            build {
                pluginManagement {
                    plugins {
                        plugin("org.apache.maven.plugins:maven-enforcer-plugin") {
                            executions {
                                execution("enforce-revision-is-set") {
                                    goals("enforce")
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
            reports {
                "note" to """The "reports" section consists of arbitrary XML"""
                "coverage" to "62%"
                "scanSucceeded" to true
                "properties" {
                    "one" to 1.0
                    "two" to 2.0
                    "three" to "3.0.0"
                    "four" to 4
                    "javaVersion" to "\${java.version}"
                    "kotlinVersion" to "\${kotlin.version}"
                }
            }
            reporting {
                excludeDefaults(false)
                outputDirectory("target/reports")
                plugins {
                    plugin("org.codehaus.reports:reports-maven-plugin:1.2.3-SNAPSHOT") {
                        reportSets {
                            reportSet("reports") {
                                id("reports")
                                reports("issues", "dependencies", "coverage")
                                inherited(false)
                                configuration {
                                    "args" to """The "configuration" section consists of arbitrary XML"""
                                }
                            }
                        }
                        configuration {
                            "args" to """The "configuration" section consists of arbitrary XML"""
                        }
                    }
                    plugin("org.apache.maven.plugins:custom-report-plugin")
                }
            }
        }
        profile("code-coverage") {
            build {
                plugins {
                    plugin("org.jacoco:jacoco-maven-plugin:0.8.2") {
                        executions {
                            execution("prepare-agent") {
                                goals("prepare-agent")
                            }
                            execution("prepare-agent-integration") {
                                goals("prepare-agent-integration")
                            }
                            execution("reports") {
                                goals("report", "report-integration")
                            }
                        }
                    }
                }
            }
        }
        profile("java11") {
            activation {
                jdk("11")
            }
        }
    }
}
