# Overview

Main goal of this extension is to provide shorter XML dialect for POM compared to standard pom.xml format. 
This is achieved by mean of using xml attributes for:
* definition of dependencies
* definition of plugins

Please note that not all properties of plugins are defined as attributes. Please refer to  */polyglot-xml/src/test/resources/pom/pom_maven_v4_1.xml* for example and use XSD file from */polyglot-xml/src/main/resources/xsd/4.1.0* folder.

Files processed by `polygloy-xml` can have `.xml41` or `.xml` file extensions. When working with `.xml` files `polyglot-xml` runs before standard xml systax to check whether file contains default syntax or not. For default syntax execution is delegated to default Maven parser. But when `polygloy-xml` detects that it can process file it does this instead of default Maven xml parser.

Short example of supported format:
```xml
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>my.example</groupId>
  <artifactId>test-webapp</artifactId>
  <packaging>war</packaging>
  <version>1.0-SNAPSHOT</version>
  <name>test-webapp</name>

  <dependencies>
    <dependency groupId="junit" artifactId="junit" version="3.8.1" scope="test"/>
  </dependencies>

  <build>
    <finalName>test-webapp</finalName>
  </build>
</project>
```

# Building

### Requirements

* [Maven](http://maven.apache.org) 3.3.1+
* [Java](www.oracle.com/technetwork/java/javase/downloads/) 7+

# Configuration

To use Polyglot for Maven you need to edit 
`${maven.multiModuleProjectDirectory}/.mvn/extensions.xml` 
and add the appropriate language extension.

## Update extensions.xml

Edit the `extensions.xml` file and add the following, replacing VERSION with
the latest stable version of current artifact.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<extensions>
  <extension>
    <groupId>io.takari.polyglot</groupId>
    <artifactId>polyglot-xml</artifactId>
    <version>VERSION</version>
  </extension>
</extensions>
```

## Convert existing POM

Use simple Maven Plugin that will help you convert any existing 
`pom.xml` files:
```
mvn io.takari.polyglot:polyglot-translate-plugin:translate \
  -Dinput=pom.xml -Doutput=pom.xml41
```
Please note that in this case `pom.xml` uses default Maven xml syntax and `pom.xml41` will use syntax defined in `polyglot-xml`.