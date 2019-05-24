package org.sonatype.maven.polyglot.kotlin.engine

import org.apache.maven.MavenExecutionException
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

object ScriptHost {
    private val host = BasicJvmScriptingHost()
    private val compilationConfigs = mapOf(
        ScriptType.POM to createJvmCompilationConfigurationFromTemplate<PomKtsScript>(),
        ScriptType.TASK to createJvmCompilationConfigurationFromTemplate<ExternalKtsScript>())

    fun eval(script: File, type: ScriptType, reciever: Any) {
        val evaluationConfig = ScriptEvaluationConfiguration {
            implicitReceivers(reciever)
            jvm {
                baseClassLoader(reciever.javaClass.classLoader)
            }
        }
        val sourceCode = script.readText().toScriptSource()
        val compilationConfig = compilationConfigs[type] ?: throw IllegalArgumentException(
            "invalid type: must be one of: ${compilationConfigs.keys}")
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
