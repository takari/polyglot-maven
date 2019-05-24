package org.sonatype.maven.polyglot.kotlin.dsl;

import org.sonatype.maven.polyglot.execute.ExecuteContext
import org.sonatype.maven.polyglot.kotlin.engine.ScriptHost
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
    fun ExecuteContext.eval(taskScript: File) {
        ScriptHost.eval(taskScript, this)
    }
}
