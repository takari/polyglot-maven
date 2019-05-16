package org.sonatype.maven.polyglot.kotlin.engine

import org.apache.maven.MavenExecutionException
import org.sonatype.maven.polyglot.kotlin.dsl.DSL
import org.sonatype.maven.polyglot.kotlin.dsl.Project
import java.io.File
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.api.ScriptDiagnostic
import kotlin.script.experimental.api.ScriptEvaluationConfiguration
import kotlin.script.experimental.api.implicitReceivers
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.baseClassLoader
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate
import kotlin.script.experimental.jvmhost.jvm

object PomKtsScriptHost {
    private val host = BasicJvmScriptingHost()
    private val compilationConfig = createJvmCompilationConfigurationFromTemplate<PomKtsScript>()

    fun eval(script: File, project: Project) {
        val evaluationConfig = ScriptEvaluationConfiguration {
            implicitReceivers(DSL(script, project))
            jvm {
                baseClassLoader(DSL::class.java.classLoader)
            }
        }
        val sourceCode = script.readText().toScriptSource()
        val result = host.eval(sourceCode, compilationConfig, evaluationConfig)
        result.reports.filter { it.exception != null }.forEach {
            System.err.println(it)
            it.exception?.printStackTrace()
        }
        if (result is ResultWithDiagnostics.Failure) {
            throw MavenExecutionException(
                    "Failed to evaluate script\n" +
                            result.reports
                                    .filterNot { it.severity == ScriptDiagnostic.Severity.DEBUG }
                                    .joinToString("\n"), script
            )
        }
    }
}
