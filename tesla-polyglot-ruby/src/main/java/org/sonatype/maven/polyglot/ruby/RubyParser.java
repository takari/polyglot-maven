package org.sonatype.maven.polyglot.ruby;

import java.io.IOException;

import org.apache.maven.model.Model;
import org.apache.maven.model.building.ModelSource;
import org.sonatype.maven.polyglot.execute.ExecuteManager;
import org.sonatype.maven.polyglot.ruby.execute.RubyExecuteTaskFactory;

import de.saumya.mojo.ruby.GemScriptingContainer;

/**
 * Parses the ruby into a Maven model.
 * 
 * @author kristian
 */
public class RubyParser {

    private final GemScriptingContainer jruby;

    private final Object parser;

    private final ExecuteManager executeManager;

    private final RubyExecuteTaskFactory factory;
    
    public RubyParser(ModelSource modelSource, ExecuteManager executeManager) throws IOException {
        // TODO something with that modelSource, i.e. when errors occurs
        this.executeManager = executeManager;
        this.jruby = new GemScriptingContainer();
        this.parser = this.jruby.runScriptletFromClassloader("parser.rb");
        this.factory = new RubyExecuteTaskFactory(jruby);
    }

    // synchronize it since it is not clear how threadsafe all is
    public synchronized Model parse(String ruby) {
        Model model = this.jruby.callMethod(this.parser, 
                    "parse", 
                    new Object[] {ruby, this.factory}, 
                    Model.class);
//        model.setModelVersion("4.0.0");
        executeManager.register(model, this.factory.getExecuteTasks());
        return model;
    }
}
