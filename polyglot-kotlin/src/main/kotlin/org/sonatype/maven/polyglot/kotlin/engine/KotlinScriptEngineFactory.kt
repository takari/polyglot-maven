package org.sonatype.maven.polyglot.kotlin.engine

import org.apache.maven.model.Model
import org.codehaus.plexus.classworlds.realm.ClassRealm
import org.codehaus.plexus.util.xml.Xpp3Dom
import org.jetbrains.kotlin.cli.common.repl.KotlinJsr223JvmScriptEngineFactoryBase
import org.jetbrains.kotlin.cli.common.repl.ScriptArgsWithTypes
import org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngineFactory
import org.jetbrains.kotlin.script.jsr223.KotlinStandardJsr223ScriptTemplate
import org.sonatype.maven.polyglot.kotlin.dsl.Project
import org.sonatype.maven.polyglot.kotlin.support.classpathFromClassLoader
import java.io.File
import java.net.URLClassLoader
import javax.script.Bindings
import javax.script.ScriptContext
import javax.script.ScriptEngine
import javax.script.ScriptEngineFactory

/**
 * Configures a [ScriptEngineFactory] that produces a JSR-223 [ScriptEngine] that is configured with entries from the
 * Maven [ClassRealm], if available.
 */
internal class KotlinScriptEngineFactory() : KotlinJsr223JvmScriptEngineFactoryBase() {

    private val kotlinScriptEngine: ScriptEngine by lazy {
        debug { System.err.println("${id(projectClassLoader)} CREATING NEW KOTLIN SCRIPT ENGINE FACTORY") }
        KotlinScriptEngine(
                scriptClassLoader,
                this,
                templateClasspath,
                KotlinStandardJsr223ScriptTemplate::class.qualifiedName!!,
                { ctx, types ->
                    ScriptArgsWithTypes(arrayOf(ctx.getBindings(ScriptContext.ENGINE_SCOPE)), types ?: emptyArray())
                },
                arrayOf(Bindings::class)
        )
    }

    override fun getScriptEngine(): ScriptEngine {
        if (projectClassLoader !is ClassRealm) {
            return KotlinJsr223JvmLocalScriptEngineFactory().scriptEngine
        }
        return kotlinScriptEngine
    }
}

private val debug = System.getProperty("polyglot-kotlin.debug")?.toBoolean() ?: false

private fun debug(block: () -> Unit) {
    if (debug) block()
}

internal val singletonEngineFactory: ScriptEngineFactory by lazy {
    KotlinScriptEngineFactory()
}

private val modelClassLoader: ClassLoader = Model::class.java.classLoader
private val projectClassLoader: ClassLoader = Project::class.java.classLoader
private val xpp3DomClassLoader: ClassLoader = Xpp3Dom::class.java.classLoader

private val templateClasspath: List<File> by lazy {
    debug { System.err.println("${id(projectClassLoader)} CREATING NEW TEMPLATE CLASS PATH") }
    val classpath = LinkedHashSet(classpathFromClassLoader(projectClassLoader))
    classpath.addAll(classpathFromClassLoader(modelClassLoader))
    classpath.addAll(classpathFromClassLoader(xpp3DomClassLoader))
    classpath.toList()
}

private val scriptClassLoader: ClassLoader by lazy {
    debug { System.err.println("${id(projectClassLoader)} CREATING NEW SCRIPT ENGINE CLASS LOADER") }
    debug { print(templateClasspath) }
    URLClassLoader(templateClasspath.map { file -> file.toURI().toURL() }.toTypedArray(), projectClassLoader)
}

private fun print(classLoader: ClassLoader) {
    print(classpathFromClassLoader(classLoader))
}

private fun print(classpath: Collection<File>) {
    System.err.println("--------------------------------------------------------------------------------")
    classpath.forEachIndexed { index, url ->
        System.err.println("url[${index}] = $url")
    }
    System.err.println("--------------------------------------------------------------------------------")
}

private fun id(obj: Any): String {
    return "${obj.javaClass.name}@${Integer.toHexString(System.identityHashCode(obj))}"
}
