package org.sonatype.maven.polyglot.kotlin.dsl;

import org.apache.maven.execution.MavenSession
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import org.sonatype.maven.polyglot.kotlin.engine.ScriptHost
import org.sonatype.maven.polyglot.kotlin.engine.ScriptType
import org.sonatype.maven.polyglot.kotlin.execute.BuildContext
import java.io.File

class DSL(val script: File, val project: Project) {

    /**
     * Configures a Maven project model.
     */
    @PomDsl
    fun project(nameOrId: String? = null, block: Project.() -> Unit): Project {
        if (nameOrId != null) {
            project.name = nameOrId
            project.id = nameOrId
        }
        return project.apply(block)
    }

    /**
     * Invokes the script at the supplied location
     */
    @Suppress("unused")
    fun eval(taskScript: File, project: MavenProject, session: MavenSession, log: Log, basedir: File, script: File
    ) {
        ScriptHost.eval(taskScript, ScriptType.TASK, BuildContext(project, session, log, basedir, script))
    }
}
