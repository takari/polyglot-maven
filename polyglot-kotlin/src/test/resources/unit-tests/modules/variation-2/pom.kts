project {
    profiles {
        profile {
            id("other")
            activation {
                activeByDefault()
            }
            modules(
                "demo-kotlin-library",
                "demo-kotlin-plugin",
                "demo-kotlin-app",
                "demo-kotlin-parent"
            )
        }
    }
}