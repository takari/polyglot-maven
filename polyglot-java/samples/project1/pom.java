public class pom extends org.sonatype.maven.polyglot.java.dsl.ModelFactory {

	@SuppressWarnings({ "unchecked" })
	public void project() {

		modelVersion = "4.0.0";
		groupId = "org.sample1";
		artifactId = "project1";
		packaging = "jar";
		version = "1.0-SNAPSHOT";

		properties(
			property(name1 -> "property_1"),
			property(name2 -> "property_2")
		);

		dependencies(
			dependency(groupId -> "junit", artifactId -> "junit", version -> "3.8.1", scope -> "test")
		);	
	}
}