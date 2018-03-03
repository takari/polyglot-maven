@Scope class Build {
    lateinit var sourceDirectory: String
    lateinit var testSourceDirectory: String
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
}