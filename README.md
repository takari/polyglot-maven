Description
-----------

Sonatype [Polyglot for Maven](http://polyglot.sonatype.org/).

License
-------

[Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

Support
-------

To submit an issue, please use the [Sonatype Issue Tracker](https://issues.sonatype.org/browse/PMAVEN).

Building
--------

### Requirements

* [Maven](http://maven.apache.org) 2+
* [Java](http://java.sun.com/) 5+

Check-out and build:

    git clone git@github.com:tesla/tesla-polyglot.git
    cd tesla-polyglot
    mvn install

After this completes, you can unzip and play with polyglot for maven:

    tar -xzvf tesla-polyglot-cli/target/tesla-polyglot-*-bin.tar.gz
    ./tesla-polyglot-*/bin/mvn

Polyglot for Maven includes a copy of maven 3.1.0, which isn't 100% backwards compatible
with maven 2.0. Specifically, some maven plugins might not work. Virtually all do however, so
it is feasible to move the unzipped directory and put that pmaven-*/bin on your path either
inplace of or before your existing mvn binary.

There is a translate command that will translate between different language's versions of pom.*
(and in the case of Clojure, read (not write) Leiningen's project.clj). It works by looking at
file extensions, so just give it a source and a destination file as arguments and it will translate
between them.

You don't normally have to use translate - the mvn command can read any format the translate command can,
either by reading any pom.* it can find or using a command line option e.g. mvn -f project.clj.
