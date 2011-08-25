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