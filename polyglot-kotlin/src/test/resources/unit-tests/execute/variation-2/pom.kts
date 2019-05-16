project {
    build {
        execute(id = "hello", phase = "validate", profile = "coverage") {
            println("Hello World!")
        }
    }
}
