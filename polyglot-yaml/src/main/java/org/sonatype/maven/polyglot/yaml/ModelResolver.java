package org.sonatype.maven.polyglot.yaml;

import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.resolver.Resolver;

import java.util.regex.Pattern;

/**
 * POM resolver does not need to resolve implicit scalar types as defined in
 * http://www.yaml.org/type/
 */
public class ModelResolver extends Resolver {
    public static final Pattern POM_NULL = Pattern.compile("^(?:null| )$");

    @Override
    protected void addImplicitResolvers() {
        addImplicitResolver(Tag.MERGE, MERGE, "<");
        addImplicitResolver(Tag.NULL, POM_NULL, "~n\0");
        addImplicitResolver(Tag.NULL, EMPTY, null);
    }
}
