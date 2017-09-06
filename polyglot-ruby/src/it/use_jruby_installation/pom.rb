# Copyright (c) 2015 to original author or authors
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html

project 'Execute Ruby Tasks' do

  id 'com.example:ruby-pom:1.0-SNAPSHOT'
  
  build do
    execute("first", :initialize) do |context|
      java.lang.System.out.println "jruby version #{JRUBY_VERSION}"
    end
  end
  
end  
