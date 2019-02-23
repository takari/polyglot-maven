package org.sonatype.maven.polyglot.kotlin

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.sonatype.maven.polyglot.kotlin.testing.AbstractModelTestCase
import java.io.StringWriter

class KotlinModelReaderTests : AbstractModelTestCase() {

    @Test
    fun `convert#kotlin-to-xml#variation-0`() {
        test()
    }

    @Test
    fun `convert#kotlin-to-xml#variation-1`() {
        test()
    }

    @Test
    fun `convert#kotlin-to-xml#variation-2`() {
        test()
    }

    @Test
    fun `convert#kotlin-to-xml#variation-3`() {
        test()
    }

    @Test
    fun `convert#kotlin-to-xml#variation-4`() {
        test()
    }

    @Test
    fun `convert#kotlin-to-xml#variation-5`() {
        test()
    }

    private fun test() {
        with(modelFromPomKts) {
            val modelXmlOutput = StringWriter()
            xmlModelWriter.write(modelXmlOutput, emptyMap(), this)
            assertThat(modelXmlOutput.toString()).isEqualTo(pomXmlSource.readText())
        }
    }
}
