
import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show
import org.apache.maven.model.Dependency

fun Assert<List<Dependency>>.containsArtifact(artifact: String, scope: String = "compile", type: String = "jar",
                                              classifier: String? = null, systemPath: String? = null, optional: Boolean = false) {
    val segments = artifact.split(":")
    val groupId = segments[0]
    val artifactId = segments[1]
    val version = segments[2]

    val element = actual.find { it.artifactId == artifactId && it.groupId == groupId && it.version == version }

    if (element == null) expected("to contain the artifact:${show(artifact)} but was:${show(actual)}")
    else if (element.scope != scope) expected("to contain the artifact:${show(artifact)} in scope:${show(scope)} but was in scope:${show(element.scope)}")
    else if (element.type != type) expected("to contain the artifact:${show(artifact)} with type:${show(type)} but was:${show(element.type)}")
    else if (element.classifier != classifier) expected("to contain the artifact:${show(artifact)} classified as:${show(classifier)} but was:${show(element.classifier)}")
    else if (element.systemPath != systemPath) expected("to contain the artifact:${show(artifact)} by systemPath:${show(systemPath)} but was:${show(element.systemPath)}")
    else if (element.optional != optional.toString()) expected("to contain the artifact:${show(artifact)} as optional:${show(optional)} but was:${show(element.optional)}")
}