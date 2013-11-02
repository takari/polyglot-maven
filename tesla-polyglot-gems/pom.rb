project 'tesla-polyglot-gems' do

  inherit 'io.tesla.polyglot:tesla-polyglot:0.0.8'
  id 'tesla-polyglot-gems'
  packaging 'pom'

  modules [ 'ruby-maven-libs',
            'ruby-maven' ]

  plugin( 'io.tesla.maven.plugins:tesla-license-plugin',
          'header' =>  '${project.parent.basedir}/license-header.txt',
          'excludes' => [ '**/conf/**',
                          '**/pkg/**',
                          '**/*pom.xml' ] )
end
