package org.sonatype.maven.polyglot.kotlin

import org.junit.Test

class KotlinMappingTests {

    @Test
    fun test() {
        val options = mapOf("org.apache.maven.model.building.source" to "/polyglot-maven/polyglot-kotlin/pom.kts")
        val mapping = KotlinMapping()
        mapping.accept(options)
    }
}
