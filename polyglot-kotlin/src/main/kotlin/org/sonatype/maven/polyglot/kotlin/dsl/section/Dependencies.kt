@Scope class Dependencies {
    protected var deps: MutableList<Dependency> = mutableListOf()
    operator fun component1(): List<Dependency> = deps

    fun compile(groupId: String, artifactId: String, version: String, type: String = jar, optional: Boolean = false, classifier: String? = null)
            = dependencyOn(groupId, artifactId, version, "compile", type, classifier, null, optional)
    fun test(groupId: String, artifactId: String, version: String, type: String = jar, optional: Boolean = false, classifier: String? = null)
            = dependencyOn(groupId, artifactId, version, "test", type, classifier, null, optional)
    fun provided(groupId: String, artifactId: String, version: String, type: String = jar, optional: Boolean = false, classifier: String? = null)
            = dependencyOn(groupId, artifactId, version, "provided", type, classifier, null, optional)
    fun runtime(groupId: String, artifactId: String, version: String, type: String = jar, optional: Boolean = false, classifier: String? = null)
            = dependencyOn(groupId, artifactId, version, "runtime", type, classifier, null, optional)
    fun system(groupId: String, artifactId: String, version: String, type: String = jar, optional: Boolean = false,
               classifier: String? = null, systemPath: String? = null)
            = dependencyOn(groupId, artifactId, version, "system", type, classifier, systemPath, optional)

    fun compile(vararg artifacts: String) = dependenciesOn(artifacts, "compile")
    fun test(vararg artifacts: String) = dependenciesOn(artifacts, "test")
    fun provided(vararg artifacts: String) = dependenciesOn(artifacts, "provided")
    fun runtime(vararg artifacts: String) = dependenciesOn(artifacts, "runtime")
    fun system(vararg artifacts: String) = dependenciesOn(artifacts, "system")

    fun compile(artifact: String) = dependencyOn(artifact, "compile")
    fun test(artifact: String) = dependencyOn(artifact, "test")
    fun provided(artifact: String) = dependencyOn(artifact, "provided")
    fun runtime(artifact: String) = dependencyOn(artifact, "runtime")
    fun system(artifact: String) = dependencyOn(artifact, "system")

    private fun dependencyOn(artifact: String, scope: String): Dependency {
        var dependency: String = artifact
        val exclusions: MutableList<String> = mutableListOf()
        val details = HashMap<String, String>()

        val detailsIdx = artifact.indexOf("|")
        if (detailsIdx != -1) {
            dependency = artifact.substring(0, detailsIdx)
            artifact.substring(detailsIdx + 1).split("|").forEach({
                val (key, value) = it.split("=")
                details[key] = value
            })
            exclusions.addAll(details.remove("exclusions")?.split(",")?: mutableListOf<String>())
        }

        val dependencySegments = dependency.split(":")
        check(dependencySegments.size == 3, { "Wrong dependency format. Expected: groupId:artifactId:version" })

        val newDependency = Dependency(groupId = dependencySegments[0],
                artifactId = dependencySegments[1],
                version = dependencySegments[2],
                scope = scope,
                type = if (dependencySegments.size == 4) dependencySegments[3] else jar,
                exclusions = exclusions)
        deps.add(newDependency)
        return newDependency
    }

    private fun dependenciesOn(artifacts: Array<out String>, scope: String) = artifacts.forEach { dependencyOn(it, scope) }

    private fun dependencyOn(groupId: String, artifactId: String, version: String,
                             scope: String = "compile", type: String = "jar", classifier: String? = null,
                             systemPath: String? = null, optional: Boolean = false, exclusions: MutableList<String> = mutableListOf()) : Dependency {
        val dependency = Dependency(groupId, artifactId, version, scope, type, classifier, systemPath, optional, exclusions = exclusions)
        deps.add(dependency)
        return dependency
    }

    infix fun String.exclusions(artifact: String): String = this + "|exclusions=" + artifact
    infix fun String.exclusions(artifacts: Array<String>): String  = this + "|exclusions=" + artifacts.joinToString(",")

    infix fun String.type(artifactType: String): String = this + "|type=" + artifactType
    infix fun String.optional(optional: Boolean): String = this + "|optional=" + optional
    infix fun String.classifier(classifier: String?): String = this + "|classifier=" + classifier
    infix fun String.systemPath(path: String?): String = this + "|classifier=" + path

    open class Dependency(protected val groupId: String,
                          protected val artifactId: String,
                          protected val version: String,
                          protected val scope: String = "compile",
                          protected var type: String = "jar",
                          protected var classifier: String? = null,
                          protected var systemPath: String? = null,
                          protected var optional: Boolean = false,
                          protected var exclusions: MutableList<String> = mutableListOf()) {
        init {
            if (systemPath != null) check(scope == "system", { "systemPath allowed only in system scope" })
        }

        protected constructor(dependency: Dependency) :
                this(dependency.groupId, dependency.artifactId, dependency.version, dependency.scope, dependency.type,
                        dependency.classifier, dependency.systemPath, dependency.optional, dependency.exclusions)

        fun optional(): Dependency{
            optional = true
            return this
        }

        fun type(type: String): Dependency {
            this.type = type
            return this
        }

        fun classifier(classifier: String): Dependency {
            this.classifier = classifier
            return this
        }

        fun systemPath(path: String): Dependency {
            //Consider enforcing only for system scope by sub-type
            check(scope == "system", { "systemPath allowed only in system scope" })
            this.systemPath = path
            return this
        }

        fun exclusions(vararg artifacts: String) {
            this.exclusions.addAll(artifacts)
        }
    }
}