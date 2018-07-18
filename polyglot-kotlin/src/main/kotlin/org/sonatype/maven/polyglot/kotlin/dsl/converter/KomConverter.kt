
import BuildConverter.buildOf
import DependencyConverter.dependenciesOf
import ProfileConverter.profilesOf
import org.apache.maven.model.DependencyManagement
import org.apache.maven.model.Model

object KomConverter {

    fun toModel(project: Project): Model {
        val metaProject = MetaProject(project)

        val model = Model()

        model.name = project.name
        model.description = project.description

        model.modelVersion = project.modelVersion

        val parent = metaProject.parent()
        if (parent != null) model.parent = parentOf(parent)

        model.artifactId = project.artifactId
        model.groupId = project.groupId ?: model.parent.groupId ?: throw IllegalArgumentException("No groupId specified")
        model.version = project.version ?: model.parent.version ?: throw IllegalArgumentException("No groupId specified")
        model.packaging = project.packaging

        model.url = project.url
        model.inceptionYear = project.inceptionYear

        model.properties.putAll(metaProject.properties().mapValues { it.value.toString() })
        model.dependencies = dependenciesOf(metaProject.dependencies())

        val projectBuild = metaProject.build()
        if (projectBuild != null) model.build = buildOf(projectBuild)

        model.modules.addAll(project.modules)
        metaProject.dependencyManagement()?.let {
            val (deps) = it.component1()
            model.dependencyManagement = DependencyManagement().apply {
                dependencies = dependenciesOf(deps.map { MetaDependency(it) })
            }
        }

        val profiles = metaProject.profiles()
        if (profiles != null) model.profiles = profilesOf(profiles)

        return model
    }

    private fun parentOf(parent: Parent): org.apache.maven.model.Parent {
        return org.apache.maven.model.Parent().apply {
            groupId = parent.groupId
            artifactId = parent.artifactId
            version = parent.version
            relativePath = parent.relativePath
        }
    }
}