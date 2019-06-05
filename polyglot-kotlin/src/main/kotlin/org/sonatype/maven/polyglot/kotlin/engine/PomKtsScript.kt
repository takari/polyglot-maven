package org.sonatype.maven.polyglot.kotlin.engine

import org.sonatype.maven.polyglot.execute.ExecuteContext
import org.sonatype.maven.polyglot.kotlin.dsl.PomDsl
import org.sonatype.maven.polyglot.kotlin.dsl.Project
import java.io.File
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.templates.ScriptTemplateDefinition

@KotlinScript(
        displayName = "Maven kotlin POM script",
        fileExtension = "pom.kts",
        compilationConfiguration = ScriptDefinition::class
)
@ScriptTemplateDefinition(scriptFilePattern = "pom\\.kts")
abstract class PomKtsScript(val script: File, val project: Project) {

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
