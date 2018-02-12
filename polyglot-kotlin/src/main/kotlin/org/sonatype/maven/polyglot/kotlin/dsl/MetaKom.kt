class MetaProject(project: Project): Project(project) {
    fun dependencies(): List<Project.Dependency> = deps
    fun properties(): Map<String, Any> = props
    fun parent(): Project.Parent = superParent
}