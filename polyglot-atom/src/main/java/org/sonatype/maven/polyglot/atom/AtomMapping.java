/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.atom;

import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.maven.polyglot.mapping.Mapping;
import org.sonatype.maven.polyglot.mapping.MappingSupport;

/**
 * Atom compact grammar model mapping.
 *
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
@Component(role = Mapping.class, hint = "atom")
public class AtomMapping extends MappingSupport {
  public AtomMapping() {
    super("atom");
    setPomNames("pom.atom");
    setAcceptLocationExtensions(".atom");
    setAcceptOptionKeys("atom:4.0.0");
    setPriority(1);
  }
}