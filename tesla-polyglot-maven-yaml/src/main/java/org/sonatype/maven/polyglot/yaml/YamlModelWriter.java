/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.yaml;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.ModelWriter;
import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.maven.polyglot.io.ModelWriterSupport;
import org.yaml.snakeyaml.Dumper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tags;
import org.yaml.snakeyaml.representer.Representer;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * YAML model writer.
 *
 * @author jvanzyl
 * @author bentmann
 *
 * @since 0.7
 */
@Component(role = ModelWriter.class, hint = "yaml")
public class YamlModelWriter
    extends ModelWriterSupport
{
    public void write( Writer output, Map<String, Object> o, Model model )
        throws IOException
    {
        DumperOptions options = new DumperOptions();
        options.setExplicitRoot( Tags.MAP );
        options.setDefaultFlowStyle( FlowStyle.AUTO );
        options.setIndent( 2 );
        options.setWidth( 80 );

        Representer representer = new ModelRepresenter();

        Dumper dumper = new Dumper( representer, options );
        Yaml yaml = new Yaml( dumper );
        yaml.dump( model, output );
    }

}
