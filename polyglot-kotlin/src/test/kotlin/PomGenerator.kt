object PomGenerator {

    fun pluginWithConfiguration(xmlConfigContent: String): String {
        return """
            <project>
                <build>
                    <plugins>
                        <plugin>
                            <groupId>org.apache.maven.plugins</groupId>
                            <artifactId>maven-surefire-plugin</artifactId>
                            <configuration>
                                $xmlConfigContent
                            </configuration>
                        </plugin>
                    </plugins>
                </build>
            </project>
            """
    }
}