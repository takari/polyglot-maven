# Polyglot Kotlin  

## Overview

- Supports adhoc task execution via Kotlin lambdas and external scripts.
- Supports the complete Maven model.
- Preserves well-known Maven idioms with a Kotlin flavor, allowing easy adoption.
- Supports Xpp3DOM (XML) configuration using idiomatic Kotlin.
- Provides idiomatic Kotlin extensions that improve readability and reduce lines of code.
- Support for syntax highlighting and more in IntellijIDEA

## Kotlin POM Example

```kotlin
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
                .excluding("org.slf4j:jul-logger")

        test("junit:junit:$junitVersion").excluding("org.hamcrest:hamcrest-core")

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
        execute(id = "hello-script", phase = "process-resources", script = "hello.task.kts")
    }
}
```

## IDE support

Any IDE that supports loading Maven extensions should be able to resolve the Maven model.
Currently, only IntelliJ IDEA seems to do this. 

For auto-completion to work it is necessary that the polyglot-kotlin JAR and its dependencies are on the compile 
classpath so IntelliJ IDEA will index them. The easiest way to archieve this without changing your project's
dependencies is to add it as a global library. 

To do this open `Project Structure` (Ctrl+Alt+Shift+S) -> `Global Libraries` -> click the `+` sign and choose `From maven...`.
Fill in `io.takari.polyglot:polyglot-kotlin:0.4.2`, check the `Sources` checkbox and press `OK`.
Add the global library to your (IntelliJ IDEA) module and auto-completion should work.

## Known Issues

- Each Kotlin `ScriptEngine` requires a classpath that includes information from Maven's own classpath as well as the
  extension's `ClassRealm` (which is a special type of `ClassLoader`). This classpath is used to create an additional
  `ClassLoader` for use by the `ScriptEngine` which requires the Maven process to consume more metaspace memory than it
  would if the Kotlin `ScriptEngine` were able to obtain the class information it needs from the existing classloader.
  This is a limitation of the Kotlin script engine. (See [KT-27956](https://youtrack.jetbrains.com/issue/KT-27956))

  While this issue does not appear to have a significant impact on Maven's command-line performance, it does affect the
  performance of IntelliJ IDEA's remote Maven server process (as of Build #IU_183.5912.21). The IDE's remote Maven
  server appears to create a new `ClassRealm` every time it reimports the Maven model. This in turn causes a new
  `ScriptEngine` with its additional `ClassLoader` to be re-created even though these are created as singletons. This
  results in the IDE's Maven server process consuming much more metaspace over time than it would otherwise, especially
  for multi-module projects.

  If you care about this issue, please vote for [KT-27956](https://youtrack.jetbrains.com/issue/KT-27956) under the
  Kotlin project so that the Kotlin script engine can get the information it needs from the existing classloader.

- IntelliJ IDEA does not appear to auto-matically detect changes to `pom.kts`. The workaround is to manually reimport
  the Maven project.

## Resources

- [Blog post about usage from @lion7](https://craftsmen.nl/polyglot-maven-kotlin-instead-of-xml/)