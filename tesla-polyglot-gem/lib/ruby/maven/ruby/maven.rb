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

module Maven
 
  class RubyMaven

    def self.new(*args)
      warn "deprecated: use Maven::Ruby::Cli or Maven::Ruby::Maven instead"
      require 'maven/ruby/cli'
      ::Maven::Ruby::Cli.new(*args)
    end
  end

  module Ruby
    class Maven

      private

      def launch_jruby(args)
        java.lang.System.setProperty("classworlds.conf", 
                                     File.join(self.class.maven_home, 'bin', "m2.conf"))

        java.lang.System.setProperty("maven.home", self.class.maven_home)
        cw = self.class.class_world
        org.apache.maven.cli.MavenCli.doMain( args, cw ) == 0
      end

      def self.class_world
        @class_world ||= class_world!
      end

      def self.class_world!
        (classpath_array + classpath_array('lib')).each do |path|
          require path
        end
        org.codehaus.plexus.classworlds.ClassWorld.new("plexus.core", java.lang.Thread.currentThread().getContextClassLoader())
      end
      
      def self.classpath_array(dir = 'boot')
        Dir.glob(File.join(maven_home, dir, "*jar"))
      end
      
      def launch_java(*args)
        system "java -cp #{self.class.classpath_array.join(':')} -Dmaven.home=#{File.expand_path(self.class.maven_home)} -Dclassworlds.conf=#{File.expand_path(File.join(self.class.maven_home, 'bin', 'm2.conf'))} org.codehaus.plexus.classworlds.launcher.Launcher #{args.join ' '}"
      end
      
      def options_string
        options_array.join ' '
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

      public

      def self.class_world
        @class_world ||= class_world!
      end

      def self.maven_home
        @maven_home ||= File.expand_path(File.join(File.dirname(__FILE__),
                                                   '..',
                                                   '..',
                                                   '..',
                                                   '..'))
      end

      def self.instance
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
          @verbose = options.delete('-Dverbose').to_s == 'true'
        else
          @verbose
        end
      end

      def exec(*args)
        a = args.dup + options_array
        a.flatten!
        puts "mvn #{a.join(' ')}" if verbose
        if defined? JRUBY_VERSION
          puts "using jruby #{JRUBY_VERSION} invokation" if verbose
          launch_jruby(a)
        else
          puts "using java invokation" if verbose
          launch_java(a)
        end
      end

      def method_missing( method, *args )
        exec( method )
      end
    end
  end
end
