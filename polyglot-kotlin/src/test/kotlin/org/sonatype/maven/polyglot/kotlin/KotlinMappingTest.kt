package org.sonatype.maven.polyglot.kotlin

import org.junit.Assert
import org.junit.Test

class KotlinMappingTest {

    @Test
    fun testMapping() {
        val mapping = KotlinMapping()
        val options = mapOf("org.apache.maven.model.building.source" to "/polyglot-maven/polyglot-kotlin/pom.kts")
        Assert.assertTrue(mapping.accept(options))
    }
}
