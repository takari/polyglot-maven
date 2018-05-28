# Overview

Experiment to see how Maven Java DSL could look like.  Main goal is to explore
possible syntax options and get feedback on pom.java syntax.

# Example
Simple example
```java
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
```

# Configuration

To use Polyglot for Maven you need to edit 
`${maven.multiModuleProjectDirectory}/.mvn/extensions.xml` 
and add the appropriate language extension.

There is sample project in `polyglot-maven-examples` project.

## Update extensions.xml

Edit the `extensions.xml` file and add the following, replacing VERSION with the
latest stable version of current artifact.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<extensions>
  <extension>
    <groupId>io.takari.polyglot</groupId>
    <artifactId>polyglot-java</artifactId>
    <version>VERSION</version>
  </extension>
</extensions>
```

## Convert existing POM

Use simple Maven Plugin that will help you convert any existing `pom.xml` files:

```
mvn io.takari.polyglot:polyglot-translate-plugin:translate -Dinput=pom.xml -Doutput=pom.java
```

# Model Interpolation

Model Interpolation consists in replacing ${...} with calculated value. This
mechanism works for Java DSL as well. For example, if you can change output
directory for compiled classes by adding

```java
build(outputDirectory -> "${project.build.directory}/classes_1");
```

to you Java based POM definition.  For more information about Model
Interpolation please refer to
http://maven.apache.org/ref/3.5.3/maven-model-builder/#Model_Interpolation

Other ways to get value of ${basedir} property are standard Java ways:

```java
String baseDir = System.getProperty("user.dir")
```
or
```java
String baseDir = java.nio.file.Paths.get("").toAbsolutePath().toString();
```

# Limitations

Currently not supported:
* 	Convenience methods for private org.apache.maven.model.Organization organization;
*	Convenience methods for private java.util.List licenses;
*	Convenience methods for private java.util.List developers;
*	Convenience methods for private java.util.List contributors;
*	Convenience methods for private java.util.List mailingLists;
*	Convenience methods for private org.apache.maven.model.Prerequisites prerequisites;
*	Convenience methods for private org.apache.maven.model.Scm scm;
*	Convenience methods for private org.apache.maven.model.IssueManagement issueManagement;
*	Convenience methods for private org.apache.maven.model.CiManagement ciManagement;
*	Convenience methods for private org.apache.maven.model.DistributionManagement distributionManagement;
*	Convenience methods for private java.lang.Object reports;
*	Convenience methods for private org.apache.maven.model.Reporting reporting;

To deal with these temporary limitations you have access to `model` variable
from parent class. And you can use it's methods to add configuration for any
entities. For example:

```java
Contributor contributor = new Contributor();
contributor.setName("John Smith");
model.addContributor(contributor);
```

