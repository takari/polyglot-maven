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
    private lateinit var log: Logger

    @Requirement(optional = true)
    private var project: MavenProject? = null

    override fun write(output: Writer, options: Map<String, Any>, model: Model) {
        output.write(with(StringWriter(1024)) {
            val config = HashMap<String, Any>(options)
            config.putIfAbsent("xml.dsl.enabled", project?.properties?.getProperty("polyglot-kotlin.xml-dsl-enabled", "true") ?: "true")
            config.putIfAbsent("flavor", project?.properties?.getProperty("polyglot-kotlin.flavor", "mixed") ?: "mixed")
            ModelScriptWriter(this, config).write(model)
            val kotlinScript = toString()
            if (log.isDebugEnabled) {
                log.debug(({ "POM model converted from XML: \n$kotlinScript\n" })())
            }
            kotlinScript
        })
        output.flush()
    }

}
