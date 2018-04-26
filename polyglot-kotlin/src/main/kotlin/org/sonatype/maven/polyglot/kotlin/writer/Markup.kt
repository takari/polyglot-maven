package org.sonatype.maven.polyglot.kotlin.writer

import org.apache.maven.model.Parent
import java.util.*

val nextLine = System.lineSeparator()

val tab = "    "
fun tab(text: String = "", count: Int = 1): String = tab.repeat(count) + text

fun String.quoted(): String = "\"$this\""
fun List<String>.quoted(): String = joinToString(prefix = "\"", separator = "\", \"", postfix = "\"")


fun Parent.gav(): String = "$groupId:$artifactId:$version"
infix fun String.assign(value: Any): String = "$this = \"$value\""
infix fun Any.sameAs(value: Any) = "\"$this\" sameAs \"$value\""
inline fun Properties.lineByLine(expression: (name: Any, value: Any) -> String): String {
    val lines = StringBuilder(this.size * 13)
    for (prop in this) lines.append(nextLine + expression(prop.key, prop.value))
    return lines.toString()
}

inline fun Appendable.appendlnIf(value: Any?, block: (theValue: Any) -> String): Appendable =
        if (value == null || (value is Collection<*> && value.isEmpty())) this else appendln(block(value))