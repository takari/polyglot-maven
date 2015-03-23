import org.sonatype.maven.polyglot.scala.model._
import scala.collection.immutable.Seq

Model(
  "someGroupId" % "someArtifactId" % "someVersion",
  dependencies = Seq(
    "org.scala-lang" % "scala-library" % "2.10.2"
  ),
  build = Build(
    sourceDirectory = "src/main/scala",
    testSourceDirectory = "src/test/scala",
    pluginManagement = PluginManagement(
      plugins = Seq(
        Plugin(
          "org.eclipse.m2e" % "lifecycle-mapping" % "1.0.0",
          configuration = Config(
            lifecycleMappingMetadata = Config(
              pluginExecutions = Config(
                pluginExecution = Config(
                  pluginExecutionFilter = Config(
                    groupId = "net.alchim31.maven",
                    artifactId = "scala-maven-plugin",
                    versionRange = "[3.2.0,)",
                    goals = Config(
                      goal = "add-source",
                      goal = "compile",
                      goal = "testCompile"
                    )
                  ),
                  action = Config(
                    ignore = None
                  )
                )
              )
            )
          )
        )
      )
    ),
    plugins = Seq(
      Plugin(
        "org.apache.maven.plugins" % "maven-compiler-plugin" % "3.2",
        executions = Seq(
          Execution(
            id = "default-compile",
            phase = "none"
          )
        )
      ),
      Plugin(
        "net.alchim31.maven" % "scala-maven-plugin" % "3.2.0",
        executions = Seq(
          Execution(
            goals = Seq(
              "compile",
              "testCompile"
            ),
            configuration = Config(
              args = Config(
                arg = "-deprecation"
              ),
              checkMultipleScalaVersions = "false",
              recompileMode = "incremental"
            )
          )
        )
      ),
      Plugin(
        "org.apache.maven.plugins" % "maven-surefire-plugin" % "2.18.1",
        configuration = Config(
          includes = Config(
            include = "%regex[.*Spec.*]"
          )
        )
      )
    )
  )
)
