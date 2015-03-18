/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.io;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.ModelParseException;
import org.apache.maven.model.io.ModelReader;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.IOUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

/**
 * Support for {@link ModelReader} implementations.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 0.7
 */
public abstract class ModelReaderSupport implements ModelReader {
  public Model read(final File input, final Map<String, ?> options) throws IOException {
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

  public Model read(final InputStream input, final Map<String, ?> options) throws IOException {
    return read(new InputStreamReader(input), options);
  }
}