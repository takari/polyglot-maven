project {
    distributionManagement {
        repository("example-releases") {
            name("Example Release Repository")
            url("http://repo.example.com/maven/releases/")
            layout("legacy")
            uniqueVersion(false)
        }
        snapshotRepository("example-snapshots") {
            name("Example Snapshot Repository")
            url("http://repo.example.com/maven/snapshots/")
        }
        site("example-documents") {
            name("Example Documents")
            url("http://repo.example.com/maven/documents/")
        }
        downloadUrl("http://repo.example.com/maven/artifacts")
        relocation("org.example.polyglot:polyglot-kotlin-demo:1.0.1") {
            message("The group has been renamed")
        }
        status("none")
    }
}