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

    inline fun properties(function: () -> Unit) = function()

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
    fun compile(groupId: String, artifactId: String, version: String, type: String = jar): Dependency
            = Dependency(groupId, artifactId, version, "compile", type)

    fun test(vararg artifacts: String) = dependenciesOn(artifacts, "test")
    fun test(artifact: String) = dependencyOn(artifact, "test")
    fun test(groupId: String, artifactId: String, version: String, type: String = jar): Dependency
            = Dependency(groupId, artifactId, version, "test", type)

    fun provided(vararg artifacts: String) = dependenciesOn(artifacts, "provided")
    fun provided(artifact: String) = dependencyOn(artifact, "provided")
    fun provided(groupId: String, artifactId: String, version: String, type: String = jar): Dependency
            = Dependency(groupId, artifactId, version, "provided", type)

    fun runtime(vararg artifacts: String) = dependenciesOn(artifacts, "runtime")
    fun runtime(artifact: String) = dependencyOn(artifact, "runtime")
    fun runtime(groupId: String, artifactId: String, version: String, type: String = jar): Dependency
            = Dependency(groupId, artifactId, version, "runtime", type)

    fun system(vararg artifacts: String) = dependenciesOn(artifacts, "system")
    fun system(artifact: String) = dependencyOn(artifact, "system")
    fun system(groupId: String, artifactId: String, version: String, type: String = jar): Dependency
            = Dependency(groupId, artifactId, version, "system", type)

    private fun dependencyOn(artifact: String, scope: String): Dependency {
        var dependency: String = artifact
        var exclusions: List<String> = emptyList()

        val exclusionsIdx = artifact.indexOf("-=")
        if (exclusionsIdx != -1) {
            dependency = artifact.substring(0, exclusionsIdx)
            exclusions = artifact.substring(exclusionsIdx + 2).split(",")
        }

        val dependencySegments = dependency.split(":")
        check(dependencySegments.size in 3..4, { "Wrong dependency format. Expected: groupId:artifactId:version:<type>" })

        return Dependency(groupId = dependencySegments[0],
                artifactId = dependencySegments[1],
                version = dependencySegments[2],
                scope = scope,
                type = if (dependencySegments.size == 4) dependencySegments[3] else jar,
                exclusions = exclusions)
    }

    private fun dependenciesOn(artifacts: Array<out String>, scope: String) = artifacts.forEach { dependencyOn(it, scope) }

    inner class Dependency(private val groupId: String,
                           private val artifactId: String,
                           private val version: String,
                           private val scope: String = "compile",
                           private val type: String = "jar",
                           exclusions: List<String> = emptyList()) {
        private var excludes: MutableList<String> = mutableListOf()
        init {
            this.excludes.addAll(exclusions)
            deps.add(this)
        }

        fun exclusions(vararg artifacts: String) {
            this.excludes.addAll(artifacts)
        }

        operator fun component1(): String = groupId
        operator fun component2(): String = artifactId
        operator fun component3(): String = version
        operator fun component4(): String = scope
        operator fun component5(): String = type
        operator fun component6(): List<String> = excludes
    }

    infix fun String.exclusions(artifact: String): String = this + "-=" + artifact
    infix fun String.exclusions(artifacts: Array<String>): String  = this + "-=" + artifacts.joinToString(",")

    infix fun String.type(artifactType: String): String = this + ":" + artifactType

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

