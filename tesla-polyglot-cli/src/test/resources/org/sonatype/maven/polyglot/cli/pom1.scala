/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
project { m =>
  m.packaging = "jar"  
  m.parent { p =>  
    p.artifactId = "b"    
    p.groupId = "a"    
    p.relativePath = "../pom.xml"    
    p.version = "c"    
  }  
}
