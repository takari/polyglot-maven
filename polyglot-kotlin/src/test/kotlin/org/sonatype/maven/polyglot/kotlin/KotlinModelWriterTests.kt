package org.sonatype.maven.polyglot.kotlin

import org.junit.Test
import org.sonatype.maven.polyglot.kotlin.testing.AbstractModelTestCase

class KotlinModelWriterTests(testName: String) : AbstractModelTestCase(testName) {

    @Test
    fun `test#convert#xml-to-kotlin#variation-0`() {
        doTest(
                "file.comment" to "${this::class.simpleName}\n$testBasePath",
                "xml.dsl.enabled" to false,
                "flavor" to "block",
                "sample.executions" to false
        )
    }

    @Test
    fun `test#convert#xml-to-kotlin#variation-1`() {
        doTest(
                "file.comment" to "${this::class.simpleName}\n$testBasePath",
                "xml.dsl.enabled" to true,
                "flavor" to "mixed",
                "sample.executions" to true
        )
    }

    @Test
    fun `test#convert#xml-to-kotlin#variation-2`() {
        doTest(
                "file.comment" to "${this::class.simpleName}\n$testBasePath",
                "xml.dsl.enabled" to false,
                "flavor" to "block",
                "sample.executions" to false
        )
    }

    @Test
    fun `test#convert#xml-to-kotlin#variation-3`() {
        doTest(
                "file.comment" to "${this::class.simpleName}\n$testBasePath",
                "xml.dsl.enabled" to true,
                "flavor" to "mixed",
                "sample.executions" to true
        )
    }

    @Test
    fun `test#convert#xml-to-kotlin#variation-4`() {
        doTest(
                "file.comment" to "${this::class.simpleName}\n$testBasePath",
                "xml.dsl.enabled" to true,
                "flavor" to "mixed",
                "sample.executions" to true
        )
    }

    @Test
    fun `test#convert#xml-to-kotlin#variation-5`() {
        doTest(
                "file.comment" to "${this::class.simpleName}\n$testBasePath",
                "xml.dsl.enabled" to true,
                "flavor" to "mixed",
                "sample.executions" to true
        )
    }

    private fun doTest(vararg options: Pair<String, Any?>) {
        val kts = pomXml.xml2model(*options).model2kts(*options)
        assertEquals(pomKts, kts)
        assertEquals(pomXml, kts.kts2model().model2xml())
    }
}
