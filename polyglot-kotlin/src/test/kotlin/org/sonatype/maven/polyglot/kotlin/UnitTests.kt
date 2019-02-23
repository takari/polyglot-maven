package org.sonatype.maven.polyglot.kotlin

import org.assertj.core.api.Assertions
import org.junit.Test
import org.sonatype.maven.polyglot.kotlin.dsl.propertiesFactory
import org.sonatype.maven.polyglot.kotlin.testing.AbstractModelTestCase
import java.io.StringWriter

class UnitTests : AbstractModelTestCase() {

    //@formatter:off

    @Test fun `unit-tests#artifactId#variation-1`() = test()
    @Test fun `unit-tests#artifactId#variation-2`() = test()
    @Test fun `unit-tests#artifactId#variation-3`() = test()
    @Test fun `unit-tests#artifactId#variation-4`() = test()
    @Test fun `unit-tests#artifactId#variation-5`() = test()
    @Test fun `unit-tests#artifactId#variation-6`() = test()

    @Test fun `unit-tests#build#variation-1`() = test()
    @Test fun `unit-tests#build#variation-2`() = test()
    @Test fun `unit-tests#build#variation-3`() = test()
    @Test fun `unit-tests#build#variation-4`() = test()
    @Test fun `unit-tests#build#variation-5`() = test()
    @Test fun `unit-tests#build#variation-6`() = test()

    @Test fun `unit-tests#ciManagement#variation-1`() = test()
    @Test fun `unit-tests#ciManagement#variation-2`() = test()

    @Test fun `unit-tests#contributors#variation-1`() = test()
    @Test fun `unit-tests#contributors#variation-2`() = test()

    @Test fun `unit-tests#dependencies#variation-1`() = test()
    @Test fun `unit-tests#dependencies#variation-2`() = test()
    @Test fun `unit-tests#dependencies#variation-3`() = test()
    @Test fun `unit-tests#dependencies#variation-4`() = test()
    @Test fun `unit-tests#dependencies#variation-5`() = test()
    @Test fun `unit-tests#dependencies#variation-6`() = test()
    @Test fun `unit-tests#dependencies#variation-7`() = test()
    @Test fun `unit-tests#dependencies#variation-8`() = test()

    @Test fun `unit-tests#dependencyManagement#variation-1`() = test()
    @Test fun `unit-tests#dependencyManagement#variation-2`() = test()
    @Test fun `unit-tests#dependencyManagement#variation-3`() = test()

    @Test fun `unit-tests#description#variation-1`() = test()
    @Test fun `unit-tests#description#variation-2`() = test()
    @Test fun `unit-tests#description#variation-3`() = test()

    @Test fun `unit-tests#developers#variation-1`() = test()
    @Test fun `unit-tests#developers#variation-2`() = test()

    @Test fun `unit-tests#distributionManagement#variation-1`() = test()
    @Test fun `unit-tests#distributionManagement#variation-2`() = test()
    @Test fun `unit-tests#distributionManagement#variation-3`() = test()
    @Test fun `unit-tests#distributionManagement#variation-4`() = test()

    @Test fun `unit-tests#execute#variation-1`() = test()
    @Test fun `unit-tests#execute#variation-2`() = test()

    @Test fun `unit-tests#groupId#variation-1`() = test()
    @Test fun `unit-tests#groupId#variation-2`() = test()
    @Test fun `unit-tests#groupId#variation-3`() = test()
    @Test fun `unit-tests#groupId#variation-4`() = test()
    @Test fun `unit-tests#groupId#variation-5`() = test()
    @Test fun `unit-tests#groupId#variation-6`() = test()

    @Test fun `unit-tests#id#variation-1`() = test()
    @Test fun `unit-tests#id#variation-2`() = test()
    @Test fun `unit-tests#id#variation-3`() = test()
    @Test fun `unit-tests#id#variation-4`() = test()

    @Test fun `unit-tests#inceptionYear#variation-1`() = test()
    @Test fun `unit-tests#inceptionYear#variation-2`() = test()
    @Test fun `unit-tests#inceptionYear#variation-3`() = test()

    @Test fun `unit-tests#issueManagement#variation-1`() = test()
    @Test fun `unit-tests#issueManagement#variation-2`() = test()

    @Test fun `unit-tests#licenses#variation-1`() = test()
    @Test fun `unit-tests#licenses#variation-2`() = test()
    @Test fun `unit-tests#licenses#variation-3`() = test()

    @Test fun `unit-tests#mailingLists#variation-1`() = test()
    @Test fun `unit-tests#mailingLists#variation-2`() = test()
    @Test fun `unit-tests#mailingLists#variation-3`() = test()

    @Test fun `unit-tests#modelVersion#variation-1`() = test()
    @Test fun `unit-tests#modelVersion#variation-2`() = test()

    @Test fun `unit-tests#modules#variation-1`() = test()
    @Test fun `unit-tests#modules#variation-2`() = test()

    @Test fun `unit-tests#name#variation-1`() = test()
    @Test fun `unit-tests#name#variation-2`() = test()
    @Test fun `unit-tests#name#variation-3`() = test()

    @Test fun `unit-tests#organization#variation-1`() = test()
    @Test fun `unit-tests#organization#variation-2`() = test()
    @Test fun `unit-tests#organization#variation-3`() = test()

    @Test fun `unit-tests#packaging#variation-1`() = test()
    @Test fun `unit-tests#packaging#variation-2`() = test()
    @Test fun `unit-tests#packaging#variation-3`() = test()
    @Test fun `unit-tests#packaging#variation-4`() = test()
    @Test fun `unit-tests#packaging#variation-5`() = test()
    @Test fun `unit-tests#packaging#variation-6`() = test()

    @Test fun `unit-tests#parent#variation-1`() = test()
    @Test fun `unit-tests#parent#variation-2`() = test()
    @Test fun `unit-tests#parent#variation-3`() = test()
    @Test fun `unit-tests#parent#variation-4`() = test()
    @Test fun `unit-tests#parent#variation-5`() = test()
    @Test fun `unit-tests#parent#variation-6`() = test()

    @Test fun `unit-tests#pluginRepositories#variation-1`() = test()
    @Test fun `unit-tests#pluginRepositories#variation-2`() = test()
    @Test fun `unit-tests#pluginRepositories#variation-3`() = test()
    @Test fun `unit-tests#pluginRepositories#variation-4`() = test()

    @Test fun `unit-tests#prerequisites#variation-1`() = test()
    @Test fun `unit-tests#prerequisites#variation-2`() = test()

    @Test fun `unit-tests#profiles#variation-1`() = test()
    @Test fun `unit-tests#profiles#variation-2`() = test()

    @Test fun `unit-tests#properties#variation-1`() = test()
    @Test fun `unit-tests#properties#variation-2`() = test()

    @Test fun `unit-tests#reporting#variation-1`() = test()
    @Test fun `unit-tests#reporting#variation-2`() = test()
    @Test fun `unit-tests#reporting#variation-3`() = test()
    @Test fun `unit-tests#reporting#variation-4`() = test()

    @Test fun `unit-tests#reports#variation-1`() = test()
    @Test fun `unit-tests#reports#variation-2`() = test()
    @Test fun `unit-tests#reports#variation-3`() = test()
    @Test fun `unit-tests#reports#variation-4`() = test()
    @Test fun `unit-tests#reports#variation-5`() = test()

    @Test fun `unit-tests#repositories#variation-1`() = test()
    @Test fun `unit-tests#repositories#variation-2`() = test()
    @Test fun `unit-tests#repositories#variation-3`() = test()
    @Test fun `unit-tests#repositories#variation-4`() = test()

    @Test fun `unit-tests#scm#variation-1`() = test()
    @Test fun `unit-tests#scm#variation-2`() = test()

    @Test fun `unit-tests#url#variation-1`() = test()
    @Test fun `unit-tests#url#variation-2`() = test()

    @Test fun `unit-tests#version#variation-1`() = test()
    @Test fun `unit-tests#version#variation-2`() = test()
    @Test fun `unit-tests#version#variation-3`() = test()
    @Test fun `unit-tests#version#variation-4`() = test()
    @Test fun `unit-tests#version#variation-5`() = test()
    @Test fun `unit-tests#version#variation-6`() = test()

    // @formatter:on

    private fun test() {
        with(modelFromPomKts) {
            val modelXmlOutput = StringWriter()
            xmlModelWriter.write(modelXmlOutput, emptyMap(), this)
            Assertions.assertThat(modelXmlOutput.toString()).isEqualTo(pomXmlSource.readText())
        }
    }
}