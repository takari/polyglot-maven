@Scope class Build {
    var sourceDirectory: String = "src/main/java"
    var testSourceDirectory: String = "src/test/java"
    lateinit var finalName: String

    protected var plugs: Plugins? = null
        set(value) {
            check(plugs == null, { "Plugins is already defined" })
            field = value
        }
    operator fun component1() = plugs

    fun plugins(block: (@Scope Plugins).() -> Unit) {
        plugs = Plugins()
        block(plugs!!)
    }

    protected var pluginManagement: PluginManagement? = null
    operator fun component2() = pluginManagement
    fun pluginManagement(block: (@Scope PluginManagement).() -> Unit) {
        pluginManagement = PluginManagement(Plugins())
        block(pluginManagement!!)
    }
}