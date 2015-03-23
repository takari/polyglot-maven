import org.sonatype.maven.polyglot.scala.model._
import scala.collection.immutable.Seq

Model(
  "io.tesla.polyglot" % "tesla-polyglot" % "0.0.1-SNAPSHOT",
  build = Build(
    tasks = Seq(
      Task("someId", "compile") {
        ec => ec.getProject.setArtifactId("We executed!")
      }
    )
  )
)
