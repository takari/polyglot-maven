package org.sonatype.maven.polyglot.kotlin.writer

import org.apache.maven.shared.utils.xml.Xpp3Dom
import java.io.StringWriter

fun Array<Xpp3Dom>.toKonfig(): String {
    val configurationWriter = StringWriter()
    for (element in this) write(element, configurationWriter, 5)
    return configurationWriter.toString()
}

private fun write(element: Xpp3Dom, configurationWriter: StringWriter, margin: Int) {
    with(configurationWriter) {
        if (element.value != null) appendln(tab("${element.name.quoted()} += ${element.value.quoted()}", margin))
        else if (element.children.isNotEmpty()) {
            val isValues = element.children.size > 1 && element.children.all { it.name == element.children[0].name }
            val begin = if (isValues) "[" else "{"
            val end = if (isValues) "]" else "}"
            append(tab(element.name.quoted(), margin)).append(" ").appendln(begin)
            if (isValues) {
                val values = element.children.map { it.value }
                appendln(values.joinToString(",$nextLine" + tab(count = margin + 1), tab(count = margin + 1)) {
                    it.quoted()
                })
            } else {
                for (child in element.children) write(child, configurationWriter, margin + 1)
            }
            appendln(tab(end, margin))
        } else appendln(tab("+" + element.name.quoted(), margin))
    }
}