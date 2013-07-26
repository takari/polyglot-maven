
require 'maven/ruby/maven'

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
