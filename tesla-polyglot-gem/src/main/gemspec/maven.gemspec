# -*- encoding: utf-8 -*-

Gem::Specification.new do |s|
  s.name = %q{ruby-maven}
  s.version = "#{File.basename(File.expand_path('..')).sub(/-SNAPSHOT/, '').sub(/[a-zA-Z-]+-/, '').gsub(/-/, '.')}"
  s.platform = 'java'
  s.required_rubygems_version = Gem::Requirement.new("> 1.3.1") if s.respond_to? :required_rubygems_version=
  s.authors = ["mkristian"]
  s.description = %q{maven support for rubygems based on maven 3.0. it allows to use xyz.gemspec file as pom file or the usual pom.xml files. with a rails3 application with a Gemfile (suitable for jruby) you can run jetty as development server. this gem is a stripped polyglot-maven which includes only the jruby part. the executable runs only with jruby.}
  s.email = ["m.kristian@web.de"]
  s.extra_rdoc_files = ["NOTICE.txt", "LICENSE.txt", "README.txt"]
  s.files = Dir.glob("*.txt") +
    Dir.glob("bin/jetty-run") +
    Dir.glob("bin/rmvn") +
    Dir.glob("bin/m2.conf") +
    Dir.glob("boot/*") +
    Dir.glob("conf/*") +
    Dir.glob("lib/*")
  ext = Dir.glob("ext/*ruby*")
  ext.delete(ext.detect{ |f| f =~ /jruby-complete/ })
  s.files += ext
  s.bindir = "bin"
  s.executables = ['rmvn', 'jetty-run']
  s.homepage = %q{http://github.com/sonatype/polyglot-maven}
  s.rdoc_options = ["--main", "README.txt"]
  s.require_paths = ['none']
  s.rubygems_version = %q{1.3.5}
  s.summary = %q{maven support for rubygems}

end

