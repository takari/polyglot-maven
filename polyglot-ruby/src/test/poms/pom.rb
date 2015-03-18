project 'Apache Maven', 'http://maven.apache.org/' do

  id 'org.apache.maven:maven', '3.0.4-SNAPSHOT'
  inherit 'org.eclipse.tesla:tesla', '3'
  packaging 'pom'

  description 'Maven is a project development management and
    comprehension tool. Based on the concept of a project object model:
    builds, dependency management, documentation creation, site
    publication, and distribution publication are all controlled from
    the declarative file. Maven can be extended by plugins to utilise a
    number of other development tools for reporting or the build
    process. All of Square\'s Java projects. It\'s like a party for your code.'

  properties( 'gossipVersion' => '1.7',
              'aetherVersion' => '1.12',
              'junitVersion' => '4.8.2',
              'plexusUtilsVersion' => '2.0.6',
              'classWorldsVersion' => '2.4',
              'project.build.sourceEncoding' => 'UTF-8',
              'maven.build.timestamp.format' => 'yyyyMMddHHmm',
              'sisuInjectVersion' => '2.2.1',
              'modelloVersion' => '1.4.1',
              'distributionName' => 'Eclipse Tesla',
              'plexusVersion' => '1.5.5',
              'mavenVersion' => '3.0.4-SNAPSHOT',
              'easyMockVersion' => '1.2_Java1.3',
              'jlineVersion' => '2.3',
              'jxpathVersion' => '1.3',
              'maven.test.redirectTestOutputToFile' => 'true',
              'commonsCliVersion' => '1.2',
              'project.reporting.outputEncoding' => 'UTF-8',
              'plexusInterpolationVersion' => '1.14',
              'distributionShortName' => 'Tesla',
              'wagonVersion' => '1.0-beta-7',
              'build.timestamp' => '${maven.build.timestamp}',
              'securityDispatcherVersion' => '1.3',
              'distributionId' => 'eclipse-tesla',
              'slf4jVersion' => '1.6.1',
              'cipherVersion' => '1.7',
              'gshellVersion' => '3.0.4-SNAPSHOT' )

  scope :test do
    jar 'junit:junit', '${junitVersion}'
  end

  modules [ 'maven-core',
            'apache-maven',
            'maven-model',
            'maven-settings',
            'maven-settings-builder',
            'maven-artifact',
            'maven-aether-provider',
            'maven-repository-metadata',
            'maven-plugin-api',
            'maven-model-builder',
            'maven-embedder',
            'maven-compat',
            'tesla-shell',
            'tesla-polyglot' ]

  overrides do

    jar 'org.apache.maven:maven-model', '${project.version}'
    jar 'org.apache.maven:maven-settings', '${project.version}'
    jar 'org.apache.maven:maven-settings-builder', '${project.version}'
    jar 'org.apache.maven:maven-plugin-api', '${project.version}'
    jar 'org.apache.maven:maven-embedder', '${project.version}'
    jar 'org.apache.maven:maven-core', '${project.version}'
    jar 'org.apache.maven:maven-model-builder', '${project.version}'
    jar 'org.apache.maven:maven-compat', '${project.version}'
    jar 'org.apache.maven:maven-artifact', '${project.version}'
    jar 'org.apache.maven:maven-aether-provider', '${project.version}'
    jar 'org.apache.maven:maven-repository-metadata', '${project.version}'
    jar 'org.codehaus.plexus:plexus-utils', '${plexusUtilsVersion}'
    jar( 'org.sonatype.sisu:sisu-inject-plexus', '${sisuInjectVersion}',
         :exclusions => 'org.sonatype.sisu.inject:cglib' )
    jar 'org.sonatype.sisu:sisu-inject-bean', '2.2.1'
    jar( 'org.codehaus.plexus:plexus-component-annotations', '${plexusVersion}',
         :exclusions => 'junit:junit' )
    jar 'org.codehaus.plexus:plexus-classworlds', '${classWorldsVersion}'
    jar 'org.codehaus.plexus:plexus-interpolation', '${plexusInterpolationVersion}'
    jar 'org.slf4j:slf4j-api', '${slf4jVersion}'
    scope :runtime do
      jar 'org.slf4j:slf4j-simple', '${slf4jVersion}'
    end
    jar 'org.apache.maven.wagon:wagon-provider-api', '${wagonVersion}'
    jar 'org.apache.maven.wagon:wagon-file', '${wagonVersion}'
    jar( 'org.apache.maven.wagon:wagon-http-lightweight', '${wagonVersion}',
         :exclusions => 'commons-logging:commons-logging' )
    jar( 'org.apache.maven.wagon:wagon-http-shared', '${wagonVersion}',
         :exclusions => 'commons-logging:commons-logging' )
    jar 'org.sonatype.aether:aether-api', '${aetherVersion}'
    jar 'org.sonatype.aether:aether-spi', '${aetherVersion}'
    jar 'org.sonatype.aether:aether-impl', '${aetherVersion}'
    jar 'org.sonatype.aether:aether-util', '${aetherVersion}'
    jar( 'org.sonatype.aether:aether-connector-asynchttpclient', '${aetherVersion}',
         :exclusions => 'org.codehaus.plexus:plexus-container-default' )
    jar( 'org.sonatype.aether:aether-connector-file', '${aetherVersion}',
         :exclusions => 'org.codehaus.plexus:plexus-container-default' )
    jar( 'org.sonatype.aether:aether-connector-wagon', '${aetherVersion}',
         :exclusions => 'org.codehaus.plexus:plexus-container-default' )
    jar 'org.sonatype.maven:wagon-ahc', '1.2.0'
    jar( 'commons-cli:commons-cli', '${commonsCliVersion}',
         :exclusions => [ 'commons-lang:commons-lang',
                          'commons-logging:commons-logging' ] )
    jar 'commons-jxpath:commons-jxpath', '${jxpathVersion}'
    jar 'org.sonatype.plexus:plexus-sec-dispatcher', '${securityDispatcherVersion}'
    jar 'org.sonatype.plexus:plexus-cipher', '${cipherVersion}'
    jar 'org.sonatype.gossip:gossip-core', '${gossipVersion}'
    jar 'org.sonatype.gossip:gossip-bootstrap', '${gossipVersion}'
    jar 'org.sonatype.gossip:gossip-slf4j', '${gossipVersion}'
    jar 'org.sonatype.gossip:gossip-support', '${gossipVersion}'
    jar 'org.sonatype.grrrowl:grrrowl', '1.1.1'
    jar 'com.thoughtworks.xstream:xstream', '1.3.1'
    jar 'commons-codec:commons-codec', '1.4'
    jar 'org.eclipse.tesla.shell:tesla-shell-core', '${gshellVersion}'
    jar 'org.eclipse.tesla.shell:tesla-shell-core', '${gshellVersion}:tests'
    jar 'org.eclipse.tesla.shell:tesla-shell-util', '${gshellVersion}'
    jar 'org.eclipse.tesla.shell.ext:tesla-shell-gossip', '${gshellVersion}'
    jar 'org.eclipse.tesla.shell.ext:tesla-shell-plexus', '${gshellVersion}'
    jar 'org.eclipse.tesla.shell.ext:tesla-shell-logback', '${gshellVersion}'
    jar 'org.eclipse.tesla.shell.commands:tesla-shell-standard', '${gshellVersion}'
    jar 'org.eclipse.tesla.shell.commands:tesla-shell-logging', '${gshellVersion}'
    jar 'org.eclipse.tesla.shell.commands:tesla-shell-pref', '${gshellVersion}'
    jar 'org.eclipse.tesla.shell.commands:tesla-shell-network', '${gshellVersion}'
    jar 'org.eclipse.tesla.shell.commands:tesla-shell-file', '${gshellVersion}'
    jar 'org.eclipse.tesla.shell.commands:tesla-shell-pom', '${gshellVersion}'
    jar 'org.eclipse.tesla.shell:tesla-shell-maven', '${mavenVersion}'
    jar 'org.eclipse.tesla.shell:tesla-shell-personality', '${mavenVersion}'
    jar 'org.eclipse.tesla.polyglot:tesla-polyglot-common', '${mavenVersion}'
    jar 'org.eclipse.tesla.polyglot:tesla-polyglot-ruby', '${mavenVersion}'
    jar 'org.slf4j:jcl-over-slf4j', '${slf4jVersion}'
    jar 'ch.qos.logback:logback-classic', '0.9.29'
    jar 'org.sonatype.jline:jline', '${jlineVersion}'
    jar 'org.sonatype.jline:jline', '${jlineVersion}', :classifier => 'tests'
    jar( 'commons-jexl:commons-jexl', '1.1',
         :exclusions => ['commons-logging:commons-logging',
                        'junit:junit'] )
    scope :test do
      jar 'easymock:easymock', '${easyMockVersion}'
    end

    plugin( 'org.codehaus.plexus:plexus-component-metadata',
            '${plexusVersion}' ) do
      execute_goals 'generate-metadata', 'generate-test-metadata'
    end
    plugin( :compiler, '2.3.2',
            :source => '1.5',
            :target => '1.5' )
    plugin( :release,
            :tagBase => 'https://svn.apache.org/repos/asf/maven/maven-3/tags',
            :autoVersionSubmodules => 'true' )
    plugin( :surefire, '2.8.1',
            :argLine => '-Xmx256m' )
    plugin( 'org.codehaus.modello:modello-maven-plugin', '${modelloVersion}',
            :useJava5 => 'true' ) do
      execute_goals( 'xdoc', 'xsd',
                     :id => 'site-docs',
                     :phase => 'pre-site' )
      execute_goals( 'java', 'xpp3-reader', 'xpp3-writer',
                     :id => 'standard' )
    end
    plugin 'org.apache.felix:maven-bundle-plugin', '1.0.0'
    plugin :assembly, '2.2-beta-5'
    plugin :javadoc, '2.5'
    plugin :resources, '2.4.2'
    plugin 'remote-resources', '1.1'
    plugin :site, '2.1'
    plugin 'org.codehaus.mojo:javacc-maven-plugin', '2.6'
    plugin 'org.sonatype.plugins:sisu-maven-plugin', '1.1'
    plugin( 'org.eclipse.m2e:lifecycle-mapping', '1.0.0',
            :lifecycleMappingMetadata => {
              :pluginExecutions => [ {
                :pluginExecutionFilter => {
                  :groupId => 'org.sonatype.plugins',
                  :artifactId => 'sisu-maven-plugin',
                  :versionRange => '[1.1,)',
                  :goals => [ 'main-index',
                              'test-index' ]
                },
                :action => {
                  :ignore => true
                }
              } ]
            } )
  end

  phase 'process-classes' do
    plugin( 'org.codehaus.mojo:animal-sniffer-maven-plugin', '1.6',
            :signature => {
              :groupId => 'org.codehaus.mojo.signature',
              :artifactId => 'java15',
              :version => '1.0'
            } ) do
      execute_goals( 'check',
                     :id => 'check-java-1.5-compat' )
    end
  end

  plugin 'org.sonatype.plugins:sisu-maven-plugin' do
    execute_goals 'main-index', 'test-index'
  end

  plugin( 'com.mycila.maven-license-plugin:maven-license-plugin', '1.9.0',
          :aggregate => 'true',
          :strictCheck => 'true',
          :header => '${project.basedir}/header.txt',
          :useDefaultExcludes => 'false',
          :includes => [ '**/pom.xml',
                         '**/*.xml',
                         '**/*.xsd',
                         '**/*.xjb',
                         '**/*.mdo',
                         '**/*.properties',
                         '**/*.java',
                         '**/*.groovy',
                         '**/*.scala',
                         '**/*.aj',
                         '**/*.js',
                         '**/*.css' ],
          :excludes => [ '**/target/**' ],
          :mapping => {
            :scala => 'JAVADOC_STYLE',
            :xjb => 'XML_STYLE',
            :mdo => 'XML_STYLE'
          } )
end
