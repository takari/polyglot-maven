package org.sonatype.maven.polyglot.kotlin

import org.apache.maven.model.Model
import org.apache.maven.model.Parent
import org.apache.maven.model.io.ModelWriter
import org.codehaus.plexus.component.annotations.Component
import org.sonatype.maven.polyglot.io.ModelWriterSupport
import java.io.Writer
import java.util.*

@Component(role = ModelWriter::class, hint = "kotlin")
class KotlinModelWriter : ModelWriterSupport() {
    val nextLine = System.lineSeparator()

    override fun write(output: Writer, options: MutableMap<String, Any>, model: Model) {
        with(output) {
            with(model) {
append("""
project {
    ${ifExists(name) { "name" assign it }}
    ${if (parent == null) "groupId" assign groupId else "parent" assign parent.gav() }
    artifactId = "$artifactId"
    packaging = $packaging${ifNonEmpty(properties) {"""
    properties {${properties.lineByLine { name, value -> name sameAs value}}
    }"""}}
}
""".trimIndent())
            }
        }
    }

    fun Parent.gav(): String = "$groupId:$artifactId:$version"

    inline fun ifExists(subject: Any?, valueExtractor: (Any) -> String): String = if (subject != null) valueExtractor(subject) else ""
    inline fun ifNonEmpty(subject: Properties?, valueExtractor: (Any) -> String): String
            = if (subject != null && !subject.isEmpty) valueExtractor(subject) else ""
    infix fun String.assign(value: Any): String = "$this = \"$value\""
    infix fun Any.sameAs(value: Any) = "\"$this\" sameAs \"$value\""
    inline fun Properties.lineByLine(expression: (name: Any, value: Any) -> String): String {
        var lines: String = ""
        for (prop in this) lines += nextLine + "        " + expression(prop.key, prop.value)
        return lines
    }
}

