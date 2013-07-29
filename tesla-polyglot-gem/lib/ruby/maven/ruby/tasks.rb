require 'maven/ruby/maven'
require 'maven/tools/dsl'
require 'maven/tools/visitor'

module Maven
  class Tasks
    include Rake::DSL

    def install

      desc "Setup Maven instance."
      task :maven do
      end

      desc "Build gem into the pkg directory."
      task :build => :maven do
        Maven::Ruby::Maven.instance.package
      end

      desc "Compile any java source configured - default java files are in src/main/java."
      task :compile => :maven do
        Maven::Ruby::Maven.instance.compile
      end

      desc "Package Jarfile with the compiled classes - default jarfile lib/{name}.jar"
      task :jar => :maven do
        Maven::Ruby::Maven.instance.prepare_package
      end

      desc "Push gem to rubygems.org"
      task :push => :maven do
        Maven::Ruby::Maven.instance.deploy
      end
    end
  end
  Tasks.new.install
end

include Maven::Tools::DSL

def maven( &block )
  instance = Maven::Ruby::Maven.instance
  if block
    f = File.join( 'target', "pom4rake.xml" )
    v = Maven::Tools::Visitor.new( File.open( f, 'w' ) )
    pom = tesla( &block )
    v.accept_project( pom )
    instance.options[ '-f' ] = f
  end
  instance
end
