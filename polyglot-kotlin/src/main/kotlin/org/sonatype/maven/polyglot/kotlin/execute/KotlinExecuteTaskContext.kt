package org.sonatype.maven.polyglot.kotlin.execute

import org.apache.maven.execution.MavenSession
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import java.io.File
import javax.script.ScriptEngine

open class KotlinExecuteTaskContext(bindings: Map<String, Any?>) {

    val project: MavenProject = bindings["project"] as MavenProject
    val session: MavenSession = bindings["session"] as MavenSession
    val basedir: File = bindings["basedir"] as File
    val log: Log = bindings["log"] as Log
    val script: File = bindings["script"] as File
    val scriptEngine: ScriptEngine = bindings["kotlin.script.engine"] as ScriptEngine
}
