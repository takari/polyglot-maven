#
# Copyright (C) 2013 Christian Meier
#
# Permission is hereby granted, free of charge, to any person obtaining a copy of
# this software and associated documentation files (the "Software"), to deal in
# the Software without restriction, including without limitation the rights to
# use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
# the Software, and to permit persons to whom the Software is furnished to do so,
# subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
# FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
# COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
# IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
# CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
#
require 'fileutils'
require 'java' if defined? JRUBY_VERSION
require 'tesla_maven'

module Maven
  module Ruby

    unless defined? JRUBY_VERSION
      require 'stringio'
      require 'maven/tools/dsl'
      require 'maven/tools/visitor'
      class POM
        include Maven::Tools::DSL

        def initialize( file = nil )
          unless file
            file = pom_file( 'pom.rb' )
            file ||= pom_file( 'Mavenfile' )
            file ||= pom_file( '*.gemspec' )
          end
          @model = to_model( file ) if file
        end

        def pom_file( pom )
          files = Dir[ pom ]
          case files.size
          when 0
          when 1
            files.first
          else
            warn 'more than one pom file found'
          end
        end

        def to_s( file = nil )
          if @model
            if file
              v = ::Maven::Tools::Visitor.new( File.open( file, 'w' ) )
              v.accept_project( @model )
              true
            else
              io = StringIO.new
              v = ::Maven::Tools::Visitor.new( io )
              v.accept_project( @model )
              io.string
            end
          end
        end

        def to_model( file )
          case file.to_s
          when /pom.rb/
            eval_pom( File.read( file ), file )
          when /Mavenfile/
            eval_pom( "tesla do\n#{ File.read( file ) }\nend", file )
          when /.+\.gemspec/
            eval_pom( "gemspec( #{ File.basename( file ) } )", file )
          end
        rescue ArgumentError
          warn 'fallback to old maven model'

          raise 'TODO old maven model'
        end
      end
    end
    class Maven

      private

      def launch_jruby(args)
        java.lang.System.setProperty( "classworlds.conf",
                                      TeslaMaven.bin( "m2.conf" ) )

        java.lang.System.setProperty( 'maven.home', TeslaMaven.maven_home )
        java.lang.System.setProperty( 'tesla.ext', TeslaMaven.ext )
        java.lang.System.setProperty( 'tesla.lib', TeslaMaven.lib )
        # loading here is needed to let tesla-ruby find jruby on
        # classloader, though m2.conf does load those libs as well
        self.class.require_classpath( TeslaMaven.maven_boot )
        self.class.require_classpath( TeslaMaven.maven_lib )
        self.class.require_classpath( TeslaMaven.lib )
        org.codehaus.plexus.classworlds.launcher.Launcher.main( args ) == 0
      end

      def self.class_world
        @class_world ||= class_world!
      end

      def self.class_world!
        require_classpath( TeslaMaven.boot )
        require_classpath( TeslaMaven.lib )
        org.codehaus.plexus.classworlds.ClassWorld.new( "plexus.core",
                                                        java.lang.Thread.currentThread().getContextClassLoader())
      end

      def self.classpath_array( dir )
        Dir.glob( File.join( dir, "*jar" ) )
      end

      def self.require_classpath( dir )
        classpath_array( dir ).each do |jar|
          require jar
        end
      end

      def adjust_args( args )
        if ! defined?( JRUBY_VERSION ) &&
           ( args.index( '-f' ) || args.index( '--file' ) ).nil?

          pom = POM.new
          pom_file = '.pom.xml'
          if pom.to_s( pom_file )
            args = args + [ '-f', pom_file ]
          else
            args
          end
        else
          args
        end
      end

      def launch_java(*args)
        # TODO works only on unix like OS
        system "java -cp #{self.class.classpath_array( ::Maven.boot ).join(':')} -Dmaven.home=#{::Maven.home} -Dclassworlds.conf=#{::Maven.bin( 'm2.conf' )} org.codehaus.plexus.classworlds.launcher.Launcher #{args.join ' '}"
      end

      def options_array
        options.collect do |k,v|
          if k =~ /^-D/
            v = "=#{v}" unless v.nil?
            "#{k}#{v}"
          else
            if v.nil?
              "#{k}"
            else
              ["#{k}", "#{v}"]
            end
          end
        end.flatten
      end
      private :options_array

      public

      def self.class_world
        @class_world ||= class_world!
      end

      def self.maven_home
        warn 'DEPRECATED use TeslaMaven.maven_home or Maven.home directly'
        TeslaMaven.maven_home
      end

      def self.instance( &block )
        @instance ||= self.new
      end

      def options
        @options ||= {}
      end

      def verbose= v
        @verbose = v
      end

      def property(key, value = nil)
        options["-D#{key}"] = value
      end

      def verbose
        if @verbose.nil?
          @verbose = options.delete('-Dverbose').to_s != 'false'
        else
          @verbose
        end
      end

      def exec(*args)
        a = args.dup + options_array
        a = adjust_args( a.flatten )
        if a.delete( '-Dverbose=true' ) || a.delete( '-Dverbose' ) || verbose
          puts "mvn #{a.join(' ')}"
        end
        if defined? JRUBY_VERSION
          puts "using jruby #{JRUBY_VERSION} invokation" if verbose
          launch_jruby(a)
        else
          puts "using java invokation" if verbose
          launch_java(a)
        end
      end

      def method_missing( method, *args )
        exec( [ method ] + args )
      end
    end
  end
end
