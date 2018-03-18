import BuildConverter.buildOf
import DependencyConverter.dependenciesOf
import org.apache.maven.model.Model
import org.apache.maven.model.Parent

object KomConverter {

    fun toModel(project: Project): Model {
        val metaProject = MetaProject(project)

        val model = Model()

        model.name = project.name
        model.description = project.description

        model.modelVersion = project.modelVersion

        model.parent = parentOf(metaProject)

        model.artifactId = project.artifactId
        model.groupId = project.groupId ?: model.parent.groupId
        model.version = project.version ?: model.parent.version
        model.packaging = project.packaging

        model.url = project.url
        model.inceptionYear = project.inceptionYear

        model.properties.putAll(metaProject.properties().mapValues { it.value.toString() })
        model.dependencies = dependenciesOf(metaProject.dependencies())

        val projectBuild = metaProject.build()
        if (projectBuild != null) model.build = buildOf(projectBuild)
        return model
    }

    private fun parentOf(metaProject: MetaProject): Parent {
        val parent = metaProject.parent()
        return Parent().apply {
            groupId = parent.groupId
            artifactId = parent.artifactId
            version = parent.version
            relativePath = parent.relativePath
        }
    }
}