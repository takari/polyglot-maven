class PluginDependencies: DependenciesUnderTheHood() {

    //Intellij idea highlighting only first class member methods
    fun compile(groupId: String, artifactId: String, version: String, type: String = jar, optional: Boolean = false, classifier: String? = null)
            = dependencyOn(groupId, artifactId, version, "compile", type, classifier, null, optional)
    fun runtime(groupId: String, artifactId: String, version: String, type: String = jar, optional: Boolean = false, classifier: String? = null)
            = dependencyOn(groupId, artifactId, version, "runtime", type, classifier, null, optional)
    fun system(groupId: String, artifactId: String, version: String, type: String = jar, optional: Boolean = false,
               classifier: String? = null, systemPath: String? = null)
            = dependencyOn(groupId, artifactId, version, "system", type, classifier, systemPath, optional)

    fun compile(vararg artifacts: String) = dependenciesOn(artifacts, "compile")
    fun runtime(vararg artifacts: String) = dependenciesOn(artifacts, "runtime")
    fun system(vararg artifacts: String) = dependenciesOn(artifacts, "system")

    fun compile(artifact: String) = dependencyOn(artifact, "compile")
    fun runtime(artifact: String) = dependencyOn(artifact, "runtime")
    fun system(artifact: String) = dependencyOn(artifact, "system")

}