package org.sonatype.maven.polyglot.kotlin.engine

import kotlin.script.experimental.annotations.KotlinScript

@KotlinScript(
    displayName = "Maven kotlin POM script",
    fileExtension = "pom.kts",
    compilationConfiguration = PomKtsScriptDefinition::class
)
abstract class PomKtsScript
