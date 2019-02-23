project {
    profiles {
        profile {
            id("profileA1")
            activation {
                activeByDefault(true)
            }
        }
        profile {
            id("profileA2")
            activation {
                activeByDefault(false)
            }
        }
        profile {
            id("profileB")
            activation {
                os {
                    arch("x86")
                    family("Windows")
                    name("Windows XP")
                    version("5.1.2.3")
                }
            }
        }
        profile {
            id("profileC")
            activation {
                file {
                    exists("fileA")
                    missing("fileB")
                }
            }
        }
        profile {
            id("profileD")
            activation {
                property {
                    name("propA")
                    value("true")
                }
            }
        }
        profile {
            id("profileE")
            activation {
                jdk("11")
            }
        }
    }
}