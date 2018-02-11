open class Project() {
    lateinit var artifactId: String
    var groupId: String? = null //default = parent.groupId
    var version: String? = null //default = parent.version

    lateinit var parent: String

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
        if (exclusionsIdx != -1) {
            val exclusionLine = name.substring(exclusionsIdx + 2)
            dependency = Dependency(name.substring(0, exclusionsIdx), scope, exclusionLine.split(","))
        } else dependency = Dependency(name, scope)

        deps.add(dependency)
        return dependency
    }

    private fun dependenciesOn(list: Array<out String>, scope: String)
            = list.forEach { dependencyOn(it, scope) }

    class Dependency(protected val dependency: String,
                          protected val scope: String = "compile",
                          exclusions: List<String> =  emptyList<String>()) {
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
    
    protected constructor(project: Project):this(){
        props = project.props
        deps = project.deps
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