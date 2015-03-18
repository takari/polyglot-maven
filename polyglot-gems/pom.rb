require File.join( basedir,
                   'ruby-maven',
                   'ruby',
                   'tesla_maven' )

project 'polyglot-gems' do

  inherit "io.takari.polyglot:polyglot:#{TeslaMaven::VERSION}"
  id 'tesla-polyglot-gems'
  packaging 'pom'

  modules [ 'ruby-maven-libs',
            'ruby-maven' ]

  plugin( 'io.tesla.maven.plugins:tesla-license-plugin',
          'header' =>  '${basedir}/../license-header.txt',
          'excludes' => [ '**/conf/**',
                          '**/pkg/**',
                          '**/*pom.xml' ] )
end
