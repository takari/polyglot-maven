package org.sonatype.maven.polyglot.kotlin.support

fun anyOf(vararg values: Any?, predicate: (Any?) -> Boolean): Boolean {
    return values.map(predicate).firstOrNull { it } ?: false
}

val isNull: (Any?) -> Boolean = { it == null }

val isNotNull: (Any?) -> Boolean = { it != null }
