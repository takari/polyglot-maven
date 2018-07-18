import org.apache.maven.model.Dependency
import org.apache.maven.model.Exclusion

object DependencyConverter {

    internal fun dependenciesOf(metaDependencies: List<MetaDependency>): MutableList<Dependency> {
        val dependencies = mutableListOf<Dependency>()

        metaDependencies.forEach {
            dependencies.add(Dependency().apply {
                groupId = it.groupId()
                artifactId = it.artifactId()
                version = it.version()
                type = it.type()
                scope = it.scope()
                classifier = it.classifier()
                systemPath = it.systemPath()
                optional = it.isOptional().toString()

                exclusionsOf(it.exclusions()).forEach { addExclusion(it) }
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