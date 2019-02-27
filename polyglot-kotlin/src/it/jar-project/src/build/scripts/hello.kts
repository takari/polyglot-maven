val project = bindings["project"] as org.apache.maven.project.MavenProject
val session = bindings["session"] as org.apache.maven.execution.MavenSession
val basedir = bindings["basedir"] as java.io.File
val log = bindings["log"] as org.apache.maven.plugin.logging.Log
val script = bindings["script"] as java.io.File
val keys = bindings.keys as Collection<String>

"""
    ------------------------------------------------------------------------

    HELLO ${project.name}!!!

    Folder:   ${basedir}
    Script:   .${script.path.substringAfter(basedir.path)}
    Bindings: ${keys}

    ------------------------------------------------------------------------
""".trimIndent().lines().forEach { log.info(it) }
