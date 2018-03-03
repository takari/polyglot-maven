import org.apache.maven.model.Plugin
import org.apache.maven.model.PluginExecution

object BuildConverter {

    fun buildOf(projectBuild: Build): org.apache.maven.model.Build {
        return org.apache.maven.model.Build().apply {
            sourceDirectory = projectBuild.sourceDirectory
            testSourceDirectory = projectBuild.testSourceDirectory
            finalName = projectBuild.finalName

            val (plugs) = projectBuild
            if (plugs != null) plugins = pluginsOf(plugs.component1())
        }
    }

    private fun pluginsOf(projectPlugins: List<Plugins.Plugin>):List<Plugin> {
        return projectPlugins.map {
            Plugin().apply {
                artifactId = it.artifactId
                groupId = it.groupId
                version = it.version

                val (execs) = it
                execs?.component1()?.forEach {
                    executions.add(PluginExecution().apply {
                        id = it.id
                        phase = it.phase
                        goals = it.goals.toList()

                    })
                }
            }
        }
    }
}