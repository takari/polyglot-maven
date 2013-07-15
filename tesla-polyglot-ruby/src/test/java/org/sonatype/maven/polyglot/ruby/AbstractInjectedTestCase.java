package org.sonatype.maven.polyglot.ruby;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.ModelWriter;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.eclipse.sisu.containers.InjectedTestCase;
import org.sonatype.maven.polyglot.execute.ExecuteManagerImpl;

public abstract class AbstractInjectedTestCase extends InjectedTestCase {

	@Inject
	@Named("${basedir}/src/test/poms")
	protected File poms;

	protected void assertRoundtrip( String pomName, boolean debug ) throws Exception {
	    File pom = new File( poms, pomName );
	    MavenXpp3Reader xmlModelReader = new MavenXpp3Reader();
	    Model xmlModel = xmlModelReader.read(new FileInputStream( pom ));
	    
	    //
	    // Write out the Ruby POM
	    //
	    ModelWriter writer = new RubyModelWriter();
	    StringWriter w = new StringWriter();
	    writer.write( w, new HashMap<String, Object>(), xmlModel );
	
	    if ( debug ){
	    	// Let's take a look at see what's there
	    	System.out.println(w.toString());
	    }
	    
	    //
	    // Read in the Ruby POM
	    //
	    RubyModelReader rubyModelReader = new RubyModelReader();
		rubyModelReader.executeManager = new ExecuteManagerImpl() {
			{
				log = new ConsoleLogger( Logger.LEVEL_INFO, "test" );
			}
		};
	    StringReader reader = new StringReader( w.toString() );
	    Model rubyModel = rubyModelReader.read( reader, new HashMap<String, Object>() );
	    
	    //
	    // Test for fidelity
	    //
	    assertNotNull( rubyModel );
	
	    assertRoundtrip( xmlModel, rubyModel, debug );
	}

	private void assertRoundtrip( Model xmlModel, Model rubyModel, boolean debug )
			throws IOException {
		MavenXpp3Writer xmlWriter = new MavenXpp3Writer();
	    StringWriter ruby = new StringWriter();
	    xmlWriter.write(ruby, rubyModel);
	    StringWriter xml = new StringWriter();
	    xmlWriter.write(xml, xmlModel);
	    
	    if ( debug ) {
	    	// Let's take a look at see what's there
	    	System.out.println(ruby.toString());
	    	System.out.println(xml.toString());
	    }
	    
	    assertEquals( simplify( xml ), simplify( ruby ) );
	}
	
	private String simplify(StringWriter xml) {
		String x = xml.toString().replaceAll( "\\s", "").replaceFirst("<properties>.*</properties>", "").replaceFirst("<\\?.*\\?>", "");
		return x;
	}	
}
