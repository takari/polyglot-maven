/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.yaml;

import org.apache.maven.model.*;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

public class Issue44Test {

  @Test
  public void testModelReader() throws Exception {
    InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("issue44.yaml");
    assertNotNull(input);
    YamlModelReader modelReader = new YamlModelReader();
    Model model = modelReader.read(input, null);
    assertNotNull(model);
    Plugin plugin = model.getBuild().getPlugins().get(0);
    assertEquals("proguard-maven-plugin", plugin.getArtifactId());
    assertEquals("2.0.11", plugin.getVersion());
    Xpp3Dom conf =  (Xpp3Dom) plugin.getConfiguration();
    //TODO what should be hier ?
    assertEquals("[${java.home}/lib/rt.jar, ${java.home}/lib/jsse.jar]", conf.getChild("libs").getValue());
  }
}
