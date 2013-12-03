require File.join( basedir,
                   'ruby-maven',
                   'ruby',
                   'tesla_maven' )

project 'tesla-polyglot-gems' do

  inherit "io.tesla.polyglot:tesla-polyglot:#{TeslaMaven::VERSION}"
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
