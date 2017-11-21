package org.sonatype.maven.polyglot.java.test;

import org.sonatype.maven.polyglot.java.dsl.ModelTemplate;

public class ModelTest extends ModelTemplate {
	
	
	@SuppressWarnings({ "unchecked"})
	public void project() {
		
		modelVersion = "4.0";
		groupId = "my-grp";
		artifactId = "my-art";
		version = "1.0";
	
		
		parent( 
				artifactId -> "artf_id",
				version -> "v1",
				relativePath -> "../..",
				groupId -> "grp-id-1"
		);
		
		dependencies(
				dependency(
						groupId -> "dep1grp",
						artifactId -> "art2",
						exclusions(
							exclusion(groupId -> "grpToExclude"),
							exclusion(groupId -> "grp2ToExclude")
						),
						version -> "v2"
				),
				dependency(groupId -> "gr2", artifactId -> "art3")
//				test(groupId -> "org.junit", artifactId -> "junit") // already specifies scope
		);		
		
		build()
			.resources(
				resource -> {resource.directory="c://foodir"; resource.filtering=true; resource.targetPath="c://bardir"; resource.includes="*.a"; resource.excludes="*.b";},
				resource -> {resource.directory="src/main/resources"; resource.filtering=true; resource.targetPath="target ";}
			)
			.resources(
					resource(
						directory -> "c://foodir",
						filtering -> "true",
						targetPath -> "c://bardir",
						includes("*.a"),
						excludes("*.b")
					),
					resource()
						.directory("c://foodir").filtering(true).targetPath("c://bardir").includes("*.a").excludes("*.b")
					.buildResource(),
					resource(r -> {r.directory="c://foodir"; r.filtering=true; r.targetPath="c://bardir"; r.includes="*.a"; r.excludes="*.b";}) 
			)
			.pluginManagement(
					plugin("org.apache.rat:apache-rat-plugin")
						.configuration(
							xml()
							.startConfig()
								.tag("excludes", excludes -> {
									excludes.child("exclude", exclude -> exclude.content("src/test/resources*/**"));
									excludes.child("exclude", exclude -> exclude.content("src/test/projects/**"));
									excludes.child("exclude", exclude -> exclude.content("src/test/remote-repo/**"));
									excludes.child("exclude", exclude -> exclude.content("**/*.odg"));
								})
							.endConfig()
						),
					plugin(groupId -> "org.apache.maven.plugins", artifactId -> "maven-checkstyle-plugin", version -> "2.14")
			)
			.plugins(
					plugin("org.codehaus.mojo", "animal-sniffer-maven-plugin", "1.14")
					.configuration(
						xml()
						.startConfig()
							.tag("signature", signature -> {
								signature.child("groupId", groupId -> groupId.content("org.codehaus.mojo.signature"));
								signature.child("artifactId", artifactId -> artifactId.content("java17"));
								signature.child("version", version -> version.content("1.0"));
							})
						.endConfig()
					)
					.executions(
						execution(
							id -> "check-java-1.6-compat",
							phase -> "process-classes",
							goals("check")
						)
	//					,execution("check-java-1.6-compat").phase("process-classes").goals("check")
					)
			);
		
		
		profile("jboss")		
				.activeByDefault(true)
		
				.dependencies(
					dependency(groupId -> "gr1", artifactId -> "art1"),
					dependency(groupId -> "gr2", artifactId -> "art2")
				)
				
				.modules("a", "ss")
				
				.properties(
					property(name1 -> "prop1"),
					property(name2 -> "prop2")
				)
				
				.build(
					profileBuild()
					.plugins(
							plugin("")
							.configuration(
								xml()
								.startConfig()
									.tag("classifier", tag -> tag.content("pre-process"))
								.endConfig()
							)
					)
					.resources(
						resource("directory", "targetPath", true, new String[]{"*"}, null)
					)
				);	
		
		
		properties(
			property(name1 -> "property_1"),
			property(name2 -> "property_2")
		);
	}
	
}
