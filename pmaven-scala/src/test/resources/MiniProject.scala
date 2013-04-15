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
