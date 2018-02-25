class MetaProject(project: Project): Project(project) {
    fun dependencies(): List<MetaDependency> = deps.map { MetaDependency(it) }
    fun properties(): Map<String, Any> = props
    fun parent(): Project.Parent = superParent

    inner class MetaDependency(dependency: Dependency): Dependency(dependency) {
        fun groupId() = super.groupId
        fun artifactId() = super.artifactId
        fun version() = super.version
        fun scope() = super.scope
        fun type() = super.type
        fun classifier() = super.classifier
        fun systemPath() = super.systemPath
        fun isOptional() = super.optional
        fun exclusions() = super.exclusions
    }
}