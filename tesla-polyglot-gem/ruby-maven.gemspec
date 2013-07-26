# -*- mode:ruby -*-
require './lib/ruby/maven/ruby/version'
Gem::Specification.new do |s|
  s.name = 'ruby-maven'
  s.version = Maven::Ruby::VERSION

  s.authors = ["Christian Meier"]
  s.description = %q{maven support for ruby based on tesla maven. MRI needs java/javac command installed.} 
  s.email = ["m.kristian@web.de"]
  s.extra_rdoc_files = ["NOTICE.txt", "LICENSE.txt", "README.txt"]

  s.license = 'EPL' 

  Dir.glob("ext/*").each { |d| File.write( File.join( d, '.keep' ), '' ) }

  s.files = Dir.glob("*.txt") +
    Dir.glob("bin/rmvn") +
    Dir.glob("bin/m2.conf") +
    Dir.glob("boot/*") +
    Dir.glob("conf/**/*") +
    Dir.glob("ext/*/.keep") +
    Dir.glob("ext/ruby/*") +
    Dir.glob("lib/*") +
    Dir.glob("lib/ext/*") +
    Dir.glob("lib/ruby/**/*.rb")
  s.bindir = "bin"
  s.executables = ['rmvn']
  s.homepage = %q{https://github.com/tesla/tesla-polyglot/tree/master/tesla-polyglot-gem}
  s.rdoc_options = ["--main", "README.md"]
  s.require_paths = ['lib/ruby']
  s.rubygems_version = %q{1.3.5}
  s.summary = %q{maven support for ruby projects}
  s.add_dependency 'maven-tools', "~> 0.33" 
  s.add_development_dependency 'minitest', '~> 5.0'  
  s.add_development_dependency 'rake', '~> 10.0'
end

# vim: syntax=Ruby
