package org.sonatype.maven.polyglot.kotlin.dsl

import org.sonatype.maven.polyglot.execute.ExecuteContext
import org.sonatype.maven.polyglot.execute.ExecuteTask
import org.sonatype.maven.polyglot.kotlin.engine.singletonEngineFactory
import org.sonatype.maven.polyglot.kotlin.execute.KotlinExecuteTask
import java.io.File

@PomDsl
class ProjectBuild : Build() {

    val tasks: MutableList<ExecuteTask> = mutableListOf()

    @PomDsl
    fun execute(id: String, phase: String, profile: String? = null, block: ExecuteContext.(ExecuteContext) -> Unit) {
        tasks.add(KotlinExecuteTask(block).apply {
            this.id = id
            this.phase = phase
            this.profileId = profile
        })
    }

    @PomDsl
    fun execute(id: String, phase: String, profile: String? = null, script: String) {
        tasks.add(KotlinExecuteTask {
            val scriptEngine = singletonEngineFactory.scriptEngine
            val file = File("$basedir/$script")
            val bindings = scriptEngine.createBindings()
            bindings["project"] = project
            bindings["log"] = log
            bindings["basedir"] = basedir
            bindings["script"] = file
            scriptEngine.eval(file.reader(), bindings)
        }.apply {
            this.id = id
            this.phase = phase
            this.profileId = profile
        })
    }
}