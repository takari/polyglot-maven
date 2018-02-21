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

    protected var deps: MutableList<Dependency> = mutableListOf()

    fun dependencies(function: () -> Unit) = function()

    fun compile(vararg list: String) = dependenciesOn(list, "compile")
    fun compile(name: String): Dependency = dependencyOn(name, "compile")

    fun test(vararg list: String) = dependenciesOn(list, "test")
    fun test(name: String) = dependencyOn(name, "test")

    fun provided(vararg list: String) = dependenciesOn(list, "provided")
    fun provided(name: String) = dependencyOn(name, "provided")

    private fun dependencyOn(name: String, scope: String): Dependency {
        val dependency: Dependency

        val exclusionsIdx = name.indexOf("-=")
        dependency = if (exclusionsIdx != -1) {
            val exclusionLine = name.substring(exclusionsIdx + 2)
            Dependency(name.substring(0, exclusionsIdx), scope, exclusionLine.split(","))
        } else Dependency(name, scope)

        deps.add(dependency)
        return dependency
    }

    private fun dependenciesOn(list: Array<out String>, scope: String) = list.forEach { dependencyOn(it, scope) }

    class Dependency(protected val dependency: String,
                     protected val scope: String = "compile",
                     exclusions: List<String> = emptyList<String>()) {
        protected var excludes: MutableList<String> = mutableListOf()

        init {
            this.excludes.addAll(exclusions)
        }

        fun exclusions(vararg exclusions: String) {
            this.excludes.addAll(exclusions)
        }

        operator fun component1(): String = dependency
        operator fun component2(): String = scope
        operator fun component3(): List<String> = excludes
    }

    infix fun String.exclusions(name: String): String {
        return this + "-=" + name
    }

    infix fun String.exclusions(names: Array<String>): String {
        return this + "-=" + names.joinToString(",")
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

