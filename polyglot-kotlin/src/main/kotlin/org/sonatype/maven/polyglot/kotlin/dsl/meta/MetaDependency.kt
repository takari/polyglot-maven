class MetaDependency(dependency: Dependencies.Dependency) : Dependencies.Dependency(dependency) {
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