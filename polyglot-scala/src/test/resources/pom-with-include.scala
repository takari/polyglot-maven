import org.sonatype.maven.polyglot.scala.model._
import scala.collection.immutable.Seq

//#include include.scala

Model(
  "io.tesla.polyglot" % "tesla-polyglot" % includedVersion,
  name = "Include Test"
)
