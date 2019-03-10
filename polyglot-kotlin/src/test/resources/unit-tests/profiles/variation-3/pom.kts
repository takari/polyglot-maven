project {
    profiles {
        profile("profileA1") {
            activation {
                activeByDefault(true)
            }
        }
        profile("profileA2") {
            activation {
                activeByDefault(false)
            }
        }
        profile("profileB") {
            activation {
                os {
                    arch("x86")
                    family("Windows")
                    name("Windows XP")
                    version("5.1.2.3")
                }
            }
        }
        profile("profileC") {
            activation {
                file {
                    exists("fileA")
                    missing("fileB")
                }
            }
        }
        profile("profileD") {
            activation {
                property {
                    name("propA")
                    value("true")
                }
            }
        }
        profile("profileE") {
            activation {
                jdk("11")
            }
        }
    }
}