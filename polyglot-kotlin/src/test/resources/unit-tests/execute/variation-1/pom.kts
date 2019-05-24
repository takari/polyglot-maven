project {
    build {
        execute(id = "hello", phase = "validate") {
            log.info("Hello World!")
        }
    }
}
