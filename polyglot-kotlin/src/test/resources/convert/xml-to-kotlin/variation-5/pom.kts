/*
 * KotlinModelWriterTests
 * convert#xml-to-kotlin#variation-5
 */
project("Polyglot Kotlin Demo") {

    id("com.example:polyglot-demo:1.0.0-SNAPSHOT:jar")

    build {
        // The following sample execution demonstrates how you can bind one or more custom script
        // executions to build lifecycle phases. For a comprehensive list of all lifecycle phases,
        // see http://maven.apache.org/ref/3.6.0/maven-core/lifecycles.html
        execute(id = "sample-script", phase = "initialize") {
            with(project) {
                log.info("[initialize] Project Name:  ${name}")
                log.info("[initialize] Project ID:    ${groupId}:${artifactId}:${version}:${packaging}")
                log.info("[initialize] Project Model: ${basedir}/pom.kts")
                log.info("[initialize] Project Dependencies:")
                dependencies.forEachIndexed { index, dep ->
                    log.info("             [${index}] ${dep.groupId}:${dep.artifactId}:${dep.version}")
                }
            }
        }
    }
}
