/*
 * KotlinModelWriterTests
 * convert#xml-to-kotlin#variation-0
 */
project {

    parent {
        groupId = "org.springframework.boot"
        artifactId = "spring-boot-starter-parent"
        version = "2.1.0.RELEASE"
        relativePath = "../../pom.xml"
    }

    groupId = "com.example"
    artifactId = "polyglot-demo"
    version = "\${revision}"

    name = "Polyglot Kotlin Demo"
    description = "A demo of Maven Polyglot with Kotlin"
    url = "https://github.com/takari/polyglot-maven"
    inceptionYear = "2012"

    organization {
        name = "takari"
        url = "http://takari.io/"
    }

    licenses {
        license {
            name = "The Eclipse Public License, Version 1.0"
            url = "http://www.eclipse.org/legal/epl-v10.html"
            distribution = "repo"
            comments = "See LICENSE.txt at the root of this repository."
        }
    }

    developers {
        developer {
            id = "thorntonrp"
            name = "Robert Thornton"
            email = "thorntonrp@example.com"
            url = "https://github.com/thorntonrp"
            organization = "Demo Organization"
            organizationUrl = "http://www.example.com"
            roles = listOf("Software Engineer")
            timezone = "UTC"
            properties {
                "phone" to "1-XXX-XXX-XXXX"
            }
        }
        developer {
            id = "johndoe"
            name = "jdoe"
            email = "jdoe@example.com"
        }
        developer {
            id = "daffy"
            name = "daffy"
            email = "daffyduck@example.com"
        }
        developer {
            id = "daffy"
            name = "Daffy Duck"
        }
        developer {
            id = "Daffy Duck"
            name = "Daffy Duck"
            email = "daffyduck@example.com"
        }
        developer {
            id = "bugs"
            name = "bugs"
            email = "bugs@example.com"
        }
        developer {
            id = "bugs"
            name = "Bugs Bunny"
        }
        developer {
            id = "bugs"
            name = "Bugs Bunny"
            email = "bugs@example.com"
        }
    }

    contributors {
        contributor {
            name = "Boris Karloff"
            email = "franky@example.com"
            url = "https://github.com/franky13241"
            organization = "Demo Organization"
            organizationUrl = "http://www.example.com"
            roles = listOf("Software Engineer")
            timezone = "UTC"
            properties {
                "phone" to "1-XXX-XXX-XXXX"
            }
        }
    }

    mailingLists {
        mailingList {
            name = "polyglot-kotlin"
            subscribe = "mailto:polyglot-kotlin@example.com?subject=subscribe"
            unsubscribe = "mailto:polyglot-kotlin@example.com?subject=unsubscribe"
            post = "mailto:polyglot-kotlin@example.com"
            archive = "http://www.example.com/mail/polyglot-kotlin/"
            otherArchives = listOf("http://mail.example.com/polyglot-kotlin/")
        }
    }

    prerequisites {
        maven = "3.5.0"
    }

    modules(
        "demo-kotlin-library",
        "demo-kotlin-plugin",
        "demo-kotlin-app",
        "demo-kotlin-parent"
    )

    scm {
        url = "https://code.example.com/git/scm/takari/polyglot-kotlin-demo.git"
        connection = "scm:https://code.example.com/git/scm/takari/polyglot-kotlin-demo.git"
        developerConnection = "scm:git:ssh://git@code.example.com:7999/takari/polyglot-kotlin-demo.git"
        tag = "v1.1.1"
    }

    issueManagement {
        system = "JIRA"
        url = "http://jira.example.com"
    }

    ciManagement {
        system = "Jenkins"
        url = "http://jenkins.example.com"
        notifiers {
            notifier {
                type = "email"
                address = "build-manager@example.com"
                isSendOnError = false
                isSendOnFailure = false
                isSendOnSuccess = false
                isSendOnWarning = false
                configuration {
                    "projectId" to 1234
                }
            }
        }
    }

    distributionManagement {
        repository {
            id = "example-releases"
            name = "Example Release Repository"
            url = "http://repo.example.com/maven/releases/"
            layout = "legacy"
            isUniqueVersion = false
        }
        snapshotRepository {
            id = "example-snapshots"
            name = "Example Snapshot Repository"
            url = "http://repo.example.com/maven/snapshots/"
        }
        site {
            id = "example-documents"
            name = "Example Documents"
            url = "http://repo.example.com/maven/documents/"
        }
        downloadUrl = "http://repo.example.com/maven/artifacts"
        relocation {
            groupId = "org.example.polyglot"
            artifactId = "polyglot-kotlin-demo"
            version = "1.0.1"
            message = "The group has been renamed"
        }
        status = "none"
    }

    properties {
        "boolean.property.false" to false
        "boolean.property.true" to true
        "double.property.max" to 1.7976931348623157E308
        "double.property.min" to 1.7976931348623157E308
        "empty.property" to ""
        "integer.property.max" to 2147483647
        "integer.property.min" to -2147483648
        "java.version" to 11
        "kotlin.version" to "1.3.21"
        "long.property.max" to 9223372036854775807
        "long.property.min" to 9223372036854775807
        "project.build.sourceEncoding" to "UTF-8"
        "project.reporting.outputEncoding" to "UTF-8"
        "release.version" to "1.0.0-SNAPSHOT"
        "revision" to "\${release.version}"
        "spring-boot-admin.version" to "2.1.1"
    }

    dependencyManagement {
        dependencies {
            dependency {
                groupId = "org.jetbrains.kotlin"
                artifactId = "kotlin-bom"
                version = "\${kotlin.version}"
                type = "pom"
                scope = "import"
            }
            dependency {
                groupId = "org.springframework.boot"
                artifactId = "spring-boot-dependencies"
                version = "\${spring-boot.version}"
                type = "pom"
                scope = "import"
            }
        }
    }

    dependencies {
        dependency {
            groupId = "org.springframework.boot"
            artifactId = "spring-boot-starter-actuator"
        }
        dependency {
            groupId = "org.springframework.boot"
            artifactId = "spring-boot-starter-cache"
        }
        dependency {
            groupId = "org.springframework.boot"
            artifactId = "spring-boot-starter-hateoas"
        }
        dependency {
            groupId = "org.springframework.boot"
            artifactId = "spring-boot-starter-oauth2-client"
        }
        dependency {
            groupId = "org.springframework.boot"
            artifactId = "spring-boot-starter-security"
        }
        dependency {
            groupId = "org.springframework.boot"
            artifactId = "spring-boot-starter-validation"
            isOptional = true
        }
        dependency {
            groupId = "org.springframework.boot"
            artifactId = "spring-boot-starter-web"
        }
        dependency {
            groupId = "org.springframework.boot"
            artifactId = "spring-boot-configuration-processor"
            scope = "compile"
            isOptional = true
        }
        dependency {
            groupId = "org.springframework.boot"
            artifactId = "spring-boot-devtools"
            scope = "runtime"
            isOptional = true
        }
        dependency {
            groupId = "de.codecentric"
            artifactId = "spring-boot-admin-starter-client"
            version = "\${spring-boot-admin.version}"
            scope = "compile"
        }
        dependency {
            groupId = "de.codecentric"
            artifactId = "spring-boot-admin-starter-server"
            version = "\${spring-boot-admin.version}"
            scope = "compile"
        }
        dependency {
            groupId = "org.springframework.boot"
            artifactId = "spring-boot-starter-test"
            scope = "test"
            exclusions {
                exclusion {
                    groupId = "junit"
                    artifactId = "junit"
                }
            }
        }
        dependency {
            groupId = "org.springframework.security"
            artifactId = "spring-security-test"
            scope = "test"
        }
        dependency {
            groupId = "org.junit.jupiter"
            artifactId = "junit-jupiter-engine"
            scope = "test"
        }
    }

    repositories {
        repository {
            id = "mvn-main-repo"
            name = "Demo Main Repository"
            url = "https://repo.example.com/mvn-main-repo"
            releases {
                checksumPolicy = "fail"
            }
            snapshots {
                checksumPolicy = "warn"
            }
        }
    }

    pluginRepositories {
        pluginRepository {
            id = "mvn-plugin-repo"
            name = "Demo Plugin Repository"
            url = "https://repo.example.com/mvn-plugin-repo"
            releases {
                checksumPolicy = "warn"
            }
            snapshots {
                checksumPolicy = "ignore"
            }
        }
    }

    build {
        finalName = "\${project.artifactId}"
        defaultGoal = "clean verify"
        sourceDirectory = "src/main/kotlin"
        testSourceDirectory = "src/test/kotlin"
        scriptSourceDirectory = "src/scripts/kotlin"
        directory = "target/generated-sources/kotlin"
        outputDirectory = "target/classes"
        testOutputDirectory = "target/test-classes"
        filters = listOf("buildId", "revision")
        resources {
            resource {
                targetPath = "static"
                isFiltering = false
                directory = "src/main/static"
                includes = listOf("**/*.xml", "**/*.txt")
                excludes = listOf("**/.tmp", "**/node_modules")
            }
        }
        testResources {
            testResource {
                targetPath = "static"
                isFiltering = false
                directory = "src/test/static"
                includes = listOf("**/*.xml", "**/*.txt")
                excludes = listOf("**/.tmp", "**/node_modules")
            }
        }
        extensions {
            extension {
                groupId = "com.example.extensions"
                artifactId = "custom-application-lifecycle"
            }
            extension {
                groupId = "com.example.extensions"
                artifactId = "another-custom-extension"
            }
        }
        pluginManagement {
            plugins {
                plugin {
                    groupId = "org.apache.maven.plugins"
                    artifactId = "maven-enforcer-plugin"
                    executions {
                        execution {
                            id = "enforce-revision-is-set"
                            goals = listOf("enforce")
                            configuration = """
                                <configuration>
                                  <rules>
                                    <requireMavenVersion>
                                      <version>[3.5,)</version>
                                    </requireMavenVersion>
                                    <requireJavaVersion>
                                      <version>[1.8,9.0)</version>
                                    </requireJavaVersion>
                                  </rules>
                                </configuration>
                                """
                        }
                    }
                }
                plugin {
                    groupId = "org.apache.maven.plugins"
                    artifactId = "maven-failsafe-plugin"
                    version = "\${maven-failsafe-plugin.version}"
                    executions {
                        execution {
                            phase = "none"
                        }
                    }
                    configuration = """
                        <configuration>
                          <testFailureIgnore>true</testFailureIgnore>
                        </configuration>
                        """
                }
                plugin {
                    groupId = "org.jetbrains.kotlin"
                    artifactId = "kotlin-maven-plugin"
                    version = "\${kotlin.version}"
                    executions {
                        execution {
                            id = "compile"
                            phase = "compile"
                            goals = listOf("compile")
                        }
                        execution {
                            id = "test-compile"
                            phase = "test-compile"
                            goals = listOf("test-compile")
                        }
                    }
                    dependencies {
                        dependency {
                            groupId = "org.jetbrains.kotlin"
                            artifactId = "kotlin-maven-allopen"
                            version = "\${kotlin.version}"
                        }
                        dependency {
                            groupId = "org.jetbrains.kotlin"
                            artifactId = "kotlin-maven-noarg"
                            version = "\${kotlin.version}"
                        }
                    }
                    configuration = """
                        <configuration>
                          <jvmTarget>1.8</jvmTarget>
                          <javaParameters>true</javaParameters>
                          <compilerPlugins>
                            <plugin>spring</plugin>
                            <plugin>jpa</plugin>
                          </compilerPlugins>
                        </configuration>
                        """
                }
                plugin {
                    groupId = "org.apache.maven.plugins"
                    artifactId = "maven-compiler-plugin"
                    executions {
                        execution {
                            id = "default-compile"
                            phase = "none"
                        }
                        execution {
                            id = "default-testCompile"
                            phase = "none"
                        }
                        execution {
                            id = "java-compile"
                            phase = "compile"
                            goals = listOf("compile")
                        }
                        execution {
                            id = "java-test-compile"
                            phase = "test-compile"
                            goals = listOf("testCompile")
                        }
                    }
                }
            }
        }
        plugins {
            plugin {
                groupId = "org.apache.maven.plugins"
                artifactId = "maven-compiler-plugin"
            }
            plugin {
                groupId = "org.apache.maven.plugins"
                artifactId = "maven-enforcer-plugin"
            }
            plugin {
                groupId = "org.springframework.boot"
                artifactId = "spring-boot-maven-plugin"
            }
            plugin {
                groupId = "org.jetbrains.kotlin"
                artifactId = "kotlin-maven-plugin"
            }
        }
    }

    reports = """
        <reports>
          <note>The &quot;reports&quot; section consists of arbitrary XML</note>
          <coverage>62%</coverage>
        </reports>
        """

    reporting {
        isExcludeDefaults = false
        outputDirectory = "target/reports"
        plugins {
            plugin {
                groupId = "org.codehaus.reports"
                artifactId = "reports-maven-plugin"
                version = "1.2.3-SNAPSHOT"
                reportSets {
                    reportSet {
                        id = "reports"
                        reports = listOf("issues", "dependencies", "coverage")
                        isInherited = false
                        configuration = """
                            <configuration>
                              <args>The &quot;configuration&quot; section consists of arbitrary XML</args>
                            </configuration>
                            """
                    }
                }
                configuration = """
                    <configuration>
                      <args>The &quot;configuration&quot; section consists of arbitrary XML</args>
                    </configuration>
                    """
            }
        }
    }

    profiles {
        profile {
            id = "release"
            activation {
                isActiveByDefault = true
                jdk = "1.8"
                os {
                    name = "Windows 10"
                    family = "windows"
                    arch = "x64"
                    version = "8.0.0"
                }
                property {
                    name = "foo"
                    value = "bar"
                }
                file {
                    missing = "bar.txt"
                    exists = "foo.txt"
                }
            }
            distributionManagement {
                repository {
                    id = "example-releases"
                    name = "Example Release Repository"
                    url = "http://repo.example.com/maven/releases/"
                    layout = "legacy"
                    isUniqueVersion = false
                }
                snapshotRepository {
                    id = "example-snapshots"
                    name = "Example Snapshot Repository"
                    url = "http://repo.example.com/maven/snapshots/"
                }
                site {
                    id = "example-documents"
                    name = "Example Documents"
                    url = "http://repo.example.com/maven/documents/"
                }
                downloadUrl = "http://repo.example.com/maven/artifacts"
                relocation {
                    groupId = "org.example.polyglot"
                    artifactId = "polyglot-kotlin-demo"
                    version = "1.0.1"
                    message = "The group has been renamed"
                }
                status = "none"
            }
            properties {
                "empty.property" to ""
                "java.version" to 11
                "kotlin.version" to "1.3.21"
                "project.build.sourceEncoding" to "UTF-8"
                "project.reporting.outputEncoding" to "UTF-8"
                "release.version" to "1.0.0-SNAPSHOT"
                "revision" to "\${release.version}"
                "spring-boot-admin.version" to "2.1.1"
            }
            dependencyManagement {
                dependencies {
                    dependency {
                        groupId = "org.jetbrains.kotlin"
                        artifactId = "kotlin-bom"
                        version = "\${kotlin.version}"
                        type = "pom"
                        scope = "import"
                    }
                    dependency {
                        groupId = "org.springframework.boot"
                        artifactId = "spring-boot-dependencies"
                        version = "\${spring-boot.version}"
                        type = "pom"
                        scope = "import"
                    }
                }
            }
            dependencies {
                dependency {
                    groupId = "org.springframework.boot"
                    artifactId = "spring-boot-starter-actuator"
                }
                dependency {
                    groupId = "org.springframework.boot"
                    artifactId = "spring-boot-starter-cache"
                }
                dependency {
                    groupId = "org.springframework.boot"
                    artifactId = "spring-boot-starter-hateoas"
                }
                dependency {
                    groupId = "org.springframework.boot"
                    artifactId = "spring-boot-starter-oauth2-client"
                }
                dependency {
                    groupId = "org.springframework.boot"
                    artifactId = "spring-boot-starter-security"
                }
                dependency {
                    groupId = "org.springframework.boot"
                    artifactId = "spring-boot-starter-validation"
                }
                dependency {
                    groupId = "org.springframework.boot"
                    artifactId = "spring-boot-starter-web"
                }
                dependency {
                    groupId = "org.springframework.boot"
                    artifactId = "spring-boot-configuration-processor"
                    scope = "compile"
                    isOptional = true
                }
                dependency {
                    groupId = "org.springframework.boot"
                    artifactId = "spring-boot-devtools"
                    scope = "compile"
                    isOptional = true
                }
                dependency {
                    groupId = "de.codecentric"
                    artifactId = "spring-boot-admin-starter-client"
                    version = "\${spring-boot-admin.version}"
                    scope = "compile"
                }
                dependency {
                    groupId = "de.codecentric"
                    artifactId = "spring-boot-admin-starter-server"
                    version = "\${spring-boot-admin.version}"
                    scope = "compile"
                }
                dependency {
                    groupId = "org.springframework.boot"
                    artifactId = "spring-boot-starter-test"
                    scope = "test"
                    exclusions {
                        exclusion {
                            groupId = "junit"
                            artifactId = "junit"
                        }
                    }
                }
                dependency {
                    groupId = "org.springframework.security"
                    artifactId = "spring-security-test"
                    scope = "test"
                }
                dependency {
                    groupId = "org.junit.jupiter"
                    artifactId = "junit-jupiter-engine"
                    scope = "test"
                }
            }
            repositories {
                repository {
                    id = "mvn-main-repo"
                    name = "Demo Main Repository"
                    url = "https://repo.example.com/mvn-main-repo"
                    releases {
                        checksumPolicy = "fail"
                    }
                    snapshots {
                        checksumPolicy = "warn"
                    }
                }
            }
            pluginRepositories {
                pluginRepository {
                    id = "mvn-plugin-repo"
                    name = "Demo Plugin Repository"
                    url = "https://repo.example.com/mvn-plugin-repo"
                    releases {
                        checksumPolicy = "warn"
                    }
                    snapshots {
                        checksumPolicy = "ignore"
                    }
                }
            }
            build {
                pluginManagement {
                    plugins {
                        plugin {
                            groupId = "org.apache.maven.plugins"
                            artifactId = "maven-enforcer-plugin"
                            executions {
                                execution {
                                    id = "enforce-revision-is-set"
                                    goals = listOf("enforce")
                                    configuration = """
                                        <configuration>
                                          <rules>
                                            <requireProperty>
                                              <property>release.version</property>
                                              <regex>v\d{4}\.\d{2}\.\d{2}</regex>
                                            </requireProperty>
                                          </rules>
                                        </configuration>
                                        """
                                }
                            }
                        }
                    }
                }
            }
            reports = """
                <reports>
                  <note>The &quot;reports&quot; section consists of arbitrary XML</note>
                  <coverage>62%</coverage>
                  <scanSucceeded>true</scanSucceeded>
                  <properties>
                    <one>1.0</one>
                    <two>2.0</two>
                    <three>3.0.0</three>
                    <four>4</four>
                    <javaVersion>${"$"}{java.version}</javaVersion>
                    <kotlinVersion>${"$"}{kotlin.version}</kotlinVersion>
                  </properties>
                </reports>
                """
            reporting {
                isExcludeDefaults = false
                outputDirectory = "target/reports"
                plugins {
                    plugin {
                        groupId = "org.codehaus.reports"
                        artifactId = "reports-maven-plugin"
                        version = "1.2.3-SNAPSHOT"
                        reportSets {
                            reportSet {
                                id = "reports"
                                reports = listOf("issues", "dependencies", "coverage")
                                isInherited = false
                                configuration = """
                                    <configuration>
                                      <args>The &quot;configuration&quot; section consists of arbitrary XML</args>
                                    </configuration>
                                    """
                            }
                        }
                        configuration = """
                            <configuration>
                              <args>The &quot;configuration&quot; section consists of arbitrary XML</args>
                            </configuration>
                            """
                    }
                    plugin {
                        groupId = "org.apache.maven.plugins"
                        artifactId = "custom-report-plugin"
                    }
                }
            }
        }
        profile {
            id = "code-coverage"
            build {
                plugins {
                    plugin {
                        groupId = "org.jacoco"
                        artifactId = "jacoco-maven-plugin"
                        version = "0.8.2"
                        executions {
                            execution {
                                id = "prepare-agent"
                                goals = listOf("prepare-agent")
                            }
                            execution {
                                id = "prepare-agent-integration"
                                goals = listOf("prepare-agent-integration")
                            }
                            execution {
                                id = "reports"
                                goals = listOf("report", "report-integration")
                            }
                        }
                    }
                }
            }
        }
        profile {
            id = "java11"
            activation {
                jdk = "11"
            }
        }
    }
}
