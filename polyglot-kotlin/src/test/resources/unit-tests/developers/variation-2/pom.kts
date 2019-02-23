project {
    developers {
        developer {
            id("thorntonrp")
            name("Robert Thornton")
            email("thorntonrp@example.com")
            url("https://github.com/thorntonrp")
            organization("Demo Organization")
            organizationUrl("http://www.example.com")
            roles("Software Engineer", "Technical Lead")
            timezone("UTC")
            properties {
                "phone" to "1-XXX-XXX-XXXX"
            }
        }
        developer("johndoe") {
            name("jdoe")
            email("jdoe@example.com")
            url("https://github.com/johndoe13456")
            organization("Demo Organization")
            organizationUrl("http://www.example.com")
            roles("Software Engineer", "Technical Lead")
            timezone("UTC")
            properties {
                "phone" to "1-XXX-XXX-XXXX"
            }
        }
        developer("Daffy Duck <daffy@ldschurch.org>")
        developer("Bugs Bunny <bugs@ldschurch.org>") {
            id("carrots")
        }
        developer("elmer.fudd@ldschurch.org") {
            name("Elmer Fudd")
        }
    }
}