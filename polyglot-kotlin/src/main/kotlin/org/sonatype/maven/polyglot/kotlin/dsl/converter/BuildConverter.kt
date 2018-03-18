import PluginConverter.pluginsOf

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
}