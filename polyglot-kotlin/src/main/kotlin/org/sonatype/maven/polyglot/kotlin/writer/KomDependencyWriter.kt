package org.sonatype.maven.polyglot.kotlin.writer

import jar
import org.apache.maven.model.Dependency
import org.apache.maven.model.Exclusion

object KomDependencyWriter {
    fun import(dependencies: List<Dependency>, marginSize: Int = 2): String = dependencyIn("import", dependencies, marginSize)
    fun compile(dependencies: List<Dependency>, marginSize: Int = 2): String = dependencyIn("compile", dependencies, marginSize)
    fun test(dependencies: List<Dependency>, marginSize: Int = 2): String = dependencyIn("test", dependencies, marginSize)
    fun provided(dependencies: List<Dependency>, marginSize: Int = 2): String = dependencyIn("provided", dependencies, marginSize)
    fun system(dependencies: List<Dependency>, marginSize: Int = 2): String = dependencyIn("system", dependencies, marginSize)
    fun runtime(dependencies: List<Dependency>, marginSize: Int = 2): String = dependencyIn("runtime", dependencies, marginSize)


    private fun dependencyIn(scope: String, dependencies: List<Dependency>, marginSize: Int): String {
        val scopeDependencies = dependencies.filter { it.scope == scope }
        if (scopeDependencies.isEmpty()) return ""

        val multiline = scopeDependencies.size > 1
        val lnOrEmpty = if (multiline) nextLine else ""
        val tabOrEmpty = if (multiline) tab(count = marginSize + 1) else ""

        val codeText = StringBuilder(tab("$scope($lnOrEmpty", marginSize))
        scopeDependencies.joinTo(codeText, prefix = tabOrEmpty, separator = ",$lnOrEmpty" + tab(count = marginSize + 1)) {
            "\"${it.groupId}:${it.artifactId}:${it.version}\"" + detailsOf(it)
        }

        if (multiline) codeText.append(lnOrEmpty).append(tab(count = marginSize))

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