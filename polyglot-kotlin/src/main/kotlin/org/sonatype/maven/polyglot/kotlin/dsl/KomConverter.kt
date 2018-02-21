import org.apache.maven.model.Dependency
import org.apache.maven.model.Exclusion
import org.apache.maven.model.Model
import org.apache.maven.model.Parent

object KomConverter {

    fun toModel(project: Project): Model {
        val model = Model()

        model.name = project.name
        model.description = project.description

        model.modelVersion = project.modelVersion

        val metaProject = MetaProject(project)
        model.parent = parentOf(metaProject)

        model.artifactId = project.artifactId
        model.groupId = project.groupId ?: model.parent.groupId
        model.version = project.version ?: model.parent.version
        model.packaging = project.packaging

        model.url = project.url
        model.inceptionYear = project.inceptionYear

        model.properties.putAll(metaProject.properties().mapValues { it.value.toString() })
        model.dependencies = dependenciesOf(metaProject.dependencies())

        return model
    }

    private fun dependenciesOf(projectDeps: List<Project.Dependency>): MutableList<Dependency> {
        val dependencies = mutableListOf<Dependency>()
        projectDeps.forEach {
            val (dependency, scope, exclusions) = it
            val dependencySegments = dependency.split(":")
            check(dependencySegments.size == 3, { "Wrong dependency format. Expected: groupId:artifactId:version" })

            dependencies.add(Dependency().apply {
                groupId = dependencySegments[0]
                artifactId = dependencySegments[1]
                version = dependencySegments[2]
                this.scope = scope

                exclusionsOf(exclusions).forEach { addExclusion(it) }
            })
        }
        return dependencies
    }

    private fun exclusionsOf(exclusions: List<String>): List<Exclusion> {
        val excludes = mutableListOf<Exclusion>()
        exclusions.forEach {
            val exclusionSegments = it.split(":")
            check(exclusionSegments.size == 2, { "Wrong exclusion format. Expected: groupId:artifactId" })

            excludes.add(Exclusion().apply {
                groupId = exclusionSegments[0]
                artifactId = exclusionSegments[1]
            })
        }
        return excludes
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