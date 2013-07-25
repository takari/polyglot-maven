/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.ruby;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.maven.model.Model;
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

    public RubyParser( ExecuteManager executeManager ) throws IOException {
        this.executeManager = executeManager;
        this.jruby = new GemScriptingContainer();
        InputStream script = getClass().getClassLoader().getResourceAsStream("parser.rb");
        if ( script != null ){
            this.parser = this.jruby.runScriptlet( script, "parser.rb");
        }
        else {
            this.parser = this.jruby.runScriptletFromClassloader("parser.rb");
        }
        this.factory = new RubyExecuteTaskFactory(jruby);
    }

    // synchronize it since it is not clear how threadsafe all is
    public synchronized Model parse( String ruby, File source ) {
        Model model = this.jruby.callMethod( this.parser,
                    "parse",
                    new Object[] { ruby,
                                   this.factory,
                                   source != null ? source.getAbsolutePath() : null },
                    Model.class );
        executeManager.register( model, this.factory.getExecuteTasks() );
        executeManager.install( model );
        return model;
    }
}
