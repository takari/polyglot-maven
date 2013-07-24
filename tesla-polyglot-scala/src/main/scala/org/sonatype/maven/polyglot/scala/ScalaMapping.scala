/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.scala

import org.codehaus.plexus.component.annotations.Component
import org.sonatype.maven.polyglot.mapping.{Mapping, MappingSupport}

/**
 * Scala model mapping.
 * @since 0.7
 */
@Component(role=classOf[Mapping], hint="scala")
class ScalaMapping extends MappingSupport("scala") {
  setPomNames("pom.scala");
  setAcceptLocationExtensions(".scala");
  setAcceptOptionKeys("scala:4.0.0");
  setPriority(-3);
}