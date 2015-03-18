/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.mapping;

import org.apache.maven.model.io.ModelReader;
import org.apache.maven.model.io.ModelWriter;

import java.io.File;
import java.util.Map;

/**
 * Provides a mapping to polyglot specific models.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 0.7
 */
public interface Mapping {
  File locatePom(File dir);

  boolean accept(Map<String, ?> options);

  ModelReader getReader();

  ModelWriter getWriter();

  float getPriority();

  String getFlavour();
}