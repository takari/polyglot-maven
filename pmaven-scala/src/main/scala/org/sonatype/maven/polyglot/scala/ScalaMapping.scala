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