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
// */

package org.sonatype.maven.polyglot.atom;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.DefaultModelWriter;
import org.apache.maven.model.io.ModelWriter;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.sonatype.maven.polyglot.io.ModelWriterSupport;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * TODO fix!
 */
@Component(role=ModelWriter.class, hint="atom")
public class AtomModelWriter extends ModelWriterSupport {
    @Requirement
    protected Logger log;
    
    public void write(final Writer output, final Map<String,Object> options, final Model model)
        throws IOException {
        assert output != null;
        assert model != null;

        StringWriter buff = new StringWriter();
        DefaultModelWriter writer = new DefaultModelWriter();
        writer.write(buff, options, model);

//        Dom2Groovy converter = new Dom2Groovy(new IndentPrinter(new PrintWriter(output), "    "));

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(buff.toString())));

            Element root = doc.getDocumentElement();
            NamedNodeMap attrs = root.getAttributes();
            for (int i=0; i<attrs.getLength(); i++) {
                Attr attr = (Attr)attrs.item(i);
                root.removeAttribute(attr.getName());
            }
            // Not sure where this comes from but the above will not nuke it
            root.removeAttribute("xmlns:xsi");
            
//            converter.print(doc);
            output.flush();
        }
        catch (ParserConfigurationException e) {
            throw (IOException) new IOException().initCause( e );
        }
        catch (SAXException e) {
            throw (IOException) new IOException().initCause( e );
        }
    }
}