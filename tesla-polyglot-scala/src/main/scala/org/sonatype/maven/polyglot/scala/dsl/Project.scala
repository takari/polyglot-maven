/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
