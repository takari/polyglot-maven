# -*- mode:ruby -*-
require './ruby/maven'
Gem::Specification.new do |s|
  s.name = 'ruby-maven-libs'
  s.version = Maven::VERSION

  s.authors = ["Christian Meier"]
  s.description = %q{maven distribution as gem - no ruby executables !} 
  s.email = ["m.kristian@web.de"]
  s.extra_rdoc_files = Dir.glob("*.txt") +
    Dir.glob( "NOTICE*" ) + 
    Dir.glob( "LICENSE*" )

  s.license = 'APL' 

  s.files = Dir.glob("*.txt") +
    Dir.glob("bin/*") +
    Dir.glob("boot/*") +
    Dir.glob("conf/**/*") +
    Dir.glob("ruby/**/*") +
    Dir.glob("lib/**/*")
  s.homepage = %q{https://github.com/tesla/tesla-polyglot/tree/master/tesla-polyglot-gem/ruby-maven-libs}
  s.require_path = 'ruby'
  s.summary = %q{maven distribution as gem}
end

# vim: syntax=Ruby
