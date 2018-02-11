//package org.sonatype.maven.polyglot.kotlin

import org.junit.Test

class KotlinModelReaderTest {
    val modelReader = KotlinModelReader()

    @Test fun read() {
        val resource = this.javaClass.getResourceAsStream("/pom.kts")

        val options = mutableMapOf<String, Any>()
        modelReader.read(resource, options)
    }
}