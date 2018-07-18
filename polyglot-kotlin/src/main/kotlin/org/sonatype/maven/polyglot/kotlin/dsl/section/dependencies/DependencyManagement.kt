class DependencyManagement(private val dependencies: Dependencies) {
    operator fun component1() = dependencies

    fun dependencies(block: (@Scope Dependencies).() -> Unit) {
        block(dependencies)
    }
}