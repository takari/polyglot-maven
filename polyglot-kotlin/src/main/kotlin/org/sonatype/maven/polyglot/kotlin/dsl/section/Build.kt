@Scope class Build {
    var sourceDirectory = "src/main/java"
    var scriptSourceDirectory = "src/main/scripts"
    var testSourceDirectory: String = "src/test/java"
    var outputDirectory = "target/classes"
    var testOutputDirectory = "target/test-classes"
    var finalName: String = "\${artifactId}-\${version}"
    var directory = "target"

    var filters = arrayOf<String>()
    operator fun Array<String>.get(vararg filters: String) {
        assert(this@Build.filters === this, {"Unexpected Build.filters DSL usage"})
        this@Build.filters = filters as Array<String>
    }

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