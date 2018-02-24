
import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show
import org.apache.maven.model.Dependency

fun Assert<List<Dependency>>.containsArtifact(artifact: String, scope: String = "compile") {
    val segments = artifact.split(":")
    val groupId = segments[0]
    val artifactId = segments[1]
    val version = segments[2]

    val element = actual.find { it.artifactId == artifactId && it.groupId == groupId && it.version == version }

    if (element == null) expected("to contain the artifact:${show(artifact)} but was:${show(actual)}")
    else if (element.scope != scope) expected("to contain the artifact:${show(artifact)} in scope:${show(scope)} but was in scope:${show(element.scope)}")
}