/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
import org.sonatype.maven.polyglot.scala.dsl._

val parent = Project(groupId = "testGroupId",
                     artifactId = "testParent")

Project(
  parent = parent,
  artifactId = "testArtifactId",
  dependencies = "x:y:1.0" ::
                 "a:b:2.0" ::
                 Nil
)
