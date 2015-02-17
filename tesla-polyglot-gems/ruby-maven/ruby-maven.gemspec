# -*- mode:ruby -*-

$LOAD_PATH << File.dirname( File.expand_path( __FILE__, '..' ) ) + '/lib'
require 'maven/ruby/version'

Gem::Specification.new do |s|
  s.name = 'ruby-maven'
  s.version = Maven::Ruby::VERSION

  s.authors = ["Christian Meier"]
  s.description = %q{maven support for ruby based on tesla maven. MRI needs java/javac command installed.} 
  s.email = ["m.kristian@web.de"]

  s.license = 'EPL' 

  s.files = Dir.glob("*.txt") +
    Dir.glob("bin/rmvn") +
    Dir.glob("ruby-maven-home/bin/m2*") +
    Dir.glob("ruby-maven-home/ext/ruby/maven-tools-*jar") +
    Dir.glob("ruby-maven-home/ext/ruby/tesla-*jar") +
    Dir.glob("lib/**/*.rb") +
    Dir.glob("ruby-maven-home/lib/tesla*.jar")
  s.bindir = "bin"
  s.executables = ['rmvn', 'dumppom']
  s.homepage = %q{https://github.com/takari/maven-polyglot/tree/master/tesla-polyglot-gems}
  s.rdoc_options = ["--main", "README.md"]
  s.require_paths = ['lib']
  s.rubygems_version = %q{1.3.5}
  s.summary = %q{maven support for ruby projects}
  s.add_dependency 'maven-tools', "~> 1.0.8" 
  s.add_dependency 'ruby-maven-libs', "=#{Maven::Ruby::MAVEN_VERSION}"
  s.add_development_dependency 'minitest', '~> 5.3'  
  s.add_development_dependency 'rake', '~> 10.3'
end

# vim: syntax=Ruby
