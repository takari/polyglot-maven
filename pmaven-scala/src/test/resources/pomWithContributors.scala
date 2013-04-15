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