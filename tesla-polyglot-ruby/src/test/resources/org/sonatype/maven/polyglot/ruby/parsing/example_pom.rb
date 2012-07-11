# Example POM in Ruby
# Author: m.kristian

repositories [ "http://repository.codehaus.org", 
               "http://maven.org/central",
               "http://repo1.maven.org/maven2" ]


project "Google Guice", "http://code.google.com/p/google-guice" do
  id 'com.google.inject:guice:2.0-SNAPSHOT'
  srcs  :src => "src", :test => "test"
  
  jar 'junit:junit:3.8.1', :scope => :test
  jar 'junit:junit:4.0', :scope => :test
  jar 'kunit:org.kunit:SNAPSHOT-1.0b:jdk15', :exclusions => ['junit:junit']
          
  scm do 
    url "url:git:git@github.com:mikebrock/mvel.git",
    connection "con:git:git@github.com:mikebrock/mvel.git",
    developerConnection "dev:git:git@github.com:mikebrock/mvel.git"
  end
end


plugin 'maven-compiler-plugin:2.0.1',
  :source => '1.5',
  :target => '1.5',
  :encoding => 'UTF-8'


plugin 'maven-surefire-plugin:2.0.1',
  :childDelegation => true
  :properties => { 'mvel.disable.jit' => true,
                   'file.encoding' => "UTF-8" }
  :includes =>   [ "**/*Test.java",
                   "**/*Tests.java" ]
