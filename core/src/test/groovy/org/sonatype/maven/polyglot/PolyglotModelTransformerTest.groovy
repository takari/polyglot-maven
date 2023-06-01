/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.yaml

import org.codehaus.plexus.PlexusTestCase
import org.junit.Before
import org.junit.Test
import static org.apache.maven.model.building.ModelProcessor.SOURCE

/**
 * Tests for {@link PolyglotModelTranslator}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class PolyglotModelTranslatorTest
    extends PlexusTestCase
{
    private PolyglotModelTranslator translator

    @Before
    void setUp() {
        translator = lookup(PolyglotModelTranslator.class)
    }

    private String translate(final String input, final String output) {
        assertNotNull(input)
        assertNotNull(output)

        def url = getClass().getResource(input)
        assertNotNull(url)
        def inputOptions = [:]
        inputOptions.put(SOURCE, url.path)

        def buff = new StringWriter()
        def outputOptions = [:]
        outputOptions.put(SOURCE, output)

        translator.translate(url.newReader(), inputOptions, buff, outputOptions)

        return buff.toString()
    }

    @Test
    void testXml2Xml() {
        def text = translate("pom1.xml", "pom.xml")
        def expect = getClass().getResource("pom1.xml").text
        assertEqualsXml(expect, text)
    }

    private void assertEqualsXml( String expected, String actual )
    {
        // TODO: Use XmlUnit
        def text = actual.replaceAll( "(\r\n)|(\r)|(\n)", "\n" )
        def expect = expected.replaceAll( "(\r\n)|(\r)|(\n)", "\n" )
        assertEquals(expect, text)
    }

}
