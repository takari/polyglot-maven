package org.sonatype.maven.polyglot.kotlin

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.sonatype.maven.polyglot.kotlin.testing.AbstractModelTestCase
import java.io.StringWriter

class KotlinModelWriterTests : AbstractModelTestCase() {

    @Test
    fun `convert#xml-to-kotlin#variation-0`() {
        test(
            "file.comment" to "${this::class.simpleName}\n${testName}",
            "xml.dsl.enabled" to false,
            "flavor" to "block",
            "sample.executions" to false
        )
    }

    @Test
    fun `convert#xml-to-kotlin#variation-1`() {
        test(
            "file.comment" to "${this::class.simpleName}\n${testName}",
            "xml.dsl.enabled" to true,
            "flavor" to "mixed",
            "sample.executions" to true
        )
    }

    @Test
    fun `convert#xml-to-kotlin#variation-2`() {
        test(
            "file.comment" to "${this::class.simpleName}\n${testName}",
            "xml.dsl.enabled" to false,
            "flavor" to "block",
            "sample.executions" to false
        )
    }

    @Test
    fun `convert#xml-to-kotlin#variation-3`() {
        test(
            "file.comment" to "${this::class.simpleName}\n${testName}",
            "xml.dsl.enabled" to true,
            "flavor" to "mixed",
            "sample.executions" to true
        )
    }

    @Test
    fun `convert#xml-to-kotlin#variation-4`() {
        test(
            "file.comment" to "${this::class.simpleName}\n${testName}",
            "xml.dsl.enabled" to true,
            "flavor" to "mixed",
            "sample.executions" to true
        )
    }

    @Test
    fun `convert#xml-to-kotlin#variation-5`() {
        test(
            "file.comment" to "${this::class.simpleName}\n${testName}",
            "xml.dsl.enabled" to true,
            "flavor" to "mixed",
            "sample.executions" to true
        )
    }

    private fun test(vararg options: Pair<String, Any?>) {
        val modelXml = pomXmlSource.readText()
        val expectedModelKts = pomKtsSource.readText()
        val modelFromXml = xmlModelReader.read(modelXml.reader(), mapOf(*options))
        val modelKtsOutput = StringWriter()
        kotlinModelWriter.write(modelKtsOutput, mapOf(*options), modelFromXml)
        assertThat(modelKtsOutput.toString()).isEqualTo(expectedModelKts)

        val modelFromKts = kotlinModelReader.read(modelKtsOutput.toString().reader(), mapOf(*options))
        val modelXmlOutput = StringWriter()
        xmlModelWriter.write(modelXmlOutput, mapOf(*options), modelFromKts)
        assertThat(modelXmlOutput.toString()).isEqualTo(modelXml)
    }
}
