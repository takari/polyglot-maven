package org.sonatype.maven.polyglot.kotlin.testing

import junit.framework.TestCase
import org.apache.maven.model.Model
import org.apache.maven.model.io.DefaultModelReader
import org.apache.maven.model.io.DefaultModelWriter
import org.apache.maven.model.io.ModelReader
import org.apache.maven.model.io.ModelWriter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert
import org.sonatype.maven.polyglot.kotlin.KotlinModelReader
import org.sonatype.maven.polyglot.kotlin.KotlinModelWriter
import org.sonatype.maven.polyglot.kotlin.dsl.propertiesFactory
import java.io.File

abstract class AbstractModelTestCase(testName: String) : TestCase(testName) {

    init {
        propertiesFactory = { org.sonatype.maven.polyglot.kotlin.util.Properties() }
    }

    protected val testBasePath: String = testName.removePrefix("test#").replace('#', '/')
    private val testResources: File = File("src/test/resources")
    private val testOutput: File = File("target/test-output")

    private val kotlinModelWriter: ModelWriter = KotlinModelWriter()

    private val kotlinModelReader: ModelReader = KotlinModelReader()

    private val xmlModelReader: ModelReader = DefaultModelReader()

    private val xmlModelWriter: ModelWriter = DefaultModelWriter()

    protected val pomXml: File = testResources.resolve(testBasePath).resolve("pom.xml").also { assertThat(it).exists() }

    protected val pomKts: File = testResources.resolve(testBasePath).resolve("pom.kts").also { assertThat(it).exists() }

    protected fun File.xml2model(vararg options: Pair<String, Any?>): Model = xmlModelReader.read(this, mapOf(*options))

    protected fun File.kts2model(vararg options: Pair<String, Any?>): Model = kotlinModelReader.read(this, mapOf(*options))

    protected fun Model.model2xml(vararg options: Pair<String, Any?>): File {
        val basedir = testOutput.resolve(testBasePath)
        basedir.mkdirs()
        val file = basedir.resolve("pom.xml")
        xmlModelWriter.write(file, mapOf(*options), this)
        return file
    }

    protected fun Model.model2kts(vararg options: Pair<String, Any?>): File {
        val basedir = testOutput.resolve(testBasePath)
        basedir.mkdirs()
        val file = basedir.resolve("pom.kts")
        kotlinModelWriter.write(file, mapOf(*options), this)
        return file
    }

    protected fun assertEquals(expected: File, actual: File) {
        Assert.assertEquals(expected.readText(), actual.readText())
    }
}
