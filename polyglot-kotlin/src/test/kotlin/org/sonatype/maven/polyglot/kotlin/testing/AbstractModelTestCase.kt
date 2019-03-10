package org.sonatype.maven.polyglot.kotlin.testing

import org.apache.maven.model.Model
import org.apache.maven.model.io.DefaultModelReader
import org.apache.maven.model.io.DefaultModelWriter
import org.apache.maven.model.io.ModelReader
import org.apache.maven.model.io.ModelWriter
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.sonatype.maven.polyglot.kotlin.KotlinModelReader
import org.sonatype.maven.polyglot.kotlin.KotlinModelWriter
import org.sonatype.maven.polyglot.kotlin.dsl.propertiesFactory
import java.io.Reader

abstract class AbstractModelTestCase : AbstractTestCase() {

    init {
        propertiesFactory = { org.sonatype.maven.polyglot.kotlin.util.Properties() }
    }

    protected val kotlinModelWriter: ModelWriter = KotlinModelWriter()

    protected val kotlinModelReader: ModelReader = KotlinModelReader()

    protected val xmlModelReader: ModelReader = DefaultModelReader()

    protected val xmlModelWriter: ModelWriter = DefaultModelWriter()

    protected val pomXmlSource: Reader
        get() {
            val resource = "${testName}/pom.xml".replace('#', '/').asResource()
            Assertions.assertThat(resource).isNotNull
            return resource!!
        }

    protected val pomKtsSource: Reader
        get() {
            val resource = "${testName}/pom.kts".replace('#', '/').asResource()
            Assertions.assertThat(resource).isNotNull
            return resource!!
        }

    protected val modelFromPomXml: Model
        get() {
            val model = xmlModelReader.read(pomXmlSource, mapOf<String, Any>())
            assertThat(model).isNotNull
            return model
        }

    protected val modelFromPomKts: Model
        get() {
            val model = kotlinModelReader.read(pomKtsSource, mapOf<String, Any>())
            assertThat(model).isNotNull
            return model
        }
}