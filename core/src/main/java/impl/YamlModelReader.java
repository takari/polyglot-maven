/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package impl;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.ModelParseException;
import org.apache.maven.model.io.ModelReader;
import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.maven.yaml.io.ModelReaderSupport;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

/**
 * YAML model reader.
 *
 * @author jvanzyl
 * @author bentmann
 *
 * @since 0.7
 */
@Component(role = ModelReader.class, hint = "yaml")
public class YamlModelReader extends ModelReaderSupport {
  private final Yaml yaml;

  public YamlModelReader() {
    ModelConstructor constructor = new ModelConstructor();
    yaml = new Yaml(constructor, new Representer(), new DumperOptions(), new ModelResolver());
  }

  public Model read(Reader input, Map<String, ?> options) throws IOException, ModelParseException {
    if (input == null) {
      throw new IllegalArgumentException("YAML Reader is null.");
    }

    return (Model) yaml.load(input);
  }
}
