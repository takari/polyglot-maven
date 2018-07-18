class Activation(var default: Boolean = false, val jdk: String? = null) {
    protected var thisFile: File? = null
    operator fun component1() = thisFile
    protected var thisOs: os? = null
    operator fun component2() = thisOs
    protected var property: Pair<String, String>? = null
    operator fun component3() = property

    fun file(block: (@Scope File).() -> Unit) {
        thisFile = File()
        block(thisFile!!)
    }

    fun property(name: String, value: String) {
        check(property == null, { "property is defined several times" })
        property = Pair(name, value)
    }

    class File{
        var exists: String? = null
        var missing: String? = null
    }

    inner class os(val name: String?, val family: String?, val version: String?, val arch: String?) {
        init {
            check(thisOs == null, { "OS is defined several times" })
            thisOs = this
        }
    }

}