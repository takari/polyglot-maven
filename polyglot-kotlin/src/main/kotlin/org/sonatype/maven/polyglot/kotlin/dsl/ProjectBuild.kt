package org.sonatype.maven.polyglot.kotlin.dsl

import org.sonatype.maven.polyglot.execute.ExecuteContext
import org.sonatype.maven.polyglot.execute.ExecuteTask
import org.sonatype.maven.polyglot.kotlin.engine.ScriptHost
import org.sonatype.maven.polyglot.kotlin.execute.KotlinExecuteTask
import java.io.File

@PomDsl
class ProjectBuild : Build() {

    val tasks: MutableList<ExecuteTask> = mutableListOf()

    @PomDsl
    fun execute(id: String, phase: String, profile: String? = null, block: ExecuteContext.() -> Unit) {
        tasks.add(KotlinExecuteTask(block).apply {
            this.id = id
            this.phase = phase
            this.profileId = profile
        })
    }

    @PomDsl
    fun execute(id: String, phase: String, profile: String? = null, script: String) {
        tasks.add(KotlinExecuteTask {
            val file = File("$basedir/$script")
            ScriptHost.eval(file, basedir, this)
        }.apply {
            this.id = id
            this.phase = phase
            this.profileId = profile
        })
    }
}
