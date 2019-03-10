project {
    profiles {
        profile {
            id("other")
            activation {
                activeByDefault()
            }
            distributionManagement {
                repository("Example Release Repository") {
                    id("example-releases")
                    url("http://repo.example.com/maven/releases/")
                    layout("legacy")
                    uniqueVersion(false)
                }
                snapshotRepository("Example Snapshot Repository") {
                    id("example-snapshots")
                    url("http://repo.example.com/maven/snapshots/")
                }
                site("Example Documents") {
                    id("example-documents")
                    url("http://repo.example.com/maven/documents/")
                }
                downloadUrl("http://repo.example.com/maven/artifacts")
                relocation("org.example.polyglot:polyglot-kotlin-demo:1.0.1") {
                    message("The group has been renamed")
                }
                status("none")
            }
        }
    }
}