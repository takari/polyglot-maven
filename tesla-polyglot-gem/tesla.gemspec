# -*- mode:ruby -*-
require './lib/ruby/tesla/version'
Gem::Specification.new do |s|
  s.name = 'tesla'
  s.version = Tesla::VERSION

  s.authors = ["Christian Meier"]
  s.description = %q{maven support for rubygems based on maven 3.0. it allows to use xyz.gemspec file as pom file or the usual pom.xml files. with a rails3 application with a Gemfile (suitable for jruby). you need java installed or jruby but it will run with MRI (without installed jruby) since the maven will take care of the jruby to use.} 
  s.email = ["m.kristian@web.de"]
  s.extra_rdoc_files = ["NOTICE.txt", "LICENSE.txt", "README.txt"]

  s.license = 'MIT' 

  s.files = Dir.glob("*.txt") +
    Dir.glob("bin/mvn*") +
    Dir.glob("bin/tesla") +
    Dir.glob("bin/m2.conf") +
    Dir.glob("boot/*") +
    Dir.glob("conf/*") +
    Dir.glob("ext/ruby/*") +
    Dir.glob("lib/*") +
    Dir.glob("lib/ext/*") +
    Dir.glob("lib/ruby/**/*.rb")
  s.bindir = "bin"
  s.executables = ['tesla']
  s.homepage = %q{http://github.com/mkristian/ruby-maven}
  s.rdoc_options = ["--main", "README.md"]
  s.require_paths = ['lib/ruby']
  s.rubygems_version = %q{1.3.5}
  s.summary = %q{maven support for ruby projects with gemspec, Gemfile}
  s.add_dependency 'thor', '>= 0.14.6', '< 2.0' # mimic rails
  # TODO come back to version semantic in maven-tools
  s.add_dependency 'maven-tools', "~> 0.32.3" 
  s.add_development_dependency 'minitest', '~> 5.0'  
  s.add_development_dependency 'rake', '~> 10.0'

  File.chmod(0755, File.join("bin", "mvn")) rescue nil
end

# vim: syntax=Ruby
