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
import org.sonatype.maven.polyglot.execute.ExecuteManager;
import org.sonatype.maven.polyglot.io.ModelReaderSupport;

/**
 * Reads a <tt>pom.rb</tt> and transforms into a Maven {@link Model}.
 *
 * @author m.kristian
 */
@Component(role = ModelReader.class, hint="ruby")
public class RubyModelReader extends ModelReaderSupport {

    @Requirement
    ExecuteManager executeManager;
    
    @Requirement( hint = "ruby" )
    SetupClassRealm setupManager;
    
    public Model read( final Reader input, final Map<String, ?> options )
            throws IOException {
        assert input != null;

        setupManager.setupArtifact( "io.tesla.polyglot:tesla-polyglot-ruby:0.0.1-SNAPSHOT" );
            
        
        // read the stream from our pom.rb into a String
        StringWriter ruby = new StringWriter();
        IOUtil.copy( input, ruby );

        // TODO String source = PolyglotModelUtil.getLocation( options );
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

        // parse the String and create a POM model
        return new RubyParser( executeManager ).parse( ruby.toString(), source );
    }
}
