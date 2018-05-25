class MetaProject(project: Project): Project(project) {
    fun dependencies(): List<MetaDependency> {
        if (deps == null) return emptyList()
        val (dependencies) = deps!!
        return dependencies.map { MetaDependency(it) }
    }
    fun properties(): Map<String, Any> = props
    fun parent(): Parent? = superParent
    fun build(): Build? = thisBuild
    fun profiles(): Profiles? = thisProfiles
    fun dependencyManagement(): DependencyManagement? = dependencyManagement
}