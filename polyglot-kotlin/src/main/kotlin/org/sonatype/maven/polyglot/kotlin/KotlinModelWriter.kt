package org.sonatype.maven.polyglot.kotlin

import org.apache.maven.model.Model
import org.apache.maven.model.io.ModelWriter
import org.apache.maven.project.MavenProject
import org.codehaus.plexus.component.annotations.Component
import org.codehaus.plexus.component.annotations.Requirement
import org.codehaus.plexus.logging.Logger
import org.sonatype.maven.polyglot.io.ModelWriterSupport
import org.sonatype.maven.polyglot.kotlin.serialization.ModelScriptWriter
import java.io.StringWriter
import java.io.Writer

@Component(role = ModelWriter::class, hint = "kotlin")
class KotlinModelWriter : ModelWriterSupport() {

    @Requirement
    private var project: MavenProject? = null

    @Requirement
    private var _log: Logger? = null

    override fun write(output: Writer, options: Map<String, Any>, model: Model) {
        output.write(with(StringWriter(1024)) {
            val config = HashMap<String, Any>(options)
            config.putIfAbsent("xml.dsl.enabled", project?.properties?.getProperty("polyglot-kotlin.xml-dsl-enabled", "true") ?: "true")
            config.putIfAbsent("flavor", project?.properties?.getProperty("polyglot-kotlin.flavor", "mixed") ?: "mixed")
            config.putIfAbsent("sample.executions", project?.properties?.getProperty("polyglot-kotlin.sample-executions", "false") ?: "false")
            ModelScriptWriter(this, config).write(model)
            val kotlinScript = toString()
            debug { "POM model converted from XML: \n$kotlinScript\n" }
            kotlinScript
        })
        output.flush()
    }

    private fun debug(message: () -> String) {
        val log = _log
        if (log != null && log.isDebugEnabled) {
            log.debug(message())
        }
    }
}
