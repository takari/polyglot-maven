/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Map;

import org.apache.maven.model.Model;
import org.apache.maven.model.building.FileModelSource;
import org.apache.maven.model.building.ModelProcessor;
import org.apache.maven.model.building.ModelSource;
import org.apache.maven.model.io.ModelParseException;
import org.apache.maven.model.io.ModelReader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;

/**
 * Polyglot model processor.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 0.7
 */
//@Component(role = ModelProcessor.class, hint = "tesla-polyglot")
@Component(role = ModelProcessor.class)
public class TeslaModelProcessor implements ModelProcessor {

  private static final String NEW_LINE = System.getProperty("line.separator");
  private static final String WARNING = "?>" + NEW_LINE + "<!--" +
      NEW_LINE + "" +
      NEW_LINE + "" +
      NEW_LINE + "DO NOT MODIFIY - GENERATED CODE" +
      NEW_LINE + "" +
      NEW_LINE + "" +
      NEW_LINE + "-->";
        
  @Requirement
  private PolyglotModelManager manager;
  @Requirement
  private Logger log;

  @Override
  public File locatePom(final File dir) {
    assert manager != null;

    File pomFile = manager.locatePom(dir);
    if (pomFile != null && !pomFile.getName().endsWith(".pom") && !pomFile.getName().endsWith(".xml")) {
      pomFile = new File(pomFile.getParentFile(), ".polyglot." + pomFile.getName());
      try {
        pomFile.createNewFile();
        pomFile.deleteOnExit();
      } catch (IOException e) {
        throw new RuntimeException("error creating empty file", e);
      }
    } else {
      // behave like proper maven in case there is no pom from manager
      pomFile = new File(dir, "pom.xml");
    }

    return pomFile;
  }

  @Override
  public Model read(final File input, final Map<String, ?> options) throws IOException, ModelParseException {
    Model model;

    Reader reader = new BufferedReader(new FileReader(input));
    try {
      model = read(reader, options);
      model.setPomFile(input);
    } finally {
      IOUtil.close(reader);
    }
    return model;
  }

  @Override
  public Model read(final InputStream input, final Map<String, ?> options) throws IOException, ModelParseException {
    return read(new InputStreamReader(input), options);
  }

  @Override
  @SuppressWarnings({
      "unchecked", "rawtypes"
  })
  public Model read(final Reader input, final Map<String, ?> options) throws IOException, ModelParseException {
    assert manager != null;
    ModelSource source = (ModelSource) options.get(ModelProcessor.SOURCE);
    if (("" + source).contains(".polyglot.")) {
      log.debug(source.getLocation());

      File pom = new File(source.getLocation());
      source = new FileModelSource(new File(pom.getPath().replaceFirst("[.]polyglot[.]", "")));

      ((Map) options).put(ModelProcessor.SOURCE, source);

      ModelReader reader = manager.getReaderFor(options);
      Model model = reader.read(source.getInputStream(), options);

      MavenXpp3Writer xmlWriter = new MavenXpp3Writer();
      StringWriter xml = new StringWriter();
      xmlWriter.write(xml, model);

      FileUtils.fileWrite(pom, xml.toString());

      // dump pom if filename is given via the pom properties
      String dump = model.getProperties().getProperty("polyglot.dump.pom");
      if (dump == null) {
        // just nice to dump the pom.xml via commandline switch
        dump = System.getProperty("polyglot.dump.pom");
      }
      if (dump != null) {
        File dumpPom = new File(pom.getParentFile(), dump);
        if (!dumpPom.exists() || !FileUtils.fileRead(dumpPom).equals(xml.toString().replace("?>", WARNING))) {
          dumpPom.setWritable(true);
          FileUtils.fileWrite(dumpPom, xml.toString().replace("?>", WARNING));
          if ("true".equals(model.getProperties().getProperty("polyglot.dump.readonly"))) {
            dumpPom.setReadOnly();
          }
        }
      }

      model.setPomFile(pom);
      return model;
    } else {
      ModelReader reader = manager.getReaderFor(options);
      return reader.read(input, options);
    }
  }
}
