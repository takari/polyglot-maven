Description
-----------

[Polyglot for Tesla](http://github.com/tesla-polyglot/) is an experimental distribution of Maven that allows the expression of a POM in something other than XML (oh nooooo!). A couple of the dialects also have the capability to write plugins inline: the Groovy and Ruby dialects allow this.

License
-------

[EPL 1.0](http://www.eclipse.org/legal/epl-v10.html)

Support
-------

To submit an issue, please use [Github Issues](https://github.com/tesla/tesla-polyglot/issues).

Building
--------

### Requirements

* [Maven](http://maven.apache.org) 3.1.0+
* [Java](http://java.sun.com/) 6+

Check-out and build:

    git clone git@github.com:tesla/tesla-polyglot.git
    cd tesla-polyglot
    mvn install

After this completes, you can unzip and play with polyglot for maven:

    tar -xzvf tesla-polyglot-cli/target/tesla-polyglot-*-bin.tar.gz
    ./tesla-polyglot-*/bin/mvn

Polyglot for Maven includes a copy of maven 3.1.0, which isn't 100% backwards compatible
with maven 2.0. Specifically, some maven plugins might not work. The Tesla Polyglot distribution is just
like a normal Maven distribution and can be used like one.

There is a translate command that will translate between a `pom.xml` and the other supported dialects. For example:

```
translate pom.xml pom.rb
```

or

```
translate pom.xml pom.groovy
```

The rules about precedence of which format to be read, and what should happen when there are mixed flavors of POMs have yet to be fully worked out. Also note that the whole interoperability story has not been worked out. A pom.xml will currently not be installed or deployed so use this at your own risk. It's fully functional but interoperability is not a priority right now. Getting out a minimal viable product (MVP) is in order to get feedback.