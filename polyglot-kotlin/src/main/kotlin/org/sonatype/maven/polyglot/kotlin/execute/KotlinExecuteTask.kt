package org.sonatype.maven.polyglot.kotlin.execute

import org.sonatype.maven.polyglot.execute.ExecuteContext
import org.sonatype.maven.polyglot.execute.ExecuteTaskSupport

class KotlinExecuteTask(private val script: ScriptExecutionContext.(ScriptExecutionContext) -> Unit) : ExecuteTaskSupport() {

    override fun execute(context: ExecuteContext) {
        val scriptExecutionContext = ScriptExecutionContext(context)
        script.invoke(scriptExecutionContext, scriptExecutionContext)
    }
}