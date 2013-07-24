/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.ruby;

import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.maven.polyglot.mapping.Mapping;
import org.sonatype.maven.polyglot.mapping.MappingSupport;

/**
 * Ruby model mapping.
 *
 * @author m.kristian
 */
@Component(role = Mapping.class, hint = "ruby")
public class RubyMapping extends MappingSupport {
  public RubyMapping() {
    super("ruby");
    setPomNames("pom.rb", "Mavenfile");
    setAcceptLocationExtensions(".rb", "Mavenfile");
    setAcceptOptionKeys("ruby:4.0.0");
    setPriority(1);
  }
}