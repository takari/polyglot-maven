# Overview

[Polyglot for Maven](http://github.com/tesla/tesla-polyglot/) is an experimental distribution of Maven that allows the expression of a POM in something other than XML (oh nooooo!). A couple of the dialects also have the capability to write plugins inline: the Groovy and Ruby dialects allow this.

# Download

You can download the distribution from Maven Central:

[http://repo1.maven.org/maven2/io/tesla/polyglot/tesla-polyglot-cli/0.0.9/tesla-polyglot-cli-0.0.9-bin.tar.gz](http://repo1.maven.org/maven2/io/tesla/polyglot/tesla-polyglot-cli/0.0.9/tesla-polyglot-cli-0.0.9-bin.tar.gz)

# Usage

Polyglot for Maven includes a copy of maven 3.1.1, and can be used like a normal Maven distribution.

There is a translate command that will translate between a `pom.xml` and the other supported dialects. For example:

```
translate pom.xml pom.rb
```

or

```
translate pom.xml pom.groovy
```
or

```
translate pom.xml pom.scala
```

or

```
translate pom.xml pom.yaml
```

or

```
translate pom.xml pom.atom
```

If you want to see what various POMs look like you can take a look here:

[https://github.com/tesla/tesla-polyglot/tree/master/poms](https://github.com/tesla/tesla-polyglot/tree/master/poms)

# Building

### Requirements

* [Maven](http://maven.apache.org) 3.1.1+
* [Java](http://java.sun.com/) 6+

Check-out and build:

    git clone git@github.com:tesla/tesla-polyglot.git
    cd tesla-polyglot
    mvn install

After this completes, you can unzip and play with polyglot for maven:

    tar -xzvf tesla-polyglot-cli/target/tesla-polyglot-*-bin.tar.gz
    ./tesla-polyglot-*/bin/mvn

Now you can look at the usage guide above.

# Things we know that are less than ideal

We know there are short comings, but the distribution is functional and we care more about feedback right now.

- The rules about precedence of which format to be read, and what should happen when there are mixed flavors of POMs have yet to be fully worked out. 
- The whole interoperability story has not been worked out. 
- A pom.xml will currently not be installed or deployed so use this at your own risk. 
- The distribution is the size of a small galaxy. There's no way, currently, in Maven to dynamically download what's required to parse the discovered model. We'll sort this out eventually but there are more imporant things to figure out.

