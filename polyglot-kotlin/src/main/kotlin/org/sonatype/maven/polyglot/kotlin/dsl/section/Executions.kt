class Executions {
    protected val executions = mutableListOf<Execution>()
    operator fun component1(): List<Execution> = executions

    fun execution(id: String = "default", phase: String? = null, goal: String) {
        executions.add(Execution(id, phase, arrayOf(goal)))
    }

    fun execution(id: String = "default", phase: String? = null, goal: String, block: Execution.() -> Unit) {
        executions.add(Execution(id, phase, arrayOf(goal)))
    }

    fun execution(id: String = "default", phase: String? = null, goals: Array<String>) {
        executions.add(Execution(id, phase, goals))
    }

    fun execution(id: String = "default", phase: String? = null, goals: Array<String>, block: Execution.() -> Unit) {
        val execution = Execution(id, phase, goals)
        block(execution)
        executions.add(execution)
    }

    fun execution(id: String = "default", phase: String? = null, block: Execution.() -> Unit) {
        val execution = Execution(id, phase, emptyArray())
        block(execution)
        executions.add(execution)
    }

    class Execution(val id: String, val phase: String?, var goals: Array<String>) {
        protected var configuration: Configuration? = null
        operator fun component1() = configuration

        fun configuration(block: (@Scope Configuration).() -> Unit) {
            configuration = Configuration()
            block(configuration!!)
        }
    }
}