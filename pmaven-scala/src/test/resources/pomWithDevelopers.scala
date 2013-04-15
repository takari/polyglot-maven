project { p =>

  // developer added with ID as a parameter
  p.developer("bmaso")
  
  // developer added with a closure
  p.developer { 
    _.id = "hseeberger"
  }
  
}