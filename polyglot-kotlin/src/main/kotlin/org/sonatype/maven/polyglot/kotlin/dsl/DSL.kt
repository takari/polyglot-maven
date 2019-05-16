package org.sonatype.maven.polyglot.kotlin.dsl

import org.sonatype.maven.polyglot.execute.ExecuteContext
import org.sonatype.maven.polyglot.kotlin.engine.PomKtsScriptHost
import org.sonatype.maven.polyglot.kotlin.execute.KotlinExecuteTask
import java.io.File

class DSL(val script: File, val project: Project) {

    @Suppress("unused")
    val basedir: File = script.parentFile

    /**
     * Configures a Maven project model.
     */
    @PomDsl
    fun project(nameOrId: String? = null, block: Project.() -> Unit): Project {
        if (nameOrId != null) {
            project.name = nameOrId
            project.id(nameOrId)
        }
        return project.apply(block)
    }

    /**
     * Invokes the script at the supplied location
     */
    @Suppress("unused")
    fun eval(scriptLocation: String) {
        val script = File(scriptLocation)
        PomKtsScriptHost.eval(script, project)
    }

    @PomDsl
    fun ProjectBuild.execute(id: String, phase: String, profile: String? = null, block: ExecuteContext.(ExecuteContext) -> Unit) {
        tasks.add(KotlinExecuteTask(block).apply {
            this.id = id
            this.phase = phase
            this.profileId = profile
        })
    }

    @PomDsl
    fun ProjectBuild.execute(id: String, phase: String, profile: String? = null, script: String) {
        tasks.add(KotlinExecuteTask {
            eval(script)
        }.apply {
            this.id = id
            this.phase = phase
            this.profileId = profile
        })
    }

}
