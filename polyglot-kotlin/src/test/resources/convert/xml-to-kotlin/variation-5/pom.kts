/*
 * KotlinModelWriterTests
 * convert/xml-to-kotlin/variation-5
 */
project("Polyglot Kotlin Demo") {

    id("com.example:polyglot-demo:1.0.0-SNAPSHOT:jar")

    build {
        // The following sample execution demonstrates how you can bind one or more custom script
        // executions to build lifecycle phases. For a comprehensive list of all lifecycle phases,
        // see http://maven.apache.org/ref/3.6.0/maven-core/lifecycles.html
        execute(id = "sample-script", phase = "initialize") {
            with(project) {
                println("[initialize] Project Name:  ${name}")
                println("[initialize] Project ID:    ${groupId}:${artifactId}:${version}:${packaging}")
                println("[initialize] Project Model: ${script}")
                println("[initialize] Project Dependencies:")
                dependencies.forEachIndexed { index, dep ->
                    println("             [${index}] ${dep.groupId}:${dep.artifactId}:${dep.version}")
                }
            }
        }
    }
}
