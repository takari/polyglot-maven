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
 
package org.sonatype.maven.polyglot.scala.dsl

import scala.collection.Traversable

case class Project(parent: Option[Project] = None,
                   groupId: Option[String] = None,
                   artifactId: String,
                   version: Option[String] = Some("1.0-SNAPSHOT"),
                   dependencies: Traversable[Dependency] = Nil) {

  require(parent != null, "parent must not be null!")
  require(groupId != null, "groupId must not be null!")
  require(parent.isDefined || groupId.isDefined, "Either parent or groupId must be defined!")
  // TODO Further percondition checks
}

case class Dependency(groupId: String,
                      artifactId: String,
                      version: String,
                      scope: Scope = CompileScope) {

  require(groupId != null, "groupId must not be null!")
  // TODO Further percondition checks
}

abstract sealed class Scope
case object CompileScope extends Scope
case object ProvidedScope extends Scope
// TODO Complete list of scopes
