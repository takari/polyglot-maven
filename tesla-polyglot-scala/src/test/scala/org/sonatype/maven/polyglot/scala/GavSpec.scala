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
class GavSpec extends Specification {

  "The gav" should {
    "be expressed in short form with just the group and artifact" in {
      val ga = "somegroup" % "someartifact"
      ga.getClass.getSimpleName must_== "GroupArtifactId"
      ga.groupId must_== Some("somegroup")
      ga.artifactId must_== "someartifact"
    }
    "be expressed using the scala version convetion in the artifact" in {
      implicit val scalaVersion = ScalaVersion("2.10.2")
      val ga = "somegroup" %% "someartifact"
      ga.getClass.getSimpleName must_== "GroupArtifactId"
      ga.groupId must_== Some("somegroup")
      ga.artifactId must_== "someartifact_2.10"
    }
    "be expressed in long form" in {
      val gav = "somegroup" % "someartifact" % "someversion"
      gav.getClass.getSimpleName must_== "Gav"
      gav.groupId must_== Some("somegroup")
      gav.artifactId must_== "someartifact"
      gav.version must_== Some("someversion")
    }
    "be expressed in long form with just the group and artifact and an empty version" in {
      val gav = "somegroup" % "someartifact" % ""
      gav.getClass.getSimpleName must_== "Gav"
      gav.groupId must_== Some("somegroup")
      gav.artifactId must_== "someartifact"
      gav.version must_== None
    }
    "be expressed in short form with no group" in {
      val ga = "" % "someartifact"
      ga.getClass.getSimpleName must_== "GroupArtifactId"
      ga.groupId must_== None
      ga.artifactId must_== "someartifact"
    }
    "be expressed in constructor form with null groupId and null version" in {
      val gav = GroupArtifactId(GroupId(Option(null)), "someartifact")
      gav.groupId must_== None
      gav.artifactId must_== "someartifact"
    }
  }
}
