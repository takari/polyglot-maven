/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package impl;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.ModelWriter;
import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.maven.yaml.io.ModelWriterSupport;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.emitter.Emitter;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.serializer.Serializer;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * YAML model writer.
 *
 * @author jvanzyl
 * @author bentmann
 * @since 0.7
 */
@Component(role = ModelWriter.class, hint = "yaml")
public class YamlModelWriter extends ModelWriterSupport {
  public void write(Writer output, Map<String, Object> o, Model model) throws IOException {
    //TODO improve SnakeYAML API (A. Somov)
    DumperOptions dumperOptions = new DumperOptions();
    dumperOptions.setIndent(2);
    dumperOptions.setWidth(80);
    Serializer serializer = new Serializer(new Emitter(output, dumperOptions), new ModelResolver(), dumperOptions, Tag.MAP);
    Representer representer = new ModelRepresenter();
    try {
      serializer.open();
      Node node = representer.represent(model);
      serializer.serialize(node);
      serializer.close();
    } catch (IOException e) {
      throw new YAMLException(e);
    }
  }
}
