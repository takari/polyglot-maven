package org.sonatype.maven.polyglot.java.dsl;

import static java.util.Arrays.asList;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.sonatype.maven.polyglot.java.namedval.NamedValue;
import org.sonatype.maven.polyglot.java.namedval.NamedValueProcessor;

public class ModelTemplate implements DependencyTemplate, PropertyTemplate, BuildTemplate {

	private Model model = new Model();

	protected String modelVersion;
	protected String groupId;
	protected String artifactId;
	protected String version;
	protected String packaging;
	protected String name;
	protected String description;
	protected String url;
	protected String inceptionYear;
	protected String modelEncoding;
	// ------------
	// Field descriptor #98 Lorg/apache/maven/model/Organization;
	private org.apache.maven.model.Organization organization;

	// Field descriptor #100 Ljava/util/List;
	// Signature: Ljava/util/List<Lorg/apache/maven/model/License;>;
	private java.util.List licenses;

	// Field descriptor #100 Ljava/util/List;
	// Signature: Ljava/util/List<Lorg/apache/maven/model/Developer;>;
	private java.util.List developers;

	// Field descriptor #100 Ljava/util/List;
	// Signature: Ljava/util/List<Lorg/apache/maven/model/Contributor;>;
	private java.util.List contributors;

	// Field descriptor #100 Ljava/util/List;
	// Signature: Ljava/util/List<Lorg/apache/maven/model/MailingList;>;
	private java.util.List mailingLists;

	// Field descriptor #110 Lorg/apache/maven/model/Prerequisites;
	private org.apache.maven.model.Prerequisites prerequisites;

	// Field descriptor #112 Lorg/apache/maven/model/Scm;
	private org.apache.maven.model.Scm scm;

	// Field descriptor #114 Lorg/apache/maven/model/IssueManagement;
	private org.apache.maven.model.IssueManagement issueManagement;

	// Field descriptor #116 Lorg/apache/maven/model/CiManagement;
	private org.apache.maven.model.CiManagement ciManagement;

	// Field descriptor #118 Lorg/apache/maven/model/Build;
	private org.apache.maven.model.Build build;

	// Field descriptor #100 Ljava/util/List;
	// Signature: Ljava/util/List<Lorg/apache/maven/model/Profile;>;
	private java.util.List profiles;

	private java.util.List modules;

	// Field descriptor #68 Lorg/apache/maven/model/DistributionManagement;
	private org.apache.maven.model.DistributionManagement distributionManagement;

	// Field descriptor #72 Lorg/apache/maven/model/DependencyManagement;
	private org.apache.maven.model.DependencyManagement dependencyManagement;

	// Field descriptor #64 Ljava/util/List;
	// Signature: Ljava/util/List<Lorg/apache/maven/model/Repository;>;
	private java.util.List repositories;

	// Field descriptor #64 Ljava/util/List;
	// Signature: Ljava/util/List<Lorg/apache/maven/model/Repository;>;
	private java.util.List pluginRepositories;

	// Field descriptor #79 Ljava/lang/Object;
	private java.lang.Object reports;

	// Field descriptor #81 Lorg/apache/maven/model/Reporting;
	private org.apache.maven.model.Reporting reporting;

	// Field descriptor #83 Ljava/util/Map;
	// Signature:
	// Ljava/util/Map<Ljava/lang/Object;Lorg/apache/maven/model/InputLocation;>;
	private java.util.Map locations;

	// ------------

	public void parent(NamedValue<String>... keyValuePairs) {
		Parent parent = new Parent();
		parent = NamedValueProcessor.namedToObject(parent, keyValuePairs);
		System.out.println("Parent->" + parent);
		model.setParent(parent);
	}

	public void dependencies(Dependency... dependencies) {
		asList(dependencies).forEach(dep -> {
			System.out.println("Dependency->" + dep);

			for (Exclusion excl : dep.getExclusions()) {
				System.out.println("excl gr=" + excl.getGroupId() + ":art=" + excl.getArtifactId());
			}
			
			model.addDependency(dep);

		});
	}
	
	public BuildBuilder build() {
		return new BuildBuilder(model);
	}

	
//	build(
//	artifactId -> "artf_id",
//	version -> "v1",
//	plugins(
//			plugin(
//					artifactId -> "org.apache.maven.plugins",
//					groupId -> "maven-jar-plugin",
//					version -> "2.6",
//					configuration(
//							xml().startConfig()
//								.tag("classifier", tag -> tag.content("pre-process"))						
//							.endConfig()
//					),
//					executions(
//						execution(
//							id -> "pre-process-classes",
//							phase -> "pre-process",
//							configuration(
//									xml().startConfig()
//										.tag("classifier", tag -> tag.content("pre-process"))						
//									.endConfig()
//							)
//						)
//					),
//					pluginDependencies(
//						dependency(
//							artifactId -> "org.apache.maven.plugins",
//							groupId -> "maven-jar-plugin",
//							version -> "2.6"
//						)
//					)
//			)			
//	),
//	resources(
//			resource(
//				targetPath -> "d:/",
//				filtering -> "true"
//			)
//	),
//	testResources(null)
//);
//	public void build(BuildNamedValue... namedValues) {
//		Build build = new Build();
//		
//		Map<String, String> map = new HashMap<>();
//		asList(namedValues).stream().filter(kvp -> kvp != null).filter(kvp -> !(kvp instanceof BuildComplexTypeNamedValue))
//			.forEach(kvp -> map.put(kvp.name(), kvp.value()));
//		NamedValueProcessor.mapToObject(build, map);
//		
//		for (BuildNamedValue namedvalue : asList(namedValues)) {
//			if (namedvalue instanceof BuildPluginsNamedValue) {
//				build.setPlugins(((BuildPluginsNamedValue)namedvalue).getPlugins());				
//			}
//			
//			if (namedvalue instanceof PluginManagementNamedValue) {
//				build.setPluginManagement(((PluginManagementNamedValue)namedvalue).getPluginManagement());				
//			}
//			
//			if (namedvalue instanceof BuildExtensionNamedValue) {
//				build.setExtensions(((BuildExtensionNamedValue)namedvalue).getExtensions());				
//			}
//			
//			if (namedvalue instanceof BuildFiltersNamedValue) {
//				build.setFilters(((BuildFiltersNamedValue)namedvalue).getFilters());				
//			}
//			
//			if (namedvalue instanceof BuildResourcesNamedValue) {
//				build.setResources(((BuildResourcesNamedValue)namedvalue).getResources());				
//			}
//			
//			if (namedvalue instanceof BuildTestResourcesNamedValue) {
//				build.setTestResources(((BuildTestResourcesNamedValue)namedvalue).getResources());				
//			}					
//		}			
//		
//		this.build = build;
//	}
	
	public void properties(PropertyTemplate.Property... properties) {
		asList(properties).forEach(prop -> {
			model.addProperty(prop.getName(), prop.getValue());
			System.out.println("Added property " + prop.getName() + ":" + prop.getValue());
		});
	}
	
	public ProfileBuilder profile(String id) {
		ProfileBuilder builder = new ProfileBuilder(model, id);		
		return builder;
	}		
	
	public void project() {}

	public Model getModel() {
		
		project();
		
		if (modelVersion != null) {
			model.setModelVersion(modelVersion);
		}
		if (groupId != null) {
			model.setGroupId(groupId);
		}
		if (artifactId != null) {
			model.setArtifactId(artifactId);
		}
		if (version != null) {
			model.setVersion(version);
		}
		if (packaging != null) {
			model.setPackaging(packaging);
		}
		if (name != null) {
			model.setName(name);
		}
		if (description != null) {
			model.setDescription(description);
		}
		if (url != null) {
			model.setUrl(url);
		}
		if (inceptionYear != null) {
			model.setInceptionYear(inceptionYear);
		}
		if (modelEncoding != null) {
			model.setModelEncoding(modelEncoding);
		}
		return model;
	}
}
