/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.scala

import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import org.sonatype.maven.polyglot.scala.model._

@RunWith(classOf[JUnitRunner])
class DependencySpec extends Specification {

  "The dependency" should {
    "be expressed in short form given a Gav" in {
      val dep = "somegroup" % "someartifact" % "someversion" % "test"
      dep.getClass.getSimpleName must_== "Dependency"
      dep.gav.groupId must_== Some("somegroup")
      dep.gav.artifactId must_== "someartifact"
      dep.gav.version must_== Some("someversion")
      dep.scope must_== Some("test")
    }
    "be expressed in short form given just a Gav" in {
      val dep: Dependency = "somegroup" % "someartifact" % "someversion"
      dep.getClass.getSimpleName must_== "Dependency"
      dep.gav.groupId must_== Some("somegroup")
      dep.gav.artifactId must_== "someartifact"
      dep.gav.version must_== Some("someversion")
      dep.scope must_== None
    }
    "be expressed in short form given just a GroupArtifactId" in {
      val dep: Dependency = "somegroup" % "someartifact"
      dep.getClass.getSimpleName must_== "Dependency"
      dep.gav.groupId must_== Some("somegroup")
      dep.gav.artifactId must_== "someartifact"
      dep.gav.version must_== None
      dep.scope must_== None
    }
  }
}
