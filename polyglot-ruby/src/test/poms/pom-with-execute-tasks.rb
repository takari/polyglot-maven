# Copyright (c) 2015 to original author or authors
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html

project 'Execute Ruby Tasks' do

  id 'com.example:ruby-pom:1.0-SNAPSHOT'
  packaging 'pom'

  jar 'com.example:jar:1.0'
  
  build do
    execute("first", :initialize) do |context|
      java.lang.System.out.print context.project.name
    end
    execute(:second, :initialize) do |context|
      pt = context.project
      java.lang.System.out.print "#{pt.group_id}:#{pt.artifact_id}:#{pt.version}:#{pt.packaging}" 
    end
    execute(:third, :initialize) do
      java.lang.System.out.print where 
    end
  end
  
  def where
	"#{self.inspect.sub(/:0x[0-9a-f]{8}/,'')}"
  end
end  
