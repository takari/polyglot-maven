//package org.sonatype.maven.polyglot.kotlin
//
//import of.Project
import org.apache.maven.model.Model
import org.apache.maven.model.io.ModelReader
import org.codehaus.plexus.PlexusContainer
import org.codehaus.plexus.component.annotations.Component
import org.codehaus.plexus.component.annotations.Requirement
import org.codehaus.plexus.logging.Logger
import org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngineFactory
import org.sonatype.maven.polyglot.io.ModelReaderSupport
import java.io.Reader

@Component(role = ModelReader::class, hint = "kotlin")
class KotlinModelReader : ModelReaderSupport() {
    @Requirement
    private val container: PlexusContainer? = null

    @Requirement
    protected var log: Logger? = null

    private val scriptEngine = KotlinJsr223JvmLocalScriptEngineFactory().getScriptEngine()

    override fun read(komReader: Reader, options: MutableMap<String, *>): Model {
        val project = scriptEngine.eval(komReader) as Project

        return KomConverter.toModel(project)
    }
}