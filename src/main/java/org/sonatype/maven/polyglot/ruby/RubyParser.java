package org.sonatype.maven.polyglot.ruby;

import java.io.IOException;

import org.apache.maven.model.Model;
import org.apache.maven.model.building.ModelSource;

import de.saumya.mojo.ruby.GemScriptingContainer;

/**
 * Parses the ruby into a Maven model.
 * 
 * @author kristian
 */
public class RubyParser {

    private final GemScriptingContainer jruby;

    private final Object parser;

    public RubyParser(ModelSource modelSource) throws IOException {
        // TODO something with that modelSource, i.e. when errors occurs
        this.jruby = new GemScriptingContainer();
        this.parser = this.jruby.runScriptletFromClassloader("parser.rb");
    }

    // synchronize it since it is not clear how threadsafe all is
    public synchronized Model parse(String ruby) {
        return this.jruby.callMethod(parser, "parse", ruby, Model.class);
    }
}
