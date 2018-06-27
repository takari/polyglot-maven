import org.sonatype.maven.polyglot.scala.model._
import scala.collection.immutable.Seq

Model(
  "io.tesla.polyglot" % "tesla-polyglot" % "0.0.1-SNAPSHOT",
  dependencies = Seq(
    "someGroupId" % "someArtifactId" % "someVersion",
    "someGroupId" % "someArtifactId" % "someVersion" % "test",
    "someGroupId" % "someArtifactId" % "" % "test"
  ),
  build = Build(
    plugins = Seq(
      Plugin(
        "org.codehaus.mojo" % "exec-maven-plugin" % "1.1.1",
        configuration = Config(
          executable = "java",
          arguments = Config(
            argument = "-d64",
            argument = "-XstartOnFirstThread",
            argument = "-Duser.language=de",
            argument = "-Duser.region=DE",
            argument = "-classpath",
            classpath = None,
            argument = "foo.bar.Foo"
          )
        )
      )
    )
  ),
  modelVersion = "4.0.0"
)
