This is a fork of [Polyglot for Maven](http://github.com/takari/polyglot-maven/) that aims to strip down the complexity to a minimum and provide a fully operational YAML support for POM files

TODO: review the rest -

---

# Usage

To use Polyglot for Maven you need to edit
`${maven.multiModuleProjectDirectory}/.mvn/extensions.xml` and add the
appropriate language extension.

The scala dialect supports a separate configuration parameter
`polyglot.scala.outputdir` to specify a different output directory. This avoids the
deletion during a Maven clean phase run, when set to a different folder such as
`.polyglot-cache` instead of the default `target`. Inspect `polyglot-maven-examples/scala/.mvn/maven.config` for an example setup.

## Available Languages

The available languages, in alphabetical order, with their artifact id are:

| Language | Artifact Id        |
|:--------:|:------------------:|
| Atom     | `polyglot-atom`    |
| Groovy   | `polyglot-groovy`  |
| Clojure  | `polyglot-clojure` |
| Kotlin   | `polyglot-kotlin`  |
| Ruby     | `polyglot-ruby`    |
| Scala    | `polyglot-scala`   |
| YAML     | `polyglot-yaml`    |
| Java     | `polyglot-java`    |
| XML      | `polyglot-xml`     |

The groupId value is `io.takari.polyglot`.

## Update extensions.xml

Edit the `extensions.xml` file and add the following, replacing ARTIFACTID with
the artifactId for your chosen language.

```
<?xml version="1.0" encoding="UTF-8"?>
<extensions>
  <extension>
    <groupId>io.takari.polyglot</groupId>
    <artifactId>ARTIFACTID</artifactId>
    <version>0.4.6</version>
  </extension>
</extensions>
```

## Convert existing POM

We have created a simple Maven plugin that will help you convert any existing
`pom.xml` files:

```
mvn io.takari.polyglot:polyglot-translate-plugin:translate \
  -Dinput=pom.xml -Doutput=pom.{format}
```

Where the supported formats are `rb`, `groovy`, `scala`, `yaml`, `atom`, `java`
and of course `xml`.  See
[here](http://takari.io/2015/03/21/polyglot-maven.html) for more info.  You can
even convert back to `xml` or cross-convert between all supported formats.

# Known Limitations and Issues

## Interoperability

The whole interoperability story has not been worked out but you can create a
XML-formatted POM from the Polyglot version. Currently mixing different dialects
within a reactor is not supported.

A `pom.xml` will currently not be installed or deployed except for the Ruby DSL
and the Scala DSL but we are working towards this feature for all DSLs.

## Tooling

Some support in IDE's like IntelliJ IDEA and Eclipse exist and the different
markup languages are understood by various syntax highlighters. However, full
integration of the markup syntax and the specific Maven-related aspects is not
available.

## Limited Plugin Support

Maven plugins or Maven plugin goals that rely on the XML format are not
supported, since they are either attempting to parse the XML directly or modify
it in automated fashion do not work with Polyglot Maven. Examples are:

- Maven Release Plugin
- Maven Versions Plugin (goals that don't edit the `pom.xml` (like e.g. `display-dependency-updates`) work as expected though)

Fixes would have to be implemented in these plugins. Workarounds or replacement
workflows for most usecases exist.

# Polyglot Maven in Real Life

Despite the warning above, Polyglot Maven is pretty stable right now. Have a
look at the integration tests for each dialect in this repository for some
examples as well as our dedicated
[polyglot-maven-examples project](https://github.com/takari/polyglot-maven-examples).

Some dialect folder contain specific README files with futher instructions as
as test code with example projects and more. Ensure to check the ones for your
specific dialect out as well.

The following projects are real world usage examples that use Polyglot Maven in
their regular development and release work:

## Kotlin

Specific docs and more can be found in the [Kotlin dialect specific readme](./polyglot-kotlin/README.md).

## Ruby

* https://github.com/jruby - Extensive usage of Polyglot Ruby and contributions
  to the project from the team, and is recommended when creating
  [java extensions for jruby](https://github.com/jruby/jruby-examples/tree/master/extensions/basic).
  
* The
  [ruby-processing project](http://ruby-processing.github.io/building/building/)
  has examples of creating java extensions for jruby (see
  [JRubyArt](https://github.com/ruby-processing/JRubyArt) and
  [propane](https://github.com/ruby-processing/propane) builds) as well as
  simpler projects where the polyglot maven is used in creating gem wrappers for
  processing.org java libraries (eg
  [toxicgem](https://github.com/ruby-processing/toxicgem) and
  [geomerative](https://github.com/ruby-processing/propane) gems).

## YAML

* http://snakeyaml.org - Extensive usage of Polyglot YAML and contributions to
  the project from the team.

* https://urbanise.com - Using Polyglot YAML for building next generation strata
  management platform.

## Scala

_Java Projects_

* [LambdaTest](https://github.com/lefou/LambdaTest) - A simple Java project with
  a standalone
  [pom.scala](https://github.com/lefou/LambdaTest/blob/master/pom.scala)

* [CmdOption](https://github.com/ToToTec/CmdOption) - A Java project with a
  top-level reactor project and an additional shared scala file included into
  both `pom.scala`s

_Scala Projects_

* [Domino](https://github.com/domino-osgi/domino) - A simple project using
  Polyglot Scala.

* [Blended](https://github.com/woq-blended/blended) - A complex multi-project
  using Polyglot Scala. It's also an example where the `#include` feature is
  heavily used to share common configuration but avoid Maven parent poms, which
  are often problematic.

Please let us know of your usage by filing an issue so we can add it here.
