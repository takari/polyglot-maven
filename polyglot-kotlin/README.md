# Polyglot Kotlin  
Here is the code:
```kotlin
project {
    name = "Polyglot :: Kotlin"
    parent {
        groupId = "io.takari.polyglot"
        artifactId = "polyglot"
        version = "0.2.2-SNAPSHOT"
    }
    artifactId = "regular-project"
    packaging = jar

    val junitVersion = 4.12
    
    properties {
        "kotlin.version" assign "1.1.61"
    }

    dependencies {
        compile("org.jetbrains.kotlin:kotlin-stdlib:" + it["kotlin.version"])
        compile(it.groupId + ":polyglot-common:" + it.version)
                .exclusions("org.slf4j:jul-logger")

        test(
                "junit:junit:$junitVersion" exclusions "org.hamcrest:hamcrest-core",
                "org.jetbrains.kotlin:kotlin-test-junit:${it["kotlin.version"]}"
        )
        provided(groupId = "org.apache.maven.plugin-tools", artifactId = "maven-plugin-annotations", version = "3.4")
    }
}
```
## Motivation
Eye friendly kotlin script in [70 LOC](https://github.com/takari/polyglot-maven/blob/master/polyglot-kotlin/src/test/resources/multi-module/pom.kts) comparing to [215 lines](https://github.com/takari/polyglot-maven/blob/master/pom.xml) of XML


## IDE support
Targeting IntelliJ IDEA only.


## Not supported yet  
* reports
* licenses
* scm
* distributionManagement
* contributors
* repositories
* organization
* developers
* mailingLists
* prerequisites
* issueManagement
* ciManagement
* reporting
* arbitrary kotlin code execution on maven phase. Not sure if maven users would like to have such feature.
 

