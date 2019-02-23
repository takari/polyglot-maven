project {
    pluginRepositories {
        pluginRepository {
            id = "mvn-plugin-repo"
            name = "Demo Plugin Repository"
            url = "https://repo.example.com/mvn-plugin-repo"
            layout = "legacy"
            releases {
                isEnabled = true
                updatePolicy = "never"
                checksumPolicy = "warn"
            }
            snapshots {
                isEnabled = false
                updatePolicy = "interval:60"
                checksumPolicy = "ignore"
            }
        }
    }
}