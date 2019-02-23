package org.sonatype.maven.polyglot.kotlin.execute

import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import org.sonatype.maven.polyglot.execute.ExecuteContext
import java.io.File

class ScriptExecutionContext(
    private val context: ExecuteContext
) {
    val log: Log
        get() = context.log()

    val project: MavenProject
        get() = context.project

    val basedir: File
        get() = context.basedir()
}
