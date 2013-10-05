/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
import org.sonatype.maven.polyglot.scala.model._
 
implicit val scalaVersion = ScalaVersion("2.10.2")
 
ScalaModel(
  "" % "tesla-polyglot-scala",
  name = "Polyglot Tesla :: Scala",
  contributors = Seq(
    Contributor(
      name = "Christopher Hunt",
      organization = "Typesafe",
      organizationUrl = "http://typesafe.com"
    )
  ),
  parent = Parent("io.tesla.polyglot" % "tesla-polyglot" % "0.0.1-SNAPSHOT"),
  repositories = Seq(
    Repository(
      snapshots = RepositoryPolicy(enabled = false),
      id = "sonatype-public-grid",
      url = "http://repository.sonatype.org/content/groups/sonatype-public-grid/"
    )
  ),
  dependencies = Seq(
    "io.tesla.polyglot" % "tesla-polyglot-common" % "0.0.1-SNAPSHOT",
    "com.twitter" %% "util-eval" % "6.3.8",
    "com.googlecode.kiama" %% "kiama" % "1.5.1",
    "org.specs2" %% "specs2" % "2.1.1" % "test",
    "junit" % "junit" % "" % "test"
  ),
  build = Build(
    plugins = Seq(
      Plugin(
        "io.tesla.maven.plugins" % "tesla-license-plugin",
        configuration = Config(
          header = "../license-header.txt"
        )
      )
    ),
    tasks = Seq(Task("someTaskId", "verify") {
      ec => println("I'm Scala running during the verify phase. The ec passed in allows me to access the project")
    })
  ),
  modelVersion = "4.0.0"
)