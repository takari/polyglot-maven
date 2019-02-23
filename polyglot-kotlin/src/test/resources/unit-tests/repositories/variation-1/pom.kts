project {
    repositories {
        repository {
            id = "mvn-main-repo"
            name = "Demo Main Repository"
            url = "https://repo.example.com/mvn-main-repo"
            layout = "custom"
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