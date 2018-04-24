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
                appendlnIf(name, { tab("name" assign it) })
                appendln(tab + if (parent == null) "groupId" assign groupId else "parent" assign parent.gav())
                appendln(tab("artifactId" assign artifactId))
                appendln(tab("packaging = $packaging"))
                if (properties.isNotEmpty())
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
                if (build != null)
                    appendln(tab("build {"))
                            .appendlnIf(build.sourceDirectory, { tab("sourceDirectory" assign it, 2) })
                            .appendlnIf(build.testSourceDirectory, { tab("testSourceDirectory" assign it, 2) })
                            .appendlnIf(build.finalName, { tab("finalName" assign it, 2) })
                            .appendlnIf(build.scriptSourceDirectory, { tab("scriptSourceDirectory" assign it, 2) })
                            .appendlnIf(build.outputDirectory, { tab("outputDirectory" assign it, 2) })
                            .appendlnIf(build.testOutputDirectory, { tab("testOutputDirectory" assign it, 2) })
                            .appendlnIf(build.directory, { tab("directory" assign it, 2) })
                            if (build.filters.isNotEmpty()) appendln(tab("filters[" + build.filters
                                    .joinToString(prefix = "\"", postfix = "\"", separator = "\", \"") + "]", 2))
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

    inline fun Appendable.appendlnIf(value: Any?, block: (theValue: Any) -> String): Appendable =
            if (value != null) this.appendln(block(value)) else this
}

