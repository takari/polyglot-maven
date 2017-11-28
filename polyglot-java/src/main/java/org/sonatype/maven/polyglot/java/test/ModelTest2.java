package org.sonatype.maven.polyglot.java.test;

import org.sonatype.maven.polyglot.java.dsl.ModelTemplate;

public class ModelTest2 extends ModelTemplate {

	@SuppressWarnings({ "unchecked" })
	public void project() {

		modelVersion = "4.0.0";
		groupId = "org.sample1";
		artifactId = "project1";
		packaging = "jar";
		version = "1.0-SNAPSHOT";
		
		dependencies(
			dependency(groupId -> "junit", artifactId -> "junit", version -> "3.8.1", scope -> "test")
		);		
	}
}
