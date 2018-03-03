class Executions {
    protected val executions = mutableListOf<Execution>()
    operator fun component1(): List<Execution> = executions

    fun execution(id: String = "default", phase: String, goal: String) {
        executions.add(Execution(id, phase, arrayOf(goal)))
    }

    fun execution(id: String = "default", phase: String, goals: Array<String>) {
        executions.add(Execution(id, phase, goals))
    }

    fun execution(id: String = "default", phase: String, block: Execution.() -> Unit) {
        val execution = Execution(id, phase, emptyArray())
        block(execution)
        executions.add(execution)
    }

    inner class Execution(val id: String, val phase: String, var goals: Array<String>)
}