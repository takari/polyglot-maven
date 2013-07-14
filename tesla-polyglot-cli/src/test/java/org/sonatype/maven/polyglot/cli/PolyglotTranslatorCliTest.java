package org.sonatype.maven.polyglot.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.model.Model;
import org.apache.maven.model.building.ModelProcessor;
import org.apache.maven.model.io.DefaultModelWriter;
import org.apache.maven.model.io.ModelReader;
import org.apache.maven.model.io.ModelWriter;
import org.codehaus.plexus.PlexusTestCase;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.sonatype.maven.polyglot.PolyglotModelManager;

/**
 * Tests for {@link PolyglotTranslatorCli}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class PolyglotTranslatorCliTest
    extends PlexusTestCase
{
    private PolyglotModelManager manager;

    private PolyglotTranslatorCli translator;

    private ModelWriter writer = new DefaultModelWriter();

    @Before
    protected void setUp() throws Exception {
        manager = lookup(PolyglotModelManager.class);
        translator = new PolyglotTranslatorCli();
    }

    private void translate(String input, String ext, String expected)
        throws Exception {

        System.out.println( "Translating: " + input );
        System.out.println( ext );
        System.out.println( expected );

        URL url = getClass().getResource(input);
        assertNotNull(url);

        //println "Input url:\n${url}"
        //println "Input text:\n${url.text}"

        File file = File.createTempFile("pom", ext);
        file.deleteOnExit();
        try {
            translator.translate(url, file.toURI().toURL());
            //println "Translated text file url:\n ${file.toURI().toURL()}"
            //println "Translated text:\n${file.text}"

            url = getClass().getResource(expected);
            assertNotNull(url);

            Model expectedModel = loadModel(url.openStream(), expected);
            Model actualModel = loadModel(new FileInputStream(file), file.getName());

            assertModelEquals(expectedModel, actualModel);
        }
        finally {
            file.delete();
        }
    }

    private Model loadModel(final InputStream input, final String location) 
        throws Exception {
        System.out.println( location );
        Map<String,String> options = new HashMap<String,String>();
        options.put(ModelProcessor.SOURCE, location);
        ModelReader reader = manager.getReaderFor(options);
        return reader.read(input, options);
    }

    private void assertModelEquals(final Model expected, final Model actual) 
        throws Exception {
        //...strips all whitespace and canonicalizes XML documents...
        NoWhitespaceXMLCanonicalizer c11r = new NoWhitespaceXMLCanonicalizer();
        
        Writer swxml1 = new StringWriter();
        writer.write(swxml1, null, expected);
        String xml1 = c11r.transform(swxml1.toString());
        
        Writer swxml2 = new StringWriter();
        writer.write(swxml2, null, actual);
        String xml2 = c11r.transform(swxml2.toString());
        
        assertEquals(xml1, xml2);
    }

    @Test
    @Ignore // FIXME: This test is fundamentally broken
    public void testFormatInterchange() throws Exception {
        String[] formats = new String[]{
            "xml",
            //"groovy",
            "yml",
//          "scala",
//          "clj"
        };

        for ( String source : formats) {
            for ( String target : formats) {
                //println "Testing $source -> $target"
                translate("pom1." + source, "." + target, "pom1." + target );
            }
        }
    }
}
