project { m =>
  m.packaging = "jar"  
  m.parent { p =>  
    p.artifactId = "b"    
    p.groupId = "a"    
    p.relativePath = "../pom.xml"    
    p.version = "c"    
  }  
}
