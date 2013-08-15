import org.sonatype.maven.polyglot.scala.model._

Model(
  "io.tesla.polyglot" % "tesla-polyglot" % "0.0.1-SNAPSHOT",
  dependencies = Seq(
    "someGroupId" % "someArtifactId" % "someVersion",
    "someGroupId" % "someArtifactId" % "someVersion" % "test",
    "someGroupId" % "someArtifactId" % "" % "test"
  )
)
