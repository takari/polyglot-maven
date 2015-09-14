project {
    build {
        $execute(id: 'test1', phase: 'compile') {
            println 'hi'
        }
    }
}