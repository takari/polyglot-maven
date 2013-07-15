project {
  modelVersion '4.0.0'
  parent {
    groupId 'io.tesla'
    artifactId 'tesla'
    version '4'
  }
  groupId 'io.tesla.polyglot'
  artifactId 'tesla-polyglot'
  version '0.0.1-SNAPSHOT'
  packaging 'pom'
  name 'Polyglot Tesla :: Aggregator'
  modules {
    module 'tesla-polyglot-common'
    module 'tesla-polyglot-atom'
    module 'tesla-polyglot-groovy'
    module 'tesla-polyglot-yaml'
    module 'tesla-polyglot-clojure'
    module 'tesla-polyglot-scala'
    module 'tesla-polyglot-cli'
    module 'tesla-polyglot-maven-plugin'
  }
  properties {
    sisuInjectVersion '0.0.0.M2a'
    teslaVersion '3.1.0'
  }
  dependencyManagement {
    dependencies {
      dependency {
        groupId 'org.eclipse.sisu'
        artifactId 'org.eclipse.sisu.inject'
        version '${sisuInjectVersion}'
      }
      dependency {
        groupId 'org.eclipse.sisu'
        artifactId 'org.eclipse.sisu.plexus'
        version '${sisuInjectVersion}'
      }
      dependency {
        groupId 'org.apache.maven'
        artifactId 'maven-model-builder'
        version '3.1.0'
      }
      dependency {
        groupId 'org.apache.maven'
        artifactId 'maven-embedder'
        version '3.1.0'
      }
      dependency {
        groupId 'junit'
        artifactId 'junit'
        version '4.11'
        scope 'test'
      }
    }
  }
  build {
    //
    // Arbitrary Groovy code can be executed in any phase in the form of a dynamic plugin
    //
    $execute(id: 'hello', phase: 'validate') {
      println ""
      println "hello, I am Groovy inside Maven. What? What am I doing here?? I thought he was my arch nemesis! I'm confused."
      println ""
    }       
    
    plugins {
      plugin {
        groupId 'org.codehaus.plexus'
        artifactId 'plexus-component-metadata'
        version '1.5.4'
        executions {
          execution {
            goals {
              goal 'generate-metadata'
              goal 'generate-test-metadata'
            }
          }
        }
      }
    }
  }
}
