# Changelog

The [git commit history](https://github.com/takari/polyglot-maven/commits/master)
is the detailed source of all changes. The following provides most information
at an easier glance.

## Version 0.4.5 - 2020-03-03

- Remove upper bound for JDK version to allow Java 11 and newer
 - contributed by Manfred Moser - http://www.simpligility.com
- polyglot-kotlin - revert automatic source folder setting to koltin
  - fixes https://github.com/takari/polyglot-maven/issues/205
  - contributed by Manfred Moser - http://www.simpligility.com
- Update xstream version in test resources to avoid security alerts
  - contributed by Manfred Moser - http://www.simpligility.com
- Avoid assumption about replacement pom file being readable
  - see https://github.com/takari/polyglot-maven/pull/212
  - fixes https://github.com/takari/polyglot-maven/issues/210
  - contributed by Christoph Läubrich https://github.com/laeubi
- Fix sorting in model manager
  - see https://github.com/takari/polyglot-maven/pull/211
  - fixes https://github.com/takari/polyglot-maven/issues/209
  - contributed by Christoph Läubrich https://github.com/laeubi
- Upgrade scala-maven-plugin, clojure-maven-plugin and Clojure
  - contributed by Manfred Moser - http://www.simpligility.com

## Version 0.4.4 - 2019-11-24

- polyglot-kotlin
  - Set source folders to kotlin
    - see https://github.com/takari/polyglot-maven/pull/202
    - fixes https://github.com/takari/polyglot-maven/issues/201
    - contributed by Nils Olson https://github.com/nilols
  - Upgrade to kotlin 1.3.60
    - contributed by Manfred Moser - http://www.simpligility.com
- General
  - Provide a mechanism to override properties of a polyglot build
    - see https://github.com/takari/polyglot-maven/pull/197
    - fixes https://github.com/takari/polyglot-maven/issues/193
    - contributed by Christoph Läubrich https://github.com/laeubi
  - TeslaModelProcessor.locatePom(File) ignores files ending in.xml
    - see https://github.com/takari/polyglot-maven/pull/196
    - fixes https://github.com/takari/polyglot-maven/issues/192
    - contributed by Christoph Läubrich https://github.com/laeubi
  - Use platform encoding in ModelReaderSupport
    - see https://github.com/takari/polyglot-maven/pull/195
    - fixes https://github.com/takari/polyglot-maven/issues/192
    - contributed by Christoph Läubrich https://github.com/laeubi
  - Invoker plugin update
    - contributed by Manfred Moser - http://www.simpligility.com
  - takari parent update
    - contributed by Manfred Moser - http://www.simpligility.com
  - plexus-component-metadata update to 2.1.0
    - contributed by Manfred Moser - http://www.simpligility.com
  - maven-enforcer-plugin update to 3.0.0-M3
    - contributed by Manfred Moser - http://www.simpligility.com
- Release performed by Manfred Moser - http://www.simpligility.com  

## Version 0.4.3 - 2019-08-14

- polyglot-kotlin
  - Avoid IllegalStateException
  - see https://github.com/takari/polyglot-maven/pull/190
  - contributed by Robert Thornton https://github.com/thorntonrp
- Release performed by Manfred Moser - http://www.simpligility.com

## Version 0.4.2 - 2019-08-01

- polyglot-kotlin
  - improved support for IntelliJ Idea usage
  - see https://github.com/takari/polyglot-maven/pull/187
  - blog post https://craftsmen.nl/polyglot-maven-kotlin-instead-of-xml/
- Release performed by Manfred Moser - http://www.simpligility.com

## Version 0.4.1 - 2019-06-04

- polyglot-kotlin
  - kotlin update
  - numerous improvements to more idiomatic kotlin
  - see https://github.com/takari/polyglot-maven/pull/184
- Release performed by Manfred Moser - http://www.simpligility.com

## Version 0.4.0 - 2019-03-09

- polyglot-common:
  - Execute tasks are now installed with inheritable set to false
  - The `ExecuteContext` interface now has default implementations
  - The `ExecuteContext` now includes `getMavenSession()`
  - the `ExecuteContext` now includes `getLog()` to comport with Java bean conventions. The `log()` operation has been
    deprecated.
  - the `ExecuteContext` now includes `getBasedir()` to comport with Java bean conventions. The `basedir()` operation
    has been deprecated.
- polyglot-kotlin:
  - Updates Kotlin to 1.3.21
  - Includes support for Maven's ClassRealm
  - Includes full support for the entire Maven model
  - Includes support for execute tasks via as inline lambdas or as external scripts.
  - Adds integration tests
  - Provides 100% test coverage for the entire Maven model
  - Resolves ClassLoader issues that affected integration with IntelliJ IDEA
  - The above enhancements were contributed by Robert Thornton https://github.com/thorntonrp
- Release performed by Manfred Moser - http://www.simpligility.com

## Version 0.3.2 - 2018-10-19

- polyglot-java: fixed depMgt conversion
- polyglot-ruby: java9+ support improvement
  - see https://github.com/takari/polyglot-maven/commit/60290957cfc693fa8ad44711702b91c95f60f692
  - fixes https://github.com/takari/polyglot-maven/issues/177
  - contributed by Christian Meier https://github.com/mkristian
- added polyglot-kotlin
  - see https://github.com/takari/polyglot-maven/pull/170
  - contributed by Viacheslav Tradunskyi https://github.com/Tradunsky
- Release performed by Manfred Moser - http://www.simpligility.com

## Version 0.3.1 - 2018-06-27

- polyglot-scala: Convenience methods for `Dependency` (`classifier`, `intransitive`, `%` (scope))
  - see https://github.com/takari/polyglot-maven/pull/156
  - contributed by Tobias Roeser https://github.com/lefou
- polyglot-scala: Support `reporting`-section in pom
  - see https://github.com/takari/polyglot-maven/pull/157
  - contributed by Tobias Roeser https://github.com/lefou
- polyglot-scala: Added default value for pom property modelversion (4.0.0)
  - see https://github.com/takari/polyglot-maven/pull/158
  - contributed by Tobias Roeser https://github.com/lefou
- Polyglot-scala: Updated used Scala Version (2.11.12)
  - contributed by Tobias Roeser https://github.com/lefou
- polyglot-scala: refined Dependency.copy method
  - see https://github.com/takari/polyglot-maven/pull/164
  - contributed by Tobias Roeser https://github.com/lefou
- polyglot-scala: Made output dir to pom.scala files compilation configurable via system property `polyglot.scala.outputdir`
  - see https://github.com/takari/polyglot-maven/pull/165
  - contributed by Tobias Roeser https://github.com/lefou
- polyglot-scala: Improved support and docs for configuration elements of plugins
  - see https://github.com/takari/polyglot-maven/pull/167
  - see https://github.com/takari/polyglot-maven/pull/168
  - see https://github.com/takari/polyglot-maven/pull/169
  - fixes https://github.com/takari/polyglot-maven/issues/146
  - contributed by Tobias Roeser https://github.com/lefou
- Upgrade to latest takari-pom parent
  - contributed by Manfred Moser http://www.simpligility.com
- polyglot-yaml: Support for xml attributes
  - see https://github.com/takari/polyglot-maven/pull/162
  - fixes https://github.com/takari/polyglot-maven/issues/118
  - contributed by https://github.com/dimiii
- polyglot-yaml: exclude pomFile property from serialization
  - see https://github.com/takari/polyglot-maven/pull/163
  - fixes https://github.com/takari/polyglot-maven/issues/99
  - contributed by https://github.com/dimiii
- polyglot-java: Linux support and test fixes
- polyglot-java: Moved examples into polyglot-maven-examples
- Release performed by Manfred Moser - http://www.simpligility.com

## Version 0.3.0 - 2018-03-07

- Updated Scala version
  - see https://github.com/takari/polyglot-maven/pull/135
  - contributed by Tobias Roeser https://github.com/lefou
- Scala warning fixes
  - see https://github.com/takari/polyglot-maven/pull/141
  - fixes https://github.com/takari/polyglot-maven/issues/140
  - contributed by Tobias Roeser https://github.com/lefou
- polyglot-scala: Scala syntax friendly include preprocessor
  - see https://github.com/takari/polyglot-maven/pull/133
  - contributed by Tobias Roeser https://github.com/lefou
- Added link to user of yml version
- License and Central Repository badges
  - contributed by Manfred Moser - - http://www.simpligility.com
- polyglot-scala: Use Zinc server for Scala module
  - see https://github.com/takari/polyglot-maven/pull/144
  - contributed by Tobias Roeser https://github.com/lefou
- polyglot-scala: Support more valid XML element name chars in dynamic Config
  - see https://github.com/takari/polyglot-maven/pull/145
  - contributed by Tobias Roeser https://github.com/lefou
- Experimental addition of Java as polyglot language:
  - see https://github.com/takari/polyglot-maven/pull/149
  - see https://github.com/takari/polyglot-maven/pull/150
  - resolves https://github.com/takari/polyglot-maven/issues/28
  - contributed by https://github.com/sveryovka
- Release performed by Manfred Moser - http://www.simpligility.com
  - https://github.com/takari/polyglot-maven/issues/139
  
## Version 0.2.1 - 2017-09-06

- Improved handling of string with special characters in Scala dialect
  - see https://github.com/takari/polyglot-maven/pull/131
  - fixed https://github.com/takari/polyglot-maven/issues/100
  - contributed by Tobias Roeser https://github.com/lefou
- Fixed NPE on Maven model write in Scala dialect
  - see https://github.com/takari/polyglot-maven/pull/129
  - fixes https://github.com/takari/polyglot-maven/issues/42
  - fixes https://github.com/takari/polyglot-maven/issues/88
  - contributed by Tobias Roeser https://github.com/lefou
- Added missing dependency to translate plugin
  - see https://github.com/takari/polyglot-maven/pull/129
  - fixed https://github.com/takari/polyglot-maven/issues/96
  - contributed by Tobias Roeser https://github.com/lefou
- Ruby examples usages in documentation
  - see https://github.com/takari/polyglot-maven/pull/126
  - contributed by Martin Prout https://github.com/monkstone

Release performed by Manfred Moser - http://www.simpligility.com


## Version 0.2.0 - 2017-05-23

- Upgrade to Maven 3.5.0, parent pom and plugin updates, Maven wrapper upgrade, Takari lifecycle usage
  - see https://github.com/takari/polyglot-maven/pull/124
  - contributed by Manfred Moser
- JRuby version upgrade for Java 9 support
  - see https://github.com/takari/polyglot-maven/commit/25f1c8eb2b7789515a29826bee493b3991399ddc
  - fixes https://github.com/takari/polyglot-maven/issues/123
  - contributed by Christian Meier https://github.com/mkristian
- Support for nested lists in YAML format
  - see https://github.com/takari/polyglot-maven/pull/122
  - contributed by https://github.com/callmetango
- New translate-project goal in maven plugin
  - see https://github.com/takari/polyglot-maven/pull/101
  - contributed by Tobias Roeser https://github.com/lefou
- Extended YAML model to full model
  - see https://github.com/takari/polyglot-maven/pull/81
  - contributed by Paul Verest https://github.com/paulvi
- Documentation updates

Release performed by Manfred Moser - http://www.simpligility.com

## Version 0.1.19 - 2016-08-16 and earlier

See the commit [git commit history](https://github.com/takari/maven-wrapper/commits/master).
