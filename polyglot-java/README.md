# Overview
Experiment to see how Maven Java DSL could look like.
Main goal is to explore possible syntax options and get feedback on pom.java syntax.

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

To deal with these temporary limitations you have an access to `model` variable from parent class. And you can use it's methods to add configuration for any entities. For example: 

```java
Contributor contributor = new Contributor();
contributor.setName("John Smith");
model.addContributor(contributor);
```

# Configuration

To use Polyglot for Maven you need to edit 
`${maven.multiModuleProjectDirectory}/.mvn/extensions.xml` 
and add the appropriate language extension.

There is sample project in `samples` folder.

## Update extensions.xml

Edit the `extensions.xml` file and add the following, replacing VERSION with
the latest stable version of current artifact.

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

Use simple Maven Plugin that will help you convert any existing 
`pom.xml` files:

```
mvn io.takari.polyglot:polyglot-translate-plugin:translate -Dinput=pom.xml -Doutput=pom.java
```
