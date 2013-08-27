begin
  require 'minitest'
rescue LoadError
end

require 'minitest/autorun'
require 'rake'
require 'maven/ruby/maven'

unless defined? My
  class My


    def initialize( dir )
      @dir = File.join( File.expand_path( File.dirname( __FILE__ ) ),
                        dir )
    end

    def dir( file )
      File.join( @dir, file )
    end

    def chdir( dir )
      Dir.chdir( dir )
      java.lang.System.setProperty( 'user.dir', dir ) if defined? JRUBY_VERSION
    end
    private :chdir

    def rmvn( *args )    
      chdir( @dir )
      if defined? JRUBY_VERSION
        a = args + [ '-e', '-f', 
                     File.join( @dir, File.basename( @dir ) ) + '.gemspec' ]
      else
        a = args
      end
      if defined? JRUBY_VERSION
        warn 'jruby does not work (yet) for those specs'
      else
        Maven::Ruby::Maven.new.exec( *a )
      end
    end

    def exec( *args )
      do_exec_in( @dir, *args )
    end

    def exec_in( dir, *args )
      do_exec_in( File.join( File.expand_path( File.dirname( __FILE__ ) ),
                             dir ), *args )
    end

    def do_exec_in( dir, *args )
      ARGV.replace( args + [ '-f',  File.join( dir, 'Rakefile') ] )
      chdir( dir )
      Rake.application.instance_variable_set( '@original_dir', dir )
      Rake.application.run
    end
  end
end
