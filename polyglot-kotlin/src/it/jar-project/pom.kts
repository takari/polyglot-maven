fun message(): String {
    return "Hello, World!"
}

project("Maven Polyglot :: Kotlin Demo") {

    id = "io.takari.polyglot:polyglot-kotlin-demo:0.3.3-SNAPSHOT:jar"

    description = "A Maven project model executed with Kotlin"

    properties {
        "maven.compiler.target" to 1.8
        "maven.compiler.source" to 1.8
        "build.date" to java.time.LocalDateTime.now()
    }

    dependencies {
        compile("org.springframework.boot:spring-boot-starter:2.1.2.RELEASE")

        test("junit:junit:4.12")
    }

    build {
        execute("first", "validate") {
            println("[FIRST] Validating project: ${project.name}")
        }

        execute("second", "compile") {
            with(project) {
                println("[SECOND] Compiling project: ${groupId}:${artifactId}:${packaging}:${version}")
            }
        }

        execute("third", "test") {
            println("[THIRD] Testing... (message = ${message()})")
        }

        execute("fourth", "verify") {
            println("[FOURTH] Verifying... (message = ${message()}")
            println("[FOURTH] basedir = ${basedir}")
            println("[FOURTH] project.build.directory = ${project.build.directory}")
            println("[FOURTH] context = ${this}")
            println("[FOURTH] project = ${project}")
        }

        // The following sample execution demonstrates how you can bind one or more custom script
        // executions to build lifecycle phases. For a comprehensive list of all lifecycle phases,
        // see http://maven.apache.org/ref/3.6.0/maven-core/lifecycles.html
        execute(id = "sample-script", phase = "initialize") {
            with(project) {
                println("[initialize] Project name: ${name}")
                println("[initialize] Project id: ${groupId}:${artifactId}:${version}:${packaging}")
                println("[initialize] Project model: ${basedir}/pom.kts")
                println("[initialize] We have the following dependencies:")
                dependencies.forEachIndexed { i, dep ->
                    println("             [${i}] ${dep.groupId}:${dep.artifactId}:${dep.version}")
                }
            }
        }

        execute(id = "external-script#1", phase = "verify", script = "src/build/scripts/hello.kts")

        execute(id = "external-script#2", phase = "verify") {
            val script = "${basedir}/src/build/scripts/hello.kts"
            eval(script)
        }
    }
}
