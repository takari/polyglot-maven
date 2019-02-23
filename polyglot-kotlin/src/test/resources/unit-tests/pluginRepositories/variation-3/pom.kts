project {
    pluginRepositories {
        pluginRepository("mvn-plugin-repo") {
            name("Demo Plugin Repository")
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