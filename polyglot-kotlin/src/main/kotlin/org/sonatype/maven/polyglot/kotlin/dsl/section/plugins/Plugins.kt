@Scope class Plugins {
    protected val plugins = mutableListOf<Plugin>()
    operator fun component1():List<Plugin> = plugins

    fun plugin(artifact: String) {
        val artifactSegments = artifact.split(":")
        check(artifactSegments.size == 3, { "Wrong plugin format. Expected: groupId:artifactId:version" })
        plugin(artifactSegments[0], artifactSegments[1], artifactSegments[2])
    }

    fun plugin(groupId: String = "org.apache.maven.plugins", artifactId: String, version: String): Plugin {
        val plugin = Plugin(groupId, artifactId, version)
        plugins.add(plugin)
        return plugin
    }

    fun plugin(artifact: String, block: (@Scope Plugin).() -> Unit) {
        val artifactSegments = artifact.split(":")
        check(artifactSegments.size == 3, { "Wrong plugin format. Expected: groupId:artifactId:version" })
        plugin(artifactSegments[0], artifactSegments[1], artifactSegments[2], block)
    }

    fun plugin(groupId: String = "org.apache.maven.plugins", artifactId: String, version: String, block: (@Scope Plugin).() -> Unit) {
        val plugin = Plugin(groupId, artifactId, version)
        block(plugin)
        plugins.add(plugin)
    }

    @Scope class Plugin(val groupId: String, val artifactId: String, val version: String) {
        var inherited: Boolean = true
        protected var pluginExecutions: Executions? = null
        operator fun component1() = pluginExecutions
        protected var configuration: Configuration? = null
        operator fun component2() = configuration
        protected var dependencies: PluginDependencies? = null
        operator fun component3() = dependencies

        fun executions(block: (@Scope Executions).() -> Unit) {
            check(pluginExecutions == null, { "Executions are already defined" })
            pluginExecutions = Executions()
            block(pluginExecutions!!)
        }

        fun nonInherited(): Plugin {
            inherited = false
            return this
        }

        fun configuration(block: (@Scope Configuration).() -> Unit) {
            configuration = Configuration()
            block(configuration!!)
        }

        fun dependencies(block: (@Scope PluginDependencies).() -> Unit) {
            dependencies = PluginDependencies()
            block(dependencies!!)
        }
    }
}
