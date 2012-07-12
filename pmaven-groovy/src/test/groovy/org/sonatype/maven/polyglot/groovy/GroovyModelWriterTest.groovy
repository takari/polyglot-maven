/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.groovy

import org.apache.maven.model.Model
import org.apache.maven.model.Parent
import org.junit.Before
import org.junit.Test

import org.apache.maven.model.io.ModelWriter

/**
 * Tests for {@link org.sonatype.maven.polyglot.groovy.GroovyModelWriter}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class GroovyModelWriterTest
    extends GroovyModelTestSupport
{
    private GroovyModelWriter writer

    @Before
    void setUp() {
        writer = lookup(ModelWriter.class, "groovy")
    }

    @Test
    void testWriting() {
        def model = new Model(parent: new Parent(groupId: "a", artifactId: "b", version: "c"))

        def buff = new StringWriter()
        writer.write(buff, null, model)

        println(buff)

        def expected = load("test2.groovy")
        assertEqualsGroovy(expected, buff.toString())
    }

    private void assertEqualsGroovy( String expected, String actual ) {
        def text = actual.replaceAll( "(\r\n)|(\r)|(\n)", "\n" )
        def expect = expected.replaceAll( "(\r\n)|(\r)|(\n)", "\n" )
        assertEquals(expect, text)
    }
}