require 'java'

%w(
org.apache.maven.model.Build
org.apache.maven.model.Dependency
org.apache.maven.model.DependencyManagement
org.apache.maven.model.Exclusion
org.apache.maven.model.Model
org.apache.maven.model.Parent
org.apache.maven.model.Plugin
org.apache.maven.model.PluginExecution
org.apache.maven.model.PluginManagement
org.codehaus.plexus.util.xml.Xpp3Dom
).each {|i| java_import i }

module Tesla
  class Parser

    def parse(pom)
      eval(pom)
    end
    
    private

    def project(name, url = nil, &block)
      @model = Model.new
      @model.name = name
      @model.url = url if url

      nested_block(:project, @model, block)
      
      @model
    end

    def id(value)
      if @context == :project
	      fill_gav(@model, value)
	  else
	     @current.id = value
	  end
    end
    
    def fill_gav(receiver, gav)
      if gav
        if receiver.is_a? Class
          receiver = receiver.new
        end
        gav = gav.split(':')
        case gav.size
        when 1
          receiver.artifact_id = gav[0]
        when 2
          receiver.group_id, receiver.artifact_id = gav
        when 3
          receiver.group_id, receiver.artifact_id, receiver.version = gav
        when 4
          receiver.group_id, receiver.artifact_id, receiver.version, receiver.classifier = gav
        else
          raise "can not assign such an array #{gav.inspect}"
        end
      end
      receiver
    end

    def inherit(value)
      @model.parent = fill_gav(Parent, value)
    end
    
    def properties(props)
      props.each do |k,v|
        @current.properties[k.to_s] = v.to_s
      end
      @current.properties
    end

    def fill_dom(parent, map = {})
      map.each do |k, v|
        case v
        when Hash
          child = Xpp3Dom.new(k.to_s)
          fill_dom(child, v)
        when Array
          v.each do |val|
            child = Xpp3Dom.new(k.to_s)
            child.setValue(val)
            parent.addChild(child)
          end
          child = nil
        else        
          child = Xpp3Dom.new(k.to_s)
          child.setValue(v.to_s)
        end
        parent.addChild(child) if child
      end
    end
    
    def plugin(gav, options = {}, &block)
      plugin = fill_gav(Plugin, gav)
      if options.size > 0   
        config = Xpp3Dom.new("configuration")
        fill_dom(config, options)
        plugin.configuration = config
      end
      @current.build ||= Build.new
      if @context == :overrides
        @current.build.plugin_management ||= PluginManagement.new
        @current.build.plugin_management.plugins << plugin
      else
        @current.build.plugins << plugin
      end
      nested_block(:plugin, plugin, block) if block
      plugin
    end
    
    def overrides(&block)
      nested_block(:overrides, @current, block)
    end
    
    def execution(id =nil, phase = nil, &block)
      exec = PluginExecution.new
      exec.id = id if id
      exec.phase = phase if phase
      @current.executions << exec
      nested_block(:execution, exec, block) if block
      exec
    end
    
    def exclusions(values)
      values.each do |v|
        @current.exclusions << fill_gav(Exclusion, v)
      end
      @current
    end
    
    def dependency(type, gav, &block)
      d = fill_gav(Dependency, gav)
      d.type = type.to_s
      if @context == :overrides
      	@current.dependency_management ||= DependencyManagement.new
        @current.dependency_management.dependencies << d
      else
        @current.dependencies << d
      end
      nested_block(:dependency, d, block) if block
      d
    end
    
    def nested_block(context, receiver, block)
      old_ctx = @context
      old = @current

      @context = context
      @current = receiver

      block.call

      @current = old
      @context = old_ctx
    end
    
    def method_missing(method, *args, &block)
      if @context
        m = "#{method}=".to_sym
        if @current.respond_to? m
          @current.send(m, *args)
          @current
        else
          if args.size == 1 && args[0].is_a?(String) && args[0] =~ /\w+:\w+/
            dependency(method, args[0], &block)
          else
            p @context
            p m
          end
        end
      else
        super
      end
    end
  end
end

Tesla::Parser.new
