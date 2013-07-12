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

package org.sonatype.maven.polyglot.ruby;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Map;

import org.apache.maven.model.Model;
import org.apache.maven.model.building.ModelProcessor;
import org.apache.maven.model.building.ModelSource;
import org.apache.maven.model.io.ModelReader;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.util.IOUtil;
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
    return new RubyParser(( ModelSource)options.get( ModelProcessor.SOURCE ), executeManager )
    	.parse( ruby.toString() );
  }
}
