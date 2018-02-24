
import org.apache.maven.model.Dependency
import org.apache.maven.model.Exclusion

object DependencyConverter {

    internal fun dependenciesOf(projectDependencies: List<Project.Dependency>): MutableList<Dependency> {
        val dependencies = mutableListOf<Dependency>()
        projectDependencies.forEach {
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

}