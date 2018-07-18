
import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show
import org.apache.maven.model.Dependency
import org.apache.maven.model.Plugin
import org.apache.maven.model.PluginExecution
import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import java.io.StringReader

fun Assert<List<Dependency>>.containsArtifact(artifact: String, scope: String = "compile", type: String = "jar",
                                              classifier: String? = null, systemPath: String? = null, optional: Boolean = false) {
    val (groupId, artifactId, version) = artifact.split(':')

    val element = actual.find { it.artifactId == artifactId && it.groupId == groupId && it.version == version }

    if (element == null) expected("to contain the artifact:${show(artifact)} but was:${show(actual)}")
    else if (element.scope != scope) expected("to contain the artifact:${show(artifact)} in scope:${show(scope)} but was in scope:${show(element.scope)}")
    else if (element.type != type) expected("to contain the artifact:${show(artifact)} with type:${show(type)} but was:${show(element.type)}")
    else if (element.classifier != classifier) expected("to contain the artifact:${show(artifact)} classified as:${show(classifier)} but was:${show(element.classifier)}")
    else if (element.systemPath != systemPath) expected("to contain the artifact:${show(artifact)} by systemPath:${show(systemPath)} but was:${show(element.systemPath)}")
    else if (element.optional != optional.toString()) expected("to contain the artifact:${show(artifact)} as optional:${show(optional)} but was:${show(element.optional)}")
}

fun Assert<List<PluginExecution>>.hasExecution(id: String, phase: String, goal: String) {
    val actualExecution = actual.find { it.id == id }

    if (actualExecution == null) expected("an execution item with id = $id but was:${show(actual)}")
    else if (actualExecution.phase != phase || !actualExecution.goals.contains(goal))
        expected("an execution item ${show("$id:$phase:$goal")} but was:${show(actual)}")
}

fun Assert<Plugin>.hasConfiguration(xmlProjectWithSinglePluginConfig: String) {
    val model = MavenXpp3Reader().read(StringReader(xmlProjectWithSinglePluginConfig))
    val expectedConfiguration = model.build.plugins[0].configuration

    val actualConfiguration = actual.configuration

    if (expectedConfiguration != actualConfiguration)
        expected("the configuration:\n$expectedConfiguration\nbut was:\n$actualConfiguration")
}


fun Assert<List<Plugin>>.containsPlugin(artifact: String) {
    val (groupId, artifactId, version) = artifact.split(':')

    actual.find { it.artifactId == artifactId && it.groupId == groupId && it.version == version }
            ?: expected("to contain the plugin:${show(artifact)} but was:${show(actual)}")
}