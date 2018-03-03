class MetaProject(project: Project): Project(project) {
    fun dependencies(): List<MetaDependency> {
        if (deps == null) return emptyList()
        val (dependencies) = deps!!
        return dependencies.map { MetaDependency(it) }
    }
    fun properties(): Map<String, Any> = props
    fun parent(): Project.Parent = superParent
    fun build(): Build? = thisBuild
}