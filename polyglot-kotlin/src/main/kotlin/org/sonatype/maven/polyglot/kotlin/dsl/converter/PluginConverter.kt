import ConfigurationConverter.configurationOf
import org.apache.maven.model.Plugin
import org.apache.maven.model.PluginExecution

object PluginConverter {
    fun pluginsOf(projectPlugins: List<Plugins.Plugin>): List<Plugin> {
        return projectPlugins.map {
            Plugin().apply {
                artifactId = it.artifactId
                groupId = it.groupId
                version = it.version

                val (execs, config, deps) = it
                execs?.component1()?.forEach {
                    executions.add(PluginExecution().apply {
                        id = it.id
                        phase = it.phase
                        goals = it.goals.toList()
                    })
                }

                configuration = config?.let { configurationOf(it) }

                deps?.component1()?.map { MetaDependency(it) }?.let {
                    dependencies = DependencyConverter.dependenciesOf(it)
                }
            }
        }
    }
}