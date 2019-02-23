project {
    repositories {
        repository {
            id("mvn-main-repo")
            name("Demo Main Repository")
            url("https://repo.example.com/mvn-main-repo")
            layout("custom")
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