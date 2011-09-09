/*
 * Copyright (C) 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sonatype.maven.polyglot;

import org.apache.maven.model.Model;
import org.apache.maven.model.building.ModelProcessor;
import org.apache.maven.model.io.ModelParseException;
import org.apache.maven.model.io.ModelReader;
import org.apache.maven.model.io.ModelWriter;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.ReaderFactory;
import org.sonatype.maven.polyglot.mapping.Mapping;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

/**
 * Polyglot model processor.
 * 
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * 
 * @since 0.7
 */
@Component(role = ModelProcessor.class, hint = "tesla")
public class TeslaModelProcessor implements ModelProcessor {

  @Requirement
  protected Logger log;
  
  @Requirement(role=Mapping.class)
  private List<Mapping> mappings;

  public Model read(final File input, final Map<String, ?> options) throws IOException, ModelParseException {
    Model model;

    Reader reader = new BufferedReader(ReaderFactory.newXmlReader(input));
    try {
      model = read(reader, options);
      model.setPomFile(input);
    } finally {
      IOUtil.close(reader);
    }
    return model;
  }

  public Model read(final InputStream input, final Map<String, ?> options) throws IOException, ModelParseException {
    return read(ReaderFactory.newXmlReader(input), options);
  }

  public Model read(final Reader input, final Map<String, ?> options) throws IOException, ModelParseException {
    ModelReader reader = getReaderFor(options);
    return reader.read(input, options);
  }

  public void addMapping(final Mapping mapping) {
    assert mapping != null;
    mappings.add(mapping);
  }

  public ModelReader getReaderFor(final Map<String, ?> options) {
    for (Mapping mapping : mappings) {
      if (mapping.accept(options)) {
        ModelReader reader = mapping.getReader();
        return reader;
      }
    }

    throw new RuntimeException("Unable determine model input format; options=" + options);
  }

  public ModelWriter getWriterFor(final Map<String, ?> options) {
    for (Mapping mapping : mappings) {
      if (mapping.accept(options)) {
        return mapping.getWriter();
      }
    }

    throw new RuntimeException("Unable determine model output format; options=" + options);
  }

  public File locatePom(final File dir) {
    assert dir != null;

    File pomFile = null;
    float mappingPriority = Float.MIN_VALUE;
    for (Mapping mapping : mappings) {
      File file = mapping.locatePom(dir);
      if (file != null && (pomFile == null || mappingPriority < mapping.getPriority())) {
        pomFile = file;
        mappingPriority = mapping.getPriority();
      }
    }

    if  (pomFile == null) {
      pomFile = new File(dir, "pom.xml");
    }
    
    return pomFile;
  }
}
