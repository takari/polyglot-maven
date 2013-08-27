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
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Map;

import org.apache.maven.model.Model;
import org.apache.maven.model.building.ModelProcessor;
import org.apache.maven.model.building.ModelSource;
import org.apache.maven.model.io.ModelReader;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.util.IOUtil;
import org.sonatype.maven.polyglot.PolyglotModelUtil;
import org.sonatype.maven.polyglot.execute.ExecuteManager;
import org.sonatype.maven.polyglot.execute.ExecuteManagerImpl;
import org.sonatype.maven.polyglot.io.ModelReaderSupport;

/**
 * Reads a <tt>pom.rb</tt> and transforms into a Maven {@link Model}.
 *
 * @author m.kristian
 */
@Component(role = ModelReader.class,hint="ruby")
public class RubyModelReader extends ModelReaderSupport {

    @Requirement
    ExecuteManager executeManager = new ExecuteManagerImpl();

    public Model read( final Reader input, final Map<String, ?> options ) throws IOException {
        assert input != null;

        // read the stream from our pom.rb into a String
        StringWriter ruby = new StringWriter();
        IOUtil.copy( input, ruby );
        // parse the String and create a POM model

        //String src = PolyglotModelUtil.getLocation( options );
        //final File source = src == null ? null : new File( src );
        Object src = options.get( ModelProcessor.SOURCE );
        final File source;
        if ( src instanceof URL )
        {
            source = new File( ( (URL) src ).getFile() );
        }
        else if( src != null )
        {
            ModelSource sm = (ModelSource) src;
            source = new File(  sm.getLocation() );
        }
        else
        {
            source = null;
        }

        return new RubyParser( executeManager ).parse( ruby.toString(), source );
    }
}
