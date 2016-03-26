/*
 * Copyright (c) 2015 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

import org.sonatype.maven.polyglot.scala.model._
import scala.collection.immutable.Seq

Model(
  "io.tesla.polyglot" % "tesla-polyglot" % "0.0.1-SNAPSHOT",
  dependencies = Seq(
    "someGroupId" % "someArtifactId" % "someVersion",
    "someGroupId" % "someArtifactId" % "someVersion" % "test",
    "someGroupId" % "someArtifactId" % "" % "test"
  )
)
