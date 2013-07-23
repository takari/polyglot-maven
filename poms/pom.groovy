project {
  modelVersion '4.0.0'  
  
  parent('io.tesla:tesla:4')
  groupId 'io.tesla.polyglot'
  artifactId 'tesla-polyglot'
  version '0.0.1-SNAPSHOT'
  //$artifact('io.tesla.polyglot:tesla-polyglot:0.0.1-SNAPSHOT')
  // this will produce groupId, artifactId and version elements
  
  packaging 'pom'
  name 'Polyglot Tesla :: Aggregator'
  
  modules([
    'tesla-polyglot-common',
    'tesla-polyglot-atom',
    'tesla-polyglot-ruby',
    'tesla-polyglot-groovy',
    'tesla-polyglot-yaml',
    'tesla-polyglot-clojure',
    'tesla-polyglot-scala',
    'tesla-polyglot-cli',
    'tesla-polyglot-maven-plugin',
  ])
  
  properties {
    sisuInjectVersion '0.0.0.M2a'
    teslaVersion '3.1.0'
  }
  
  dependencyManagement {
    dependencies {
      dependency('org.eclipse.sisu:org.eclipse.sisu.inject:${sisuInjectVersion}')      
      dependency('org.eclipse.sisu:org.eclipse.sisu.plexus:${sisuInjectVersion}')      
      dependency('org.apache.maven:maven-model-builder:3.1.0')      
      dependency('org.apache.maven:maven-embedder:3.1.0')
      dependency('junit:junit:4.11:test')
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
      plugin('org.codehaus.plexus:plexus-component-metadata:1.5.4') {
        executions {
          execution(goals: ['generate-metadata', 'generate-test-metadata'])
        }
      }
    }
  }
}