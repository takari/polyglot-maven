project 'tesla-polyglot-gems' do

  model_version '4.0.0'
  id 'io.tesla.polyglot:tesla-polyglot-gems:0.0.1-SNAPSHOT'
  inherit 'io.tesla.polyglot:tesla-polyglot:0.0.1-SNAPSHOT'
  packaging 'pom'

  modules [ 'ruby-maven-libs',
            'ruby-maven' ]

  plugin( 'io.tesla.maven.plugins:tesla-license-plugin',
          'header' =>  '${project.parent.basedir}/license-header.txt',
          'excludes' => [ '**/conf/**',
                          '**/pkg/**',
                          '**/*pom.xml' ] )
end
