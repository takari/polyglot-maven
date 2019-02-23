project {
    profiles {
        profile {
            id("other")
            activation {
                activeByDefault()
            }
            repositories {
                repository("Demo Main Repository") {
                    id("mvn-main-repo")
                    url("https://repo.example.com/mvn-main-repo")
                    layout("default")
                    releases {
                        enabled(true)
                        updatePolicy("never")
                        checksumPolicy("warn")
                    }
                    snapshots {
                        enabled(false)
                        updatePolicy("interval:60")
                        checksumPolicy("ignore")
                    }
                }
            }
        }
    }
}