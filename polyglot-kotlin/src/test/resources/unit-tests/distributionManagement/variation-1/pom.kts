project {
    distributionManagement {
        repository {
            id = "example-releases"
            name = "Example Release Repository"
            url = "http://repo.example.com/maven/releases/"
            layout = "legacy"
            isUniqueVersion = false
        }
        snapshotRepository {
            id = "example-snapshots"
            name = "Example Snapshot Repository"
            url = "http://repo.example.com/maven/snapshots/"
        }
        site {
            id = "example-documents"
            name = "Example Documents"
            url = "http://repo.example.com/maven/documents/"
        }
        downloadUrl = "http://repo.example.com/maven/artifacts"
        relocation {
            groupId = "org.example.polyglot"
            artifactId = "polyglot-kotlin-demo"
            version = "1.0.1"
            message = "The group has been renamed"
        }
        status = "none"
    }
}