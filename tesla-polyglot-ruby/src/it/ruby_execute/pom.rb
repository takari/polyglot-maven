project 'Execute Ruby Tasks' do

  id 'com.example:ruby-pom:1.0-SNAPSHOT'
  
  build do
    execute("first", :initialize) do |context|
      java.lang.System.out.println context.project.name
    end
    execute(:second, :initialize) do |context|
      pt = context.project
      java.lang.System.out.println "#{pt.group_id}:#{pt.artifact_id}:#{pt.version}:#{pt.packaging}" 
    end
    execute(:third, :initialize) do
      java.lang.System.out.println where 
    end
  end
  
  def where
    "#{self.inspect.sub(/:0x[0-9a-f]{8}/,'')}"
  end
end  
