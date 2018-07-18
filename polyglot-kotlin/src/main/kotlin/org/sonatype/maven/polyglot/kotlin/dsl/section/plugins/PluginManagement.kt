class PluginManagement(protected val plugins: Plugins) {
    operator fun component1() = plugins

    fun plugins(block: (@Scope Plugins).() -> Unit) {
        block(plugins)
    }
}