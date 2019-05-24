project {
    build {
        execute(id = "hello", phase = "validate", profile = "coverage") {
            log.info("Hello World!")
        }
    }
}
