package org.sonatype.maven.polyglot.kotlin.engine

import org.codehaus.plexus.classworlds.realm.ClassRealm
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer
import org.jetbrains.kotlin.cli.common.messages.PrintingMessageCollector
import org.jetbrains.kotlin.cli.common.repl.*
import org.jetbrains.kotlin.cli.jvm.config.addJvmClasspathRoots
import org.jetbrains.kotlin.cli.jvm.config.addJvmSdkRoots
import org.jetbrains.kotlin.cli.jvm.repl.GenericReplCompiler
import org.jetbrains.kotlin.config.*
import org.jetbrains.kotlin.script.KotlinScriptDefinition
import org.jetbrains.kotlin.script.KotlinScriptDefinitionFromAnnotatedTemplate
import org.jetbrains.kotlin.utils.PathUtil
import java.io.File
import java.util.concurrent.locks.ReentrantReadWriteLock
import javax.script.ScriptContext
import javax.script.ScriptEngine
import javax.script.ScriptEngineFactory
import kotlin.reflect.KClass

/**
 * Creates a JSR-223 [ScriptEngine] that is configured with Maven [ClassRealm] information.
 *
 * Based on [org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngine]
 *
 * @see [KotlinScriptEngineFactory]
 */
internal class KotlinScriptEngine(
    private val classloader: ClassLoader = Thread.currentThread().contextClassLoader,
    factory: ScriptEngineFactory,
    private val templateClasspath: List<File>,
    templateClassName: String,
    val getScriptArgs: (ScriptContext, Array<out KClass<out Any>>?) -> ScriptArgsWithTypes?,
    private val scriptArgsTypes: Array<out KClass<out Any>>?
) : KotlinJsr223JvmScriptEngineBase(factory), KotlinJsr223JvmInvocableScriptEngine {

    override val replCompiler: ReplCompiler by lazy {
        GenericReplCompiler(
            makeScriptDefinition(templateClassName),
            makeCompilerConfiguration(),
            PrintingMessageCollector(System.out, MessageRenderer.WITHOUT_PATHS, false))
    }

    private val localEvaluator by lazy { GenericReplCompilingEvaluator(replCompiler, templateClasspath, classloader, getScriptArgs(getContext(), scriptArgsTypes)) }

    override val replEvaluator: ReplFullEvaluator get() = localEvaluator

    override val state: IReplStageState<*> get() = getCurrentState(getContext())

    override fun createState(lock: ReentrantReadWriteLock): IReplStageState<*> = replEvaluator.createState(lock)

    override fun overrideScriptArgs(context: ScriptContext): ScriptArgsWithTypes? = getScriptArgs(context, scriptArgsTypes)

    private fun makeScriptDefinition(templateClassName: String): KotlinScriptDefinition {
        val cls = classloader.loadClass(templateClassName)
        return KotlinScriptDefinitionFromAnnotatedTemplate(cls.kotlin, emptyMap())
    }

    private fun makeCompilerConfiguration() = CompilerConfiguration().apply {
        addJvmSdkRoots(PathUtil.getJdkClassesRootsFromCurrentJre())
        addJvmClasspathRoots(templateClasspath)
        put(CommonConfigurationKeys.MODULE_NAME, "pom-script")
        languageVersionSettings = LanguageVersionSettingsImpl(
            LanguageVersion.LATEST_STABLE,
            ApiVersion.LATEST_STABLE,
            mapOf(AnalysisFlags.skipMetadataVersionCheck to true)
        )
    }
}
