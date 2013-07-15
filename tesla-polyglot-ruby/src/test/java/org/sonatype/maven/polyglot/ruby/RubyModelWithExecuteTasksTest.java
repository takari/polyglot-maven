package org.sonatype.maven.polyglot.ruby;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.eclipse.sisu.containers.InjectedTestCase;
import org.sonatype.maven.polyglot.execute.ExecuteContext;
import org.sonatype.maven.polyglot.execute.ExecuteManagerImpl;
import org.sonatype.maven.polyglot.execute.ExecuteTask;

public class RubyModelWithExecuteTasksTest extends InjectedTestCase {

    @Inject
    @Named("${basedir}/src/test/poms")
    private File poms;

    public void testRubyModelWriter() throws Exception {
        File pom = new File(poms, "pom-with-execute-tasks.rb");

        //
        // Read in the Ruby POM
        //
        RubyModelReader rubyModelReader = new RubyModelReader();
        rubyModelReader.executeManager = new ExecuteManagerImpl() {
            {
                log = new ConsoleLogger( Logger.LEVEL_INFO, "test" );
            }
        };
        
        PrintStream out = System.out;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bytes));
        try {
            
            final Model rubyModel = rubyModelReader.read(new FileReader(pom),
                    new HashMap<String, Object>());

            //
            // Test for fidelity
            //
            assertNotNull(rubyModel);

            ExecuteContext context = new ExecuteContext() {

                public MavenProject getProject() {
                    return new MavenProject(rubyModel);
                }
            };
            List<ExecuteTask> tasks = rubyModelReader.executeManager.getTasks(
                    rubyModel);

            tasks.get(0).execute(context);
            assertEquals( "Execute Ruby Tasks", bytes.toString() );

            bytes.reset();
            tasks.get(1).execute(context);
            assertEquals("com.example:ruby-pom:1.0-SNAPSHOT:pom", bytes.toString());

            bytes.reset();
            tasks.get(2).execute(context);
            assertEquals("#<Tesla::Parser ", 
                    bytes.toString().replaceFirst( "@.*$", "" ) );
            
        } finally {
            System.setOut(out);
        }
    }
}
