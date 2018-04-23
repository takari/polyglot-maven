package org.sonatype.maven.polyglot.kotlin.generator

import org.apache.maven.model.Dependency

object KomDependencyGenerator {
    fun import(dependencies: List<Dependency>): String = dependencyIn("import", dependencies)
    fun compile(dependencies: List<Dependency>): String = dependencyIn("compile", dependencies)
    fun test(dependencies: List<Dependency>): String = dependencyIn("test", dependencies)
    fun provided(dependencies: List<Dependency>): String = dependencyIn("provided", dependencies)
    fun system(dependencies: List<Dependency>): String = dependencyIn("system", dependencies)


    private fun dependencyIn(scope: String, dependencies: List<Dependency>): String {
        val scopeDependencies = dependencies.filter { it.scope == scope }
        if (scopeDependencies.isEmpty()) return ""

        val multiline = scopeDependencies.size > 1
        val lnOrEmpty = if (multiline) nextLine else ""
        val tabOrEmpty = if (multiline) tab(count = 3) else ""

        val codeText = StringBuilder(tab("$scope($lnOrEmpty", 2))
        scopeDependencies.joinTo(codeText, prefix = tabOrEmpty, separator = ",$lnOrEmpty" + tab(count = 3)) {
            "\"${it.groupId}:${it.artifactId}:${it.version}\""
        }

        if (multiline) codeText.append(lnOrEmpty).append(tab(count = 2))

        return codeText.appendln(")").toString()
    }
}