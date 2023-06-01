/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.yaml;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.model.io.ModelReader;
import org.apache.maven.model.io.ModelWriter;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.sonatype.maven.yaml.mapping.Mapping;

/**
 * Manages the mapping for polyglot model support.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 0.7
 */
@Component(role = PolyglotModelManager.class)
public class PolyglotModelManager {
  @Requirement
  protected Logger log;

  @Requirement(role = Mapping.class)
  protected List<Mapping> mappings;

  public void addMapping(final Mapping mapping) {
    assert mapping != null;
    mappings.add(mapping);
  }
  
  private static final Comparator<Mapping> DESCENDING_PRIORITY = Comparator.comparingDouble(Mapping::getPriority).reversed();

  public List<Mapping> getSortedMappings() {
    List<Mapping> sortedMappings = new ArrayList<>(mappings);
    sortedMappings.sort(DESCENDING_PRIORITY);
  return sortedMappings;
  }

  public ModelReader getYamlReader(final Map<String, ?> options) {
    for (Mapping mapping : getSortedMappings()) {
      if (mapping.accept(options)) {
        return mapping.getReader();
      }
    }

    throw new RuntimeException("Unable to determine model input format; options=" + options);
  }

  public ModelReader getReaderFor(final Map<String, ?> options) {	 
    for (Mapping mapping : getSortedMappings()) {
      if (mapping.accept(options)) {
        return mapping.getReader();
      }
    }

    throw new RuntimeException("Unable to determine model input format; options=" + options);
  }
  
  public Properties getEnhancementPropertiesFor(final Map<String, ?> options) {	 
    for (Mapping mapping : getSortedMappings()) {
      if (mapping.accept(options)) {
        return mapping.getEnhancementProperties(options);
      }
    }
    return null;
  }  

  public ModelWriter getWriterFor(final Map<String, ?> options) {
    for (Mapping mapping : getSortedMappings()) {
      if (mapping.accept(options)) {
        return mapping.getWriter();
      }
    }

    throw new RuntimeException("Unable to determine model output format; options=" + options);
  }

  public File findPom(final File dir) {
    assert dir != null;

    for (Mapping mapping : getSortedMappings()) {
      File file = mapping.locatePom(dir);
      if (file != null) {
        return file;
      }
    }
    return null;
  }

  public String determineFlavourFromPom(final File dir) {
    assert dir != null;
    for (Mapping mapping : getSortedMappings()) {
      File file = mapping.locatePom(dir);
      if (file != null) {
        return mapping.getFlavour();
      }
    }
    return null;
  }

  public String getFlavourFor(final Map<String, ?> options) {
    for (Mapping mapping : getSortedMappings()) {
      if (mapping.accept(options)) {
        return mapping.getFlavour();
      }
    }

    throw new RuntimeException("Unable determine model input format; options=" + options);
  }
}
