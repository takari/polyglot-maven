package org.sonatype.maven.polyglot.kotlin.execute

import org.apache.maven.execution.MavenSession
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import java.io.File

open class BuildContext(
    val project: MavenProject,
    val session: MavenSession,
    val log: Log,
    val basedir: File,
    val script: File
)
