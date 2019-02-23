package org.sonatype.maven.polyglot.kotlin

import org.apache.maven.model.Model
import org.apache.maven.model.io.ModelReader
import org.codehaus.plexus.component.annotations.Component
import org.codehaus.plexus.component.annotations.Requirement
import org.sonatype.maven.polyglot.execute.ExecuteManager
import org.sonatype.maven.polyglot.io.ModelReaderSupport
import org.sonatype.maven.polyglot.kotlin.dsl.Project
import org.sonatype.maven.polyglot.kotlin.engine.singletonEngineFactory
import java.io.Reader
import javax.script.ScriptException

@Component(role = ModelReader::class, hint = "kotlin")
class KotlinModelReader : ModelReaderSupport() {

    @Requirement
    private var executeManager: ExecuteManager? = null

    override fun read(pomReader: Reader, options: Map<String, *>): Model {
        val model = try {
            (singletonEngineFactory.scriptEngine.eval(pomReader) as Model?) ?: Model()
        } catch (ex: ScriptException) {
            ex.printStackTrace()
            throw ex.cause ?: ex
        }
        if (model is Project) {
            val tasks = ArrayList(model.tasks)
            executeManager?.register(model, tasks)
            executeManager?.install(model, options)
            model.tasks.clear()
        }
        return model
    }
}
