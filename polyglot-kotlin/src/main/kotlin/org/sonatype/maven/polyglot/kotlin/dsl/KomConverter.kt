//package org.sonatype.maven.polyglot.kotlin.dsl
//
//import of.Project
import org.apache.maven.model.Model
import org.apache.maven.model.Parent

object KomConverter {

    fun toModel(project: Project): Model {
        val model = Model()

        model.name = project.name
        model.description = project.description

        model.modelVersion = project.modelVersion
        model.parent = parentOf(project)

        model.artifactId = project.artifactId
        model.groupId = project.groupId ?: model.parent.groupId
        model.version = project.version ?: model.parent.version
        model.packaging = project.packaging


        model.url = project.url

        model.inceptionYear = project.inceptionYear

//        model.properties = project.properties
        return model
    }

    private fun parentOf(project: Project): Parent {
        val parentSegments = project.parent.split(":")
        check(parentSegments.size == 3, { "Wrong Project.parent format. Expected: groupId:artifactId:version" })

        val parent = Parent()
        parent.groupId = parentSegments[0]
        parent.artifactId = parentSegments[1]
        parent.version = parentSegments[2]
        return parent
    }
}