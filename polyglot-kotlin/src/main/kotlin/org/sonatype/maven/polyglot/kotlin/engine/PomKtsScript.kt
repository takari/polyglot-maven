package org.sonatype.maven.polyglot.kotlin.engine

import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.templates.ScriptTemplateDefinition

@Suppress("unused")
@ScriptTemplateDefinition(scriptFilePattern = "pom\\.kts")
@KotlinScript(displayName = "Maven kotlin script", fileExtension = "pom.kts", compilationConfiguration = PomKtsScriptDefinition::class)
abstract class PomKtsScript
