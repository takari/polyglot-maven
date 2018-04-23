package org.sonatype.maven.polyglot.kotlin

import org.apache.maven.model.Model
import org.apache.maven.model.Parent
import org.apache.maven.model.io.ModelWriter
import org.codehaus.plexus.component.annotations.Component
import org.sonatype.maven.polyglot.io.ModelWriterSupport
import org.sonatype.maven.polyglot.kotlin.generator.KomDependencyGenerator.compile
import org.sonatype.maven.polyglot.kotlin.generator.KomDependencyGenerator.import
import org.sonatype.maven.polyglot.kotlin.generator.KomDependencyGenerator.provided
import org.sonatype.maven.polyglot.kotlin.generator.KomDependencyGenerator.system
import org.sonatype.maven.polyglot.kotlin.generator.KomDependencyGenerator.test
import org.sonatype.maven.polyglot.kotlin.generator.nextLine
import org.sonatype.maven.polyglot.kotlin.generator.tab
import java.io.Writer
import java.util.*

@Component(role = ModelWriter::class, hint = "kotlin")
class KotlinModelWriter : ModelWriterSupport() {

    override fun write(output: Writer, options: MutableMap<String, Any>, model: Model) {
        with(output) {
            with(model) {
                appendln("project {")
                if (name != null) appendln(tab("name" assign name))
                appendln(tab + if (parent == null) "groupId" assign groupId else "parent" assign parent.gav())
                appendln(tab("artifactId" assign artifactId))
                appendln(tab("packaging = $packaging"))
                if(properties.isNotEmpty())
                    append(tab("properties {"))
                            .appendln(properties.lineByLine { name, value -> tab(name sameAs value, 2) })
                            .appendln(tab("}"))
                if (dependencies.isNotEmpty())
                    appendln(tab("dependencies {"))
                            .append(import(dependencies))
                            .append(compile(dependencies))
                            .append(test(dependencies))
                            .append(provided(dependencies))
                            .append(system(dependencies))
                            .appendln(tab("}"))
                append("}")
            }
        }
    }

    fun Parent.gav(): String = "$groupId:$artifactId:$version"
    infix fun String.assign(value: Any): String = "$this = \"$value\""
    infix fun Any.sameAs(value: Any) = "\"$this\" sameAs \"$value\""
    inline fun Properties.lineByLine(expression: (name: Any, value: Any) -> String): String {
        val lines = StringBuilder(this.size * 13)
        for (prop in this) lines.append(nextLine + expression(prop.key, prop.value))
        return lines.toString()
    }
}

