/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
project { p =>

  // contributor added with just a name
  p.contributor("John Lennon")
  
  // contributor added with a closure, supporting setting multiple
  // properties on the contribitor object.
  p.contributor { c =>
    c.name = "Elvis Presley"
    c.email = "elvis@graceland.com"
    c.properties += ("isAlive" -> "false", "lovesPeanutButterAndBacon" -> "true")
  }
  
}