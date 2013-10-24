# -*- mode:ruby -*-
$LOAD_PATH.unshift File.expand_path( File.dirname( __FILE__ ) ) + '/../ruby-maven-libs/ruby'
require './ruby/maven/ruby/version'
Gem::Specification.new do |s|
  s.name = 'ruby-maven'
  s.version = Maven::Ruby::VERSION

  s.authors = ["Christian Meier"]
  s.description = %q{maven support for ruby based on tesla maven. MRI needs java/javac command installed.} 
  s.email = ["m.kristian@web.de"]

  s.license = 'EPL' 

  s.files = Dir.glob("*.txt") +
    Dir.glob("bin/rmvn") +
    Dir.glob("bin/m2.conf") +
    Dir.glob("ext/ruby/maven-tools-*") +
    Dir.glob("ext/ruby/tesla-*") +
    Dir.glob("ruby/**/*.rb") +
    Dir.glob("lib/tesla*.jar")
  s.bindir = "bin"
  s.executables = ['rmvn']
  s.homepage = %q{https://github.com/tesla/tesla-polyglot/tree/master/tesla-polyglot-gem}
  s.rdoc_options = ["--main", "README.md"]
  s.require_paths = ['ruby']
  s.rubygems_version = %q{1.3.5}
  s.summary = %q{maven support for ruby projects}
  s.add_dependency 'maven-tools', "~> 0.33" 
  s.add_dependency 'ruby-maven-libs', "=#{Maven::Ruby::MAVEN_VERSION}"
  s.add_development_dependency 'minitest', '~> 5.0'  
  s.add_development_dependency 'rake', '~> 10.0'
end

# vim: syntax=Ruby
