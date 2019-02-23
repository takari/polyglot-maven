project {
    profiles {
        profile {
            id("other")
            activation {
                activeByDefault()
            }
            pluginRepositories {
                pluginRepository("Demo Plugin Repository") {
                    id("mvn-plugin-repo")
                    url("https://repo.example.com/mvn-plugin-repo")
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