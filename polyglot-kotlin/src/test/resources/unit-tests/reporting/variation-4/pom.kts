project {
    profiles {
        profile {
            id("other")
            activation {
                activeByDefault()
            }
            reporting {
                excludeDefaults(false)
                outputDirectory("target/reports")
                plugins {
                    plugin("org.codehaus.reports:reports-maven-plugin:1.2.3-SNAPSHOT") {
                        inherited(true)
                        reportSets {
                            reportSet("reports") {
                                reports("issues", "dependencies", "coverage")
                                inherited(true)
                                configuration = null
                            }
                        }
                        configuration = null
                    }
                }
            }
        }
    }
}