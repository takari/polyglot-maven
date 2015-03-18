# Overview

[Polyglot for Maven](http://github.com/tesla/tesla-polyglot/) is a set of extensions for `Maven 3.3.1+` that allows the POM model to be written in dialects other than XML. Several the dialects also allow inlined plugins: the Ruby, Groovy and Scala dialects allow this.

Here's an example POM written in the Ruby dialect:

```ruby
project 'Polyglot :: Aggregator' do

  model_version '4.0.0'
  id 'io.tesla.polyglot:tesla-polyglot:0.0.1-SNAPSHOT'
  inherit 'io.tesla:tesla:4'
  packaging 'pom'

  properties( 'sisuInjectVersion' => '0.0.0.M2a',
              'teslaVersion' => '3.1.0' )

  modules [ 'tesla-polyglot-common',
            'tesla-polyglot-atom',
            'tesla-polyglot-ruby',
            'tesla-polyglot-groovy',
            'tesla-polyglot-yaml',
            'tesla-polyglot-clojure',
            'tesla-polyglot-scala',
            'tesla-polyglot-cli',
            'tesla-polyglot-maven-plugin' ]

  overrides do
    jar 'org.eclipse.sisu:org.eclipse.sisu.inject:${sisuInjectVersion}'
    jar 'org.eclipse.sisu:org.eclipse.sisu.plexus:${sisuInjectVersion}'
    jar 'org.apache.maven:maven-model-builder:3.1.0'
    jar 'org.apache.maven:maven-embedder:3.1.0'
    jar( 'junit:junit:4.11', :scope => 'test' )

  end

  plugin 'org.codehaus.plexus:plexus-component-metadata:1.5.4' do
    execute_goals 'generate-metadata', 'generate-test-metadata'
  end

  build do
    execute("first", :validate) do |context|
      puts "Hello from JRuby!"
    end
  end
end
```

# Building

### Requirements

* [Maven](http://maven.apache.org) 3.3.1+
* [Java](http://java.sun.com/) 7+

# Note of caution

The whole interoperability story has not been worked out but we expect to sort this out very quickly now that Polyglot for Maven can be used easily.

A pom.xml will currently not be installed or deployed except for the Ruby DSL but we will add this feature very shortly.
