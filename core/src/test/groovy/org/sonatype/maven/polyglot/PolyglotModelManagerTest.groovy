/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.yaml

import org.apache.maven.model.io.DefaultModelReader
import org.codehaus.plexus.PlexusTestCase
import org.junit.Before
import org.junit.Test
import static org.apache.maven.model.building.ModelProcessor.SOURCE

/**
 * Tests for {@link org.sonatype.maven.yaml.PolyglotModelManager}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class PolyglotModelManagerTest
    extends PlexusTestCase
{
    private org.sonatype.maven.yaml.PolyglotModelManager manager

    @Before
    void setUp() {
        manager = lookup(org.sonatype.maven.yaml.PolyglotModelManager.class)
    }

    private void expectReader(def key, def value, def type) {
        def options = [:]
        options.put(key, value)

        def reader = manager.getReaderFor(options)
        assertNotNull(reader)
        assertEquals(type, reader.getClass())
    }

    @Test
    void testAcceptLocationXml() {
        expectReader(SOURCE, "foo.xml", DefaultModelReader)
    }

    @Test
    void testAcceptLocationPom() {
        expectReader(SOURCE, "foo.pom", DefaultModelReader)
    }

    @Test
    void testAcceptKeyXml() {
        expectReader("xml:4.0.0", "xml:4.0.0", DefaultModelReader)
    }
}
