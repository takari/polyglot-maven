package org.sonatype.maven.polyglot.kotlin.dsl

import org.sonatype.maven.polyglot.execute.ExecuteTask

@PomDsl
class ProjectBuild : Build() {

    val tasks: MutableList<ExecuteTask> = mutableListOf()

}
