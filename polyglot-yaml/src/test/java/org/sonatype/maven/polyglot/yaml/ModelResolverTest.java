/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.yaml;

import org.junit.Test;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.Tag;

import static org.junit.Assert.assertEquals;

public class ModelResolverTest {

    @Test
    public void testModelNullResolver() throws Exception {
        ModelResolver resolver = new ModelResolver();
        assertEquals(Tag.STR, resolver.resolve(NodeId.scalar, "Null", true));
        assertEquals(Tag.STR, resolver.resolve(NodeId.scalar, "NULL", true));
        assertEquals(Tag.NULL, resolver.resolve(NodeId.scalar, "null", true));
        assertEquals(Tag.NULL, resolver.resolve(NodeId.scalar, " ", true));
        assertEquals(Tag.NULL, resolver.resolve(NodeId.scalar, "", true));
    }

    @Test
    public void testModelResolver() throws Exception {
        ModelResolver resolver = new ModelResolver();
        assertEquals(Tag.STR, resolver.resolve(NodeId.scalar, "8", true));
        assertEquals(Tag.STR, resolver.resolve(NodeId.scalar, "3.1", true));
        assertEquals(Tag.STR, resolver.resolve(NodeId.scalar, "true", true));
        assertEquals(Tag.STR, resolver.resolve(NodeId.scalar, "false", true));
    }
}