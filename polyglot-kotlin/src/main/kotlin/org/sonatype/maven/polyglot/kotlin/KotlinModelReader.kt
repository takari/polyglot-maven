package org.sonatype.maven.polyglot.kotlin

import org.apache.maven.model.Model
import org.apache.maven.model.io.ModelReader
import org.codehaus.plexus.component.annotations.Component
import org.codehaus.plexus.component.annotations.Requirement
import org.sonatype.maven.polyglot.execute.ExecuteManager
import org.sonatype.maven.polyglot.io.ModelReaderSupport
import org.sonatype.maven.polyglot.kotlin.dsl.Project
import org.sonatype.maven.polyglot.kotlin.engine.ScriptHost
import java.io.File
import java.io.InputStream
import java.io.Reader

@Component(role = ModelReader::class, hint = "kotlin")
class KotlinModelReader : ModelReader {

    @Requirement
    private var executeManager: ExecuteManager? = null

    override fun read(input: File, options: Map<String, *>): Model {
        val project = Project(input)
        ScriptHost.eval(input, project)
        val tasks = ArrayList(project.tasks)
        executeManager?.register(project, tasks)
        executeManager?.install(project, options)
        project.tasks.clear() // Must be cleared or Maven goes into an infinitely repeatable introspection
        return project
    }

    override fun read(input: Reader, options: MutableMap<String, *>): Model {
        val temp = File.createTempFile("pom", ".kts")
        temp.deleteOnExit()
        temp.writer().use { input.copyTo(it) }
        return read(temp, options)
    }

    override fun read(input: InputStream, options: MutableMap<String, *>): Model {
        val temp = File.createTempFile("pom", ".kts")
        temp.deleteOnExit()
        temp.outputStream().use { input.copyTo(it) }
        return read(temp, options)
    }
}
