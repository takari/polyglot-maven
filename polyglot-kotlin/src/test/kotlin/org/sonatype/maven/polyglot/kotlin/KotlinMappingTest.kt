package org.sonatype.maven.polyglot.kotlin

import org.codehaus.plexus.PlexusTestCase
import org.junit.Assert
import org.junit.Test
import org.sonatype.maven.polyglot.mapping.Mapping

class KotlinMappingTest : PlexusTestCase() {

    @Test
    fun testMapping() {
        val mapping = lookup(Mapping::class.java, "kotlin")
        val options = mapOf("org.apache.maven.model.building.source" to "/polyglot-maven/polyglot-kotlin/pom.kts")
        Assert.assertTrue(mapping.accept(options))
    }
}
