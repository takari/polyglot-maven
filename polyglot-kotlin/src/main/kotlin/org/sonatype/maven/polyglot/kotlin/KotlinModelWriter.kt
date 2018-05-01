package org.sonatype.maven.polyglot.kotlin

import org.apache.maven.model.Model
import org.apache.maven.model.io.ModelWriter
import org.apache.maven.shared.utils.xml.Xpp3Dom
import org.codehaus.plexus.component.annotations.Component
import org.sonatype.maven.polyglot.io.ModelWriterSupport
import org.sonatype.maven.polyglot.kotlin.writer.*
import org.sonatype.maven.polyglot.kotlin.writer.KomDependencyWriter.compile
import org.sonatype.maven.polyglot.kotlin.writer.KomDependencyWriter.import
import org.sonatype.maven.polyglot.kotlin.writer.KomDependencyWriter.provided
import org.sonatype.maven.polyglot.kotlin.writer.KomDependencyWriter.runtime
import org.sonatype.maven.polyglot.kotlin.writer.KomDependencyWriter.system
import org.sonatype.maven.polyglot.kotlin.writer.KomDependencyWriter.test
import java.io.Writer

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
                            .append(runtime(dependencies))
                            .append(system(dependencies))
                            .appendln(tab("}"))
                if (build != null) {
                    with(build) {
                        appendln(tab("build {"))
                                .appendlnIf(sourceDirectory, { tab("sourceDirectory" assign it, 2) })
                                .appendlnIf(testSourceDirectory, { tab("testSourceDirectory" assign it, 2) })
                                .appendlnIf(finalName, { tab("finalName" assign it, 2) })
                                .appendlnIf(scriptSourceDirectory, { tab("scriptSourceDirectory" assign it, 2) })
                                .appendlnIf(outputDirectory, { tab("outputDirectory" assign it, 2) })
                                .appendlnIf(testOutputDirectory, { tab("testOutputDirectory" assign it, 2) })
                                .appendlnIf(directory, { tab("directory" assign it, 2) })
                                .appendlnIf(filters, { tab("filters[${filters.quoted()}]", 2) })
                        if (plugins.isNotEmpty()) {
                            appendln(tab("plugins {", 2))
                                plugins.forEach {
                                    appendln(tab("plugin(${it.id.quoted()}) {", 3))
                                    if (it.executions.isNotEmpty()) {
                                        appendln(tab("executions {", 4))
                                        it.executions.forEach {
                                            appendln(tab("execution(id = ${it.id.quoted()}, phase = ${it.phase.quoted()}, goals = arrayOf(${it.goals.quoted()}))", 5))
                                        }
                                        appendln(tab("}", 4))
                                    }
                                    if (it.dependencies.isNotEmpty())
                                        appendln(tab("dependencies {", 4))
                                                .append(compile(it.dependencies, 5))
                                                .append(runtime(it.dependencies, 5))
                                                .append(system(it.dependencies, 5))
                                                .appendln(tab("}", 4))
                                    if (it.configuration != null)
                                        appendln(tab("configuration {", 4))
                                                .append((it.configuration as Xpp3Dom).children.toKonfig())
                                                .appendln(tab("}", 4))
                                    appendln(tab("}", 3))
                                }
                            appendln(tab("}", 2))
                        }
                        appendln(tab("}"))
                    }
                }
                append("}")
            }
        }
    }
}

