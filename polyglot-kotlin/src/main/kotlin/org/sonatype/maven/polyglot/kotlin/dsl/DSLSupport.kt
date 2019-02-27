@file:kotlin.jvm.JvmMultifileClass
@file:kotlin.jvm.JvmName("DSLSupport")

package org.sonatype.maven.polyglot.kotlin.dsl

import org.apache.commons.lang3.StringEscapeUtils.escapeJava

/**
 * Configures a Maven project model.
 */
@PomDsl
fun project(nameOrId: String? = null, block: Project.(Project) -> Unit): Project {
    val (groupId, artifactId, version, packaging) = splitCoordinates(nameOrId, 4)
    return Project().apply {
        this.modelVersion = "4.0.0"
        this.modelEncoding = "UTF-8"
        this.name = nameOrId
        this.groupId = groupId
        this.artifactId = artifactId
        this.version = version
        if (packaging != null) this.packaging = packaging
        block(this, this)
    }
}

internal var propertiesFactory: () -> java.util.Properties = { java.util.Properties() }

private val coordinatesDelimiter = Regex(":")

/**
 * Splits the supplied artifact identifier into its constituent parts.
 *
 * @param coordinates an artifact identifier in the form `groupId:artifactId[:version][:type][:classifier]`
 * @param size the size of the array to be returned
 */
fun splitCoordinates(coordinates: String?, size: Int = 5): Array<String?> {
    val list = Array<String?>(size) { null }
    coordinates?.split(coordinatesDelimiter, size)?.forEachIndexed { i, str ->
        list[i] = if (str.isBlank()) null else str.trim()
    }
    return list
}

fun <T : Any> MutableCollection<T>.addAll(vararg t: T) {
    t.forEach { add(it) }
}

fun <T : Any> MutableCollection<T>.addFirstNonNull(vararg t: T?) {
    val first = t.firstOrNull { it != null }
    if (first != null) add(first)
}

fun <T : Any> MutableCollection<T>.addAllNonNull(vararg t: T?) {
    t.forEach { if (it != null) add(it) }
}

@Suppress("UNCHECKED_CAST")
fun <T> cast(o: Any): T {
    return o as T
}

fun escape(text: String): String {
    return escapeJava(text).replace("\$", "\\\$")
}

fun escapeRaw(text: String): String {
    return text.replace("""$""", """${'$'}{'$'}""")
}
