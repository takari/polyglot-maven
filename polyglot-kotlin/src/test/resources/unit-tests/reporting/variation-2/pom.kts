project {
    reporting {
        excludeDefaults(false)
        outputDirectory("target/reports")
        plugins {
            plugin {
                groupId("org.codehaus.reports")
                artifactId("reports-maven-plugin")
                version("1.2.3-SNAPSHOT")
                inherited(true)
                reportSets {
                    reportSet {
                        id("reports")
                        reports("issues", "dependencies", "coverage")
                        inherited(false)
                        configuration = """
                            <configuration>
                              <args>The &quot;configuration&quot; section consists of arbitrary XML</args>
                            </configuration>
                            """
                    }
                }
                configuration = """
                    <configuration>
                      <args>The &quot;configuration&quot; section consists of arbitrary XML</args>
                    </configuration>
                    """
            }
        }
    }
}