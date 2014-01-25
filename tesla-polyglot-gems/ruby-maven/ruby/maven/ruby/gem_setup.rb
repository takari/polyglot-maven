require 'maven/ruby/maven'
module Maven
  module Ruby
    class GemSetup
      
      def self.setup( specfile )
        basedir = File.dirname( File.expand_path( specfile ) )
        extdir = File.join( basedir, 'ext' )
        return unless File.exists? extdir

        deps = File.join( extdir, 'deps.lst' )

        File.open( File.join( extdir, 'Makefile' ), 'w' ) do |f|
          f.print <<EOF
install:
clean:
EOF
        end

        spec = eval( File.read( specfile ) )
        jars_file = File.join( basedir, spec.require_path, 
                               "#{spec.name}_jars.rb" )

        return if File.exists?( jars_file ) && 
          File.mtime( specfile ) < File.mtime( jars_file )

        maven = Maven.new
        maven.exec 'dependency:list', "-DoutputFile=#{deps}", '-DincludeScope=runtime', '-DoutputAbsoluteArtifactFilename=true', '-DincludeTypes=jar', '-DoutputScope=false', '-f', specfile
        File.open( jars_file, 'w' ) do |f|
          f.puts "require 'jar-dependencies'"
          f.puts
          File.read( deps ).each_line do |line|
            if line.match /:jar:/
              line.gsub!( /:jar:|:compile:|:runtime:/, ':' )
              line.sub!( /^\s+/, '' )
              path = line.sub( /^.*:/, '' )
              args = line.sub( /:[^:]+$/, '' ).gsub( /:/, "', '" )
              f.puts( "require_jarfile( '#{path}', '#{args}' )" )
            end
          end
        end
      end
    end
  end
end
