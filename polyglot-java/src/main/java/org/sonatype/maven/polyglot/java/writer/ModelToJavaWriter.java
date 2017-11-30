package org.sonatype.maven.polyglot.java.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.codehaus.plexus.util.xml.Xpp3Dom;

import org.apache.maven.model.Build;
import org.apache.maven.model.BuildBase;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.Extension;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.Resource;

public class ModelToJavaWriter {
	
	private Writer out;
	private Model model;
	private String br = System.lineSeparator();
		
	public ModelToJavaWriter(Writer out, Model model) {
		super();
		this.out = out;
		this.model = model;
	}

	public void write() {
		try {
			writeFileStar();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeFileStar() throws IOException {
		out.write("public class pom extends org.sonatype.maven.polyglot.java.dsl.ModelFactory {" + br);
		out.write("    public void project() {" + br);
		
		writeHeader();
	}	
	
	public void writeFileEnd() throws IOException {
		out.write("    }" + br);
		out.write("}");
	}
	
	public void writeHeader() throws IOException {
		out.write("        modelVersion = \"" + model.getModelVersion() + "\";" + br);
		out.write("        groupId = \"" + model.getGroupId() + "\";" + br);
		out.write("        artifactId = \"" + model.getArtifactId() + "\";" + br);
		out.write("        packaging = \"" + model.getPackaging() + "\";" + br);
		out.write("        version = \"" + model.getVersion() + "\";" + br);
		
		writeParent();
	}
	
	public void writeParent() throws IOException {
		Parent parent = model.getParent();
		if (parent != null) {
			out.write("        parent(" + br);
			out.write("            groupId -> \"" + parent.getGroupId() + "\"" + br);
			out.write("            artifactId -> \"" + parent.getArtifactId() + "\"" + br);
			if (parent.getVersion() != null) {
				out.write("            version -> \"" + parent.getVersion()+ "\"" + br);
			}
			if (parent.getRelativePath() != null) {
				out.write("            relativePath -> \"" + parent.getRelativePath()+ "\"" + br);
			}
			out.write(");");
		}
		
		writeProperties();
	}
	
	public void writeProperties() throws IOException {
		Properties props = model.getProperties();
		if (props != null && !props.isEmpty()) {
			out.write("        properties(" + br);
			int propertyNumber = 1;			
			for (Object prop : props.keySet()) {
				out.write("            property(\"" + prop.toString() + "\", \"" + props.get(prop) + "\")" + br);
				if (propertyNumber < props.size()) {
					out.write("," + br);
				}
				propertyNumber++;
			}
			out.write("        );" + br);
		}
		
		writeDependencies();
	}
	
	public void writeDependencies() throws IOException {
		if (model.getDependencyManagement() != null && model.getDependencyManagement().getDependencies() != null && !model.getDependencyManagement().getDependencies().isEmpty()) {
			writeDependencies(model.getDependencies(), "dependencyManagement");			
		}		
		writeDependencies(model.getDependencies(), "dependencies");
		writeBuild();
	}
	
	public void writeDependencies(List<Dependency> dependencies, String methodName) throws IOException {
		if (dependencies != null && !dependencies.isEmpty()) {
			out.write("        " + methodName + "(" + br);
			int dependencyNumber = 1;			
			for (Dependency dependency : dependencies) {
				writeDependency(dependency, "        ");
				if (dependencyNumber < dependencies.size()) {
					out.write("," + br);
				}
				dependencyNumber++;
			}
			out.write(br + "        );" + br);
		}			
	}
	
	private void writeDependency(Dependency dependency, String indent) throws IOException {
		out.write(indent + "    dependency(" + br);
		out.write(indent + "        groupId -> \"" + dependency.getGroupId() + "\","  + br);
		out.write(indent + "        artifactId -> \"" + dependency.getArtifactId() + "\"");
		if (dependency.getVersion() != null) {
			out.write("," + br);
			out.write(indent + "        version -> \"" + dependency.getVersion() + "\"");
		}
		if (dependency.getScope() != null) {
			out.write("," + br);
			out.write(indent + "        scope -> \"" + dependency.getScope() + "\"");
		}
		if (dependency.getOptional() != null) {
			out.write("," + br);
			out.write(indent + "        optional -> \"" + dependency.getOptional() + "\"");
		}
		if (dependency.getType() != null) {
			out.write("," + br);
			out.write(indent + "        type -> \"" + dependency.getType() + "\"");
		}
		if (dependency.getSystemPath() != null) {
			out.write("," + br);
			out.write(indent + "        systemPath -> \"" + dependency.getSystemPath() + "\"");
		}
		if (dependency.getExclusions() != null && !dependency.getExclusions().isEmpty()) {
			out.write("," + br);
			out.write(indent + "        exclusions(");
			for (Exclusion exclusion : dependency.getExclusions()) {
				out.write(indent + "        exclusion(groupId -> \"" + exclusion.getGroupId());
				if (exclusion.getArtifactId() != null) {
					out.write(", artifactId -> \"" + exclusion.getArtifactId());
				}
				out.write("\")" + br);
			}
			out.write(br + indent + "    )");
		}
		out.write(br + indent + "    )");
	}
	
	private void writeBuild() throws IOException {
		Build build = model.getBuild();
		if (build != null) {
			out.write("        build()" + br);
						
			if (build.getSourceDirectory() != null) {
				out.write("            .sourceDirectory(\"" + build.getSourceDirectory() + "\")" + br);
			}
			if (build.getScriptSourceDirectory() != null) {
				out.write("            .scriptSourceDirectory(\"" + build.getScriptSourceDirectory() + "\")" + br);
			}
			if (build.getTestSourceDirectory() != null) {
				out.write("            .testSourceDirectory(\"" + build.getTestSourceDirectory() + "\")" + br);
			}
			if (build.getOutputDirectory() != null) {
				out.write("            .outputDirectory(\"" + build.getOutputDirectory() + "\")" + br);
			}
			if (build.getTestOutputDirectory() != null) {
				out.write("            .testOutputDirectory(\"" + build.getTestOutputDirectory() + "\")" + br);
			}
			List<Extension> extensions = build.getExtensions(); 
			if (extensions != null && !extensions.isEmpty()) {
				out.write("            .extensions(" + br);
				int extensionNumber = 1;
				for (Extension extension : extensions) {
					out.write("                extension(" + br);
					out.write("                    groupId -> \"" + extension.getGroupId() + "\","  + br);
					out.write("                    artifactId -> \"" + extension.getArtifactId() + "\"");
					if (extension.getVersion() != null) {
						out.write("," + br);
						out.write("                    version -> \"" + extension.getVersion() + "\"");
					}
					out.write(br + "                )");
					if (extensionNumber < extensions.size()) {
						out.write("," + br);
					}
					extensionNumber++;
				}
				out.write(br + "            )" + br);								
			}
			
			writeBuildBase(build, "            ");
			
			out.write("        .endBuild();" + br);
		}
		
		writeFileEnd();
	}
	
	private void writeBuildBase(BuildBase build, String indent) throws IOException {
		
		if (build.getDefaultGoal() != null) {
			out.write(indent + ".defaultGoal(\"" + build.getDefaultGoal() + "\")" + br);
		}
		if (build.getDirectory() != null) {
			out.write(indent + ".directory(\"" + build.getDirectory() + "\")" + br);
		}
		if (build.getFinalName() != null) {
			out.write(indent + ".finalName(\"" + build.getFinalName() + "\")" + br);
		}
		if (build.getFilters() != null && !build.getFilters().isEmpty()) {
			out.write(indent + ".filters(\"" + build.getFilters().stream().collect(Collectors.joining("\",\"")) + "\")" + br);
		}
		
		if (build.getPluginManagement() != null) {
			writePlugins(build.getPluginManagement().getPlugins(), indent, "pluginManagement");
		}
		
		writePlugins(build.getPlugins(), indent, "plugins");
		
		writeResources(build.getResources(), indent, "resources");
		writeResources(build.getTestResources(), indent, "testResources");
	}
	
	private void writeResources(List<Resource> resources, String indent, String methodName) throws IOException {
		if (resources != null && !resources.isEmpty()) {
			out.write(indent + "." + methodName + "("+ br);
			
			int pluginOrderNum = 1;
			for (Resource resource : resources) {
				writeResource(resource, indent + "    ");
				
				if (pluginOrderNum < resources.size()) {
					out.write(indent + "    ," + br);
				}
				pluginOrderNum++;
			}	
			
			out.write(indent + ")"+ br);
		}
	}
	
	private void writeResource(Resource resource, String indent) throws IOException {
		out.write(indent + "resource()" + br);
		out.write(indent + "    ");
		if (resource.getDirectory() != null) {			
			out.write(".directory(\"" + resource.getDirectory() + "\")");	
		}		
		if (resource.getFiltering() != null) {			
			out.write(".filtering(" + resource.getFiltering() + ")");	
		}
		if (resource.getTargetPath() != null) {			
			out.write(".targetPath(\"" + resource.getTargetPath() + "\")");	
		}
		if (resource.getIncludes() != null && !resource.getIncludes().isEmpty()) {			
			out.write(".includes(\"" + resource.getIncludes().stream().collect(Collectors.joining("\",\"")) + "\")");	
		}
		if (resource.getExcludes() != null && !resource.getExcludes().isEmpty()) {			
			out.write(".excludes(\"" + resource.getExcludes().stream().collect(Collectors.joining("\",\"")) + "\")");	
		}
		out.write(br + indent + ".endResource()" + br);
	}

	private void writePlugins(List<Plugin> plugins, String indent, String methodName) throws IOException {
		if (plugins != null && !plugins.isEmpty()) {
			out.write(indent + "." + methodName + "("+ br);
			int pluginOrderNum = 1;
			for (Plugin plugin : plugins) {
				writePlugin(plugin, indent + "    ");
				
				if (pluginOrderNum < plugins.size()) {
					out.write(indent + "    ," + br);
				}
				pluginOrderNum++;
			}			
			out.write(indent + ")"+ br);			
		}
	}
	
	private void writePlugin(Plugin plugin, String indent) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append(plugin.getGroupId() + ":" + plugin.getArtifactId());
		if (plugin.getVersion() != null) {
			sb.append(":" + plugin.getVersion());
		}
		out.write(indent + "plugin(\"" + sb.toString() + "\")" + br);
		if (plugin.getExtensions() != null) {
			out.write(indent + "    .extensions(" + plugin.getExtensions() + ")" + br);
		}
		if (plugin.getInherited() != null) {
			out.write(indent + "    .inherited(" + plugin.getInherited() + ")" + br);
		}
		if (plugin.getDependencies() != null && !plugin.getDependencies().isEmpty()) {
			out.write(indent + "    " + ".dependencies(" + br);
			writePluginDependency(plugin.getDependencies(), indent);
			out.write(indent + "    " + ")" + br);
		}
		writeConfiguration((Xpp3Dom)plugin.getConfiguration(), indent);
		
		out.write(indent + ".endPlugin()" + br);
	}
	
	private void writeConfiguration(Xpp3Dom configuration, String indent) throws IOException {
		if (configuration != null) {
			out.write(indent + "    " + ".configuration(" + br);
			out.write(indent + "        " + "startXML()" + br);
			
			StringBuilder sb = new StringBuilder();			
			generateConfig(configuration, sb, indent + "        ");
			
			out.write(sb.toString());			
			
			out.write(indent + "        " + ".endXML()" + br);
			out.write(indent + "    " + ")" + br);
		}
	}
	
	private void generateConfig(Xpp3Dom node, StringBuilder sb, String indent) {
		String tagName = node.getName();
		String tagValue = node.getValue();
		if (node.getParent() == null) {
			sb.append(indent + ".tag(\"" + tagName + "\", " + tagName + " -> {" + br);			
		}		
		
		if (tagValue != null) {
			sb.append(indent + "    " + calculateXMLIndent(node) + tagName + ".content(\"" + tagValue + "\");" + br);					
		}
		String[] attributeNames = node.getAttributeNames();
		for (int i = 0; i < attributeNames.length; i++) {
			String attributeName = attributeNames[i];
			sb.append(indent + calculateXMLIndent(node) + tagName + ".attribute(\"" + attributeName + "\", \"" + node.getAttribute(attributeName) + "\");" + br);
		}
		Xpp3Dom[] children = node.getChildren();
		for (int i = 0; i < children.length; i++) {
			Xpp3Dom child = children[i];
			sb.append(indent + calculateXMLIndent(child) +  child.getParent().getName() + ".child(\"" + child.getName() + "\"," + child.getName() + "-> {" + br);			
			
			generateConfig(child, sb, indent);
			
			sb.append(indent + calculateXMLIndent(child) + "});" + br);			
		}
		
		if (node.getParent() == null) {
			sb.append(indent + calculateXMLIndent(node) + "})" + br);			
		}
	}
	
	private int calculateXMlHierarchyLevel(Xpp3Dom node, int level) {		 
		if (node.getParent() == null) {
			return level;
		} else {
			return calculateXMlHierarchyLevel(node.getParent(), level + 1);
		}
	}
	
	private String calculateXMLIndent(Xpp3Dom node) {
		String indent = "";
		for (int i = 0; i < calculateXMlHierarchyLevel(node, 0); i++) {			
			indent = indent + "    ";
		}
		return indent;
	}
	
	private void writePluginDependency(List<Dependency> dependencies, String indent) throws IOException {
		int dependencyNumber = 1;
		for (Dependency dependency : dependencies) {
			writeDependency(dependency, indent + "        ");
			out.write(br);
			if (dependencyNumber < dependencies.size()) {
				out.write(indent + "           ," + br);
			}
			dependencyNumber++;
		}
	}
}
