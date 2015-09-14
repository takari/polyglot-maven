project {

  parent {
    artifactId 'b'
    groupId 'a'
    version 'c'
  }
    
  build {
    $execute(id: 'test1', phase: 'compile') {
      println 'hi'
    }
  }
}