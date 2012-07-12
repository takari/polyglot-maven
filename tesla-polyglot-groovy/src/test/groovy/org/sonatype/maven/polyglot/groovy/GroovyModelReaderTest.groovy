/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.groovy

import org.apache.maven.model.building.ModelProcessor
import org.junit.Before
import org.junit.Test

import org.apache.maven.model.io.ModelReader

/**
 * Tests for {@link GroovyModelReader}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class GroovyModelReaderTest
    extends GroovyModelTestSupport
{
    private GroovyModelReader reader

    @Before
    void setUp() {
        reader = lookup(ModelReader.class, "groovy")
    }

    @Test
    void testReading() {
        def input = getClass().getResource("test1.groovy")
        assertNotNull(input)

        def options = [:]
        options.put(ModelProcessor.SOURCE, input)
        def model = reader.read(input.openStream(), options)
        assertNotNull(model)

        dump(model)

        def parent = model.parent
        assertNotNull(parent)
        
        assertEquals("a", parent.groupId)
        assertEquals("b", parent.artifactId)
        assertEquals("c", parent.version)
        assertEquals("../pom.xml", parent.relativePath)
    }

    @Test
    void testExecute() {
        def input = getClass().getResource("execute1.groovy")
        assertNotNull(input)

        def options = [:]
        options.put(ModelProcessor.SOURCE, input)
        def model = reader.read(input.openStream(), options)
        assertNotNull(model)

        dump(model)
    }
}
