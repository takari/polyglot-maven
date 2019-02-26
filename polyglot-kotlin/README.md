# Polyglot Kotlin  
Here is the code:
```kotlin
project("Polyglot :: Kotlin") {
    parent {
        groupId = "io.takari.polyglot"
        artifactId = "polyglot"
        version = "0.2.2-SNAPSHOT"
    }
    id = "com.example:my-project:1.0:jar"

    val junitVersion = 4.12
    
    properties {
        "kotlin.version" assign "1.3.21"
    }

    dependencies {
        compile("org.jetbrains.kotlin:kotlin-stdlib:" + it["kotlin.version"])
        compile(it.groupId + ":polyglot-common:" + it.version)
                .exclusions("org.slf4j:jul-logger")

        test("junit:junit:$junitVersion").excluding("org.hamcrest:hamcrest-core")
        provided(groupId = "org.apache.maven.plugin-tools", artifactId = "maven-plugin-annotations", version = "3.4")
    }
}
```
## Motivation
- Support adhoc task execution via kotlin lambdas and external scripts.
- Support the entire Maven model
- Preserve well-known Maven idioms in a kotlin flavor.
- Support Xpp3DOM (XML) configuration using idiomatic kotlin.
- Provide idiomatic kotlin extensions that improve readability and minimize lines of code

## IDE support
Any IDE that supports Maven extensions should work, but IntelliJ IDEA is the only one that seems to support the
polyglot-maven extension so far.
