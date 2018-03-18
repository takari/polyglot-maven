project {
    name = "Polyglot :: Kotlin"
    parent {
        groupId = "io.takari.polyglot"
        artifactId = "polyglot"
        version = "0.2.2-SNAPSHOT"
    }

    artifactId = "polyglot-kotlin"

    modules[
        "polyglot - common",
        "polyglot - atom",
        "polyglot - ruby",
        "polyglot - scala",
        "polyglot - groovy",
        "polyglot - yaml",
        "polyglot - clojure",
        "polyglot - xml",
        "polyglot - java",
        "polyglot - kotlin",
        "polyglot - maven - plugin",
        "polyglot - translate - plugin"
    ]
}