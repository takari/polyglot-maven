package org.sonatype.maven.polyglot.kotlin.generator

import jar
import org.apache.maven.model.Dependency
import org.apache.maven.model.Exclusion

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
            "\"${it.groupId}:${it.artifactId}:${it.version}\"" + detailsOf(it)
        }

        if (multiline) codeText.append(lnOrEmpty).append(tab(count = 2))

        return codeText.appendln(")").toString()
    }

    private fun detailsOf(dependency: Dependency): String {
        val detailsBuilder = StringBuilder()
        if (dependency.type != jar) detailsBuilder.append(" type ${dependency.type}")
        if (dependency.isOptional) detailsBuilder.append(" optional true")
        if (dependency.classifier != null) detailsBuilder.append(" classifier ${dependency.classifier}")
        if (dependency.systemPath != null) detailsBuilder.append(" systemPath ${dependency.systemPath}")
        if (dependency.exclusions.size == 1) detailsBuilder.append(" exclusions ${dependency.exclusions[0].groupIdArtifactId()}")
        if (dependency.exclusions.size > 1) detailsBuilder.append(" exclusions arrayOf(" +
                dependency.exclusions.joinToString { it.groupIdArtifactId() } + ")")
        return detailsBuilder.toString()
    }

    private fun Exclusion.groupIdArtifactId() = "\"$groupId:$artifactId\""
}