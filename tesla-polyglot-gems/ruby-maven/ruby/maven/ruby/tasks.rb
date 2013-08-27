require 'maven/ruby/maven'
require 'maven/tools/dsl'
require 'rake'

module Maven
  class Tasks
    include Rake::DSL

    def self.instance( maven = nil )
      @maven = maven if maven
      @maven
    end

    def install

      desc "Setup Maven instance."
      task :maven do
      end

      desc "Clean up the build directory."
      task :clean => :maven do
        maven.clean
      end

      desc "Run the java unit tests from src/test/java directory."
      task :junit => :maven do
        maven.exec( 'compile', 'resources:testResources', 'compiler:testCompile', 'surefire:test' )
      end

      desc "Build gem into the pkg directory."
      task :build => :maven do
        maven.package
      end

      desc "Compile any java source configured - default java files are in src/main/java."
      task :compile => :maven do
        maven.compile
      end

      desc "Package jar-file with the compiled classes - default jar-file lib/{name}.jar"
      task :jar => :maven do
        maven.prepare_package( '-Dmaven.test.skip' )
      end

      desc "Build the gem"
      task :build => :maven do
        maven.package( '-Dmaven.test.skip' )
      end

      desc "Push gem to rubygems.org"
      task :push => :maven do
        maven.deploy( '-Dmaven.test.skip' )
      end
    end
  end
  Tasks.new.install
end

#include Maven::Tools::DSL

def maven( &block )
  if block
    require 'maven/tools/model'
    Maven::Tasks.instance( Maven::Ruby::Maven.new( tesla( &block ),
                                                   '.rake.pom.xml' ) )
  else
    if Maven::Tasks.instance
      Maven::Tasks.instance
    else
      m = Maven::Ruby::Maven.new
      m.embedded = true
      Maven::Tasks.instance( m )
    end
  end
end
