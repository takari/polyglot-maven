project {
    reporting {
        excludeDefaults = "false"
        outputDirectory = "target/reports"
        plugins {
            plugin {
                groupId = "org.codehaus.reports"
                artifactId = "reports-maven-plugin"
                version = "1.2.3-SNAPSHOT"
                isInherited = true
                reportSets {
                    reportSet {
                        id = "reports"
                        reports = listOf("issues", "dependencies", "coverage")
                        isInherited = false
                        configuration {
                            "args" to """The "configuration" section consists of arbitrary XML"""
                        }
                    }
                }
                configuration {
                    "args" to """The "configuration" section consists of arbitrary XML"""
                }
            }
        }
    }
}