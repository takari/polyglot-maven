@Scope class Plugins {
    protected val plugins = mutableListOf<Plugin>()
    operator fun component1():List<Plugin> = plugins

    fun plugin(groupId: String, artifactId: String, version: String) {
        plugins.add(Plugin(groupId, artifactId, version))
    }

    fun plugin(groupId: String, artifactId: String, version: String, block: (@Scope Plugin).() -> Unit) {
        val plugin = Plugin(groupId, artifactId, version)
        block(plugin)
        plugins.add(plugin)
    }

    @Scope class Plugin(val groupId: String, val artifactId: String, val version: String) {
        protected var pluginExecutions: Executions? = null
        operator fun component1() = pluginExecutions

        fun executions(block: (@Scope Executions).() -> Unit) {
            check(pluginExecutions == null, { "Executions are already defined" })
            pluginExecutions = Executions()
            block(pluginExecutions!!)
        }
    }
}
