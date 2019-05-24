package org.sonatype.maven.polyglot.kotlin.engine

import kotlin.script.experimental.annotations.KotlinScript

@KotlinScript(
    displayName = "External kotlin task script",
    fileExtension = "task.kts",
    compilationConfiguration = ExternalKtsScriptDefinition::class
)
abstract class ExternalKtsScript
