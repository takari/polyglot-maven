import org.jetbrains.kotlin.cli.common.repl.KotlinJsr223JvmScriptEngineBase
import org.jetbrains.kotlin.cli.common.repl.KotlinJsr223JvmScriptEngineFactoryBase
import org.jetbrains.kotlin.cli.common.repl.ScriptArgsWithTypes
import org.jetbrains.kotlin.script.jsr223.KotlinStandardJsr223ScriptTemplate
import org.jetbrains.kotlin.script.util.scriptCompilationClasspathFromContext
import org.sonatype.maven.polyglot.kotlin.engine.KotlinJsr223ScriptEngine
import javax.script.Bindings
import javax.script.ScriptContext

internal class KomScriptEngineFactory : KotlinJsr223JvmScriptEngineFactoryBase() {

    override fun getScriptEngine(): KotlinJsr223JvmScriptEngineBase {
        val templateClasspath = scriptCompilationClasspathFromContext("kotlin-script-runtime.jar", "kotlin-script-util.jar",
                classLoader = Project::class.java.classLoader
        )

        return KotlinJsr223ScriptEngine(
                Project::class.java.classLoader,
                this,
                templateClasspath,
                KotlinStandardJsr223ScriptTemplate::class.qualifiedName!!,
                { ctx, types ->
                    ScriptArgsWithTypes(arrayOf(ctx.getBindings(ScriptContext.ENGINE_SCOPE)), types ?: emptyArray())
                },
                arrayOf(Bindings::class)
        )
    }
}