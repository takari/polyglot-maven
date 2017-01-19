project {
    build {
        $execute(id: 'test1', phase: 'compile') {ec ->
            println 'hi'
        }
    }
}