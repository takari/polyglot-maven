package org.sonatype.maven.polyglot.java.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class ConfiugrationXmlBuilder {
	
	public static void main(String[] args) {
		
		
		 ConfiugrationXmlBuilder xml = new ConfiugrationXmlBuilder();
		 Xpp3Dom xmldata = xml.startConfig()
		    .tag("version", versionTag -> versionTag.content("pojo.getVersion()"))
//		    .tag("time", timeTag -> timeTag.content("pojo.getTime()"))
		    .tag("data", dataTag -> {
		        dataTag.child("item", itemTag -> itemTag.attribute("id", "pojo.getId()"));
		        dataTag.child("time", timeTag -> timeTag.content("pojo.getId()"));
		    }).endConfig();
		  
		  System.out.println("=====");
		  System.out.println(xmldata);
		  System.out.println("=====");
	}

    XMLOutputFactory factory = createFactory();
    ByteArrayOutputStream outputStream;
    XMLStreamWriter writer;

    public ConfiugrationXmlBuilder() {
    	
    }

    protected XMLStreamWriter createWriter(OutputStream outputStream) {
        try {
            return factory.createXMLStreamWriter(outputStream);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    protected XMLOutputFactory createFactory() {
        return XMLOutputFactory.newInstance();
    }

    public ConfiugrationXmlBuilder startConfig() {
    	
    	this.outputStream = new ByteArrayOutputStream();
        this.writer = createWriter(outputStream);	
    	
        try {
            writer.writeStartDocument();
            
            writer.writeStartElement("configuration");
            
            return this;
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    public Xpp3Dom endConfig() {
        try {
        	
        	writer.writeEndElement();
        	
            writer.writeEndDocument();
            writer.flush();
            writer.close();         

            Xpp3Dom dom = null;
            try {
				dom = Xpp3DomBuilder.build(new InputStreamReader(new ByteArrayInputStream(outputStream.toByteArray())));
			} catch (XmlPullParserException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
            return dom;
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    public ConfiugrationXmlBuilder tag(String name, TagFunction scopeFunction) {
        try {
            writer.writeStartElement(name);
            scopeFunction.apply(new TagContext(name, this));
            writer.writeEndElement();
            return this;
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    public interface TagFunction {
        void apply(TagContext tagContext);
    }

    public class TagContext {
        String name;
        private ConfiugrationXmlBuilder xmlBuilder;

        public TagContext(String name, ConfiugrationXmlBuilder xmlBuilder) {
            this.name = name;
            this.xmlBuilder = xmlBuilder;
        }

        public String getName() {
            return name;
        }

        public TagContext attribute(String name, String value) {
            try {
                writer.writeAttribute(name, value);
            } catch (XMLStreamException e) {
                throw new RuntimeException(e);
            }
            return this;
        }

        public TagContext content(Object content) {
            try {
                writer.writeCharacters(content.toString());
            } catch (XMLStreamException e) {
                throw new RuntimeException(e);
            }
            return this;
        }

        public ConfiugrationXmlBuilder child(String name, TagFunction scopeFunction) {
            return xmlBuilder.tag(name, scopeFunction);
        }

        public TagContext child(String name) {
            try {
                writer.writeEmptyElement(name);
                return this;
            } catch (XMLStreamException e) {
                throw new RuntimeException(e);
            }
        }

        public TagContext cdata(Object content) {
            try {
                writer.writeCData(content.toString());
                return this;
            } catch (XMLStreamException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
