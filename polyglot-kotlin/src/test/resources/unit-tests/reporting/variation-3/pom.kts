project {
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
                        configuration("""<args>The &quot;configuration&quot; section consists of arbitrary XML</args>""")
                    }
                }
                configuration("""<args>The &quot;configuration&quot; section consists of arbitrary XML</args>""")
            }
        }
    }
}