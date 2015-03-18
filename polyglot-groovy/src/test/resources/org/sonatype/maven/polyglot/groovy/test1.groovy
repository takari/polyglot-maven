/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
project {
  
  parent {
    groupId "io.tesla"
    artifactId "tesla-core"
    version "1.0.0-SNAPSHOT"
  }
    
  build {
    $execute(id: 'test1', phase: 'compile') {
      println 'hi'
    }
  }
}