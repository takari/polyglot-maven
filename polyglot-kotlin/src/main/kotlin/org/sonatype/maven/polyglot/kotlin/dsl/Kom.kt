//package of
data class Project(
        val artifactId: String,
        val groupId: String? = null,
        val version: String? = null,

        val parent: String,

        val packaging: String = jar,
        val properties: ()->Array<out Pair<String, Any>>,
//        val properties: () -> Unit,
        val dependencies: (project: Project) -> Unit,

        var name: String,
        val modelVersion: String = "4.0.0",
        val description: String? = null,
        val url: String? = null,
        val inceptionYear: String? = null
) {
    companion object {
        val props: MutableMap<String, Any> = mutableMapOf<String, Any>()
    }

    operator fun get(property: String): Any {
        return props[property]?: throw IllegalArgumentException()
    }
}

val jar = "jar"
typealias project = Project

val compile = fun(list: () -> Any) {}
val test = fun(list: () -> Any) {}
val provided = fun(list: () -> Any) {}

fun compile(vararg list: String) {}
fun test(vararg list: String) {}
fun provided(vararg list: String) {}


infix fun String.equalTo(that: Any) {
    Project.props.put(this, that)
}
//operator fun String.plusAssign(that: Any): Unit {
//    Project.props.put(this, that)
//}

//fun dependency(groupId: String, artifactId: String, version: Any? = "LATEST") {}
//fun test(groupId: String, artifactId: String, version: String = "LATEST") {}
operator fun Any.get(vararg lines: String): Array<out String> = lines
fun <T> those(vararg lines: T): Array<out T> = lines

class ChainOfProperties(val value: Pair<String, Any>, val next: ChainOfProperties)