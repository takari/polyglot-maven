open class Project() {
    lateinit var artifactId: String
    var groupId: String? = null //default = parent.groupId
        get() = field ?: superParent.groupId
    var version: String? = null //default = parent.version
        get() = field ?: superParent.version

    var parent: String? = null
        set(value) {
            val parentSegments = requireNotNull(value).split(":")
            check(parentSegments.size == 3, { "Wrong Project.parent format. Expected: groupId:artifactId:version" })
            val path = if (::relativePath.isInitialized) relativePath else "../pom.kts"
            this.Parent().apply {
                groupId = parentSegments[0]
                artifactId = parentSegments[1]
                version = parentSegments[2]
                relativePath = path
            }
        }

    private lateinit var relativePath: String
    infix fun String.relativePath(path: String): String {
        check(!this@Project::relativePath.isInitialized, { "relativePath was already defined" })
        check(!this@Project::superParent.isInitialized, { "Please define relativePath inside parent {\n}" })

        relativePath = path
        return this
    }

    var packaging: String = jar

    lateinit var name: String
    var modelVersion: String = "4.0.0"
    var description: String? = null
    var url: String? = null
    var inceptionYear: String? = null

    //properties
    protected var props: MutableMap<String, Any> = mutableMapOf<String, Any>()

    fun properties(function: () -> Unit) = function()

    infix fun String.sameAs(value: Any) {
        props.put(this, value)
    }

    operator fun String.plusAssign(value: Any) {
        props.put(this, value)
    }

    operator fun get(property: String): Any {
        return props[property] ?: throw IllegalArgumentException()
    }

    //dependencies
    protected var deps: MutableList<Dependency> = mutableListOf()

    inline fun dependencies(function: () -> Unit) = function()

    fun compile(vararg artifacts: String) = dependenciesOn(artifacts, "compile")
    fun compile(artifact: String): Dependency = dependencyOn(artifact, "compile")
    fun compile(groupId: String, artifactId: String, version: String): Dependency
            = Dependency(groupId, artifactId, version, "compile")

    fun test(vararg artifacts: String) = dependenciesOn(artifacts, "test")
    fun test(artifact: String) = dependencyOn(artifact, "test")
    fun test(groupId: String, artifactId: String, version: String): Dependency
            = Dependency(groupId, artifactId, version, "test")

    fun provided(vararg artifacts: String) = dependenciesOn(artifacts, "provided")
    fun provided(artifact: String) = dependencyOn(artifact, "provided")
    fun provided(groupId: String, artifactId: String, version: String): Dependency
            = Dependency(groupId, artifactId, version, "provided")

    fun runtime(vararg artifacts: String) = dependenciesOn(artifacts, "runtime")
    fun runtime(artifact: String) = dependencyOn(artifact, "runtime")
    fun runtime(groupId: String, artifactId: String, version: String): Dependency
            = Dependency(groupId, artifactId, version, "runtime")

    fun system(vararg artifacts: String) = dependenciesOn(artifacts, "system")
    fun system(artifact: String) = dependencyOn(artifact, "system")
    fun system(groupId: String, artifactId: String, version: String): Dependency
            = Dependency(groupId, artifactId, version, "system")

    private fun dependencyOn(artifact: String, scope: String): Dependency {
        val dependency: Dependency

        val exclusionsIdx = artifact.indexOf("-=")
        dependency = if (exclusionsIdx != -1) {
            val exclusionLine = artifact.substring(exclusionsIdx + 2)
            Dependency(artifact.substring(0, exclusionsIdx), scope, exclusionLine.split(","))
        } else Dependency(artifact, scope)

        return dependency
    }

    private fun dependenciesOn(artifacts: Array<out String>, scope: String) = artifacts.forEach { dependencyOn(it, scope) }

    inner class Dependency(private val dependency: String,
                     private val scope: String = "compile",
                     exclusions: List<String> = emptyList()) {
        private var excludes: MutableList<String> = mutableListOf()
        init {
            this.excludes.addAll(exclusions)
            deps.add(this)
        }
        //TODO: replace dependency with 3 fields: groupId, artifactId, version
        constructor(groupId: String, artifactId: String, version: String, scope: String)
                :this("$groupId:$artifactId:$version", scope)

        fun exclusions(vararg artifacts: String) {
            this.excludes.addAll(artifacts)
        }

        operator fun component1(): String = dependency
        operator fun component2(): String = scope
        operator fun component3(): List<String> = excludes
    }

    infix fun String.exclusions(artifact: String): String {
        return this + "-=" + artifact
    }

    infix fun String.exclusions(artifacts: Array<String>): String {
        return this + "-=" + artifacts.joinToString(",")
    }

    protected constructor(project: Project) : this() {
        props = project.props
        deps = project.deps
        superParent = project.superParent
    }

    protected lateinit var superParent: Parent

    inner class Parent() {
        lateinit var groupId: String
        lateinit var artifactId: String
        lateinit var version: String
        var relativePath: String = "../pom.kts"

        init {
            check(!::superParent.isInitialized, { "Parent defined several times" })
            superParent = this
        }
    }

    inline fun parent(block: Project.Parent.(Project.Parent) -> Unit): Project.Parent {
        val parent = this.Parent()
        block(parent, parent)
        return parent
    }
}

//artifacts
val jar = "jar"
val war = "war"
val ear = "ear"
val pom = "pom"

inline fun project(block: Project.(Project) -> Unit): Project {
    val project = Project()
    block(project, project)
    return project
}
//operator fun Any.get(vararg lines: String): Array<out String> = lines

