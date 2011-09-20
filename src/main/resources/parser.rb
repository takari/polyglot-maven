require 'java'
module Tesla
  class Parser

    def parse(pom)
      @model = org.apache.maven.model.Model.new
      @context = nil
      eval(pom)
    end
    
    def project(name, url = nil, &block)
      raise "wrong context: #{@context}" if @context
      @context = :project
      @current = @model
      @model.name = name
      @model.url = url if url

      block.call
      
      @model
    end

    def id(value)
      if value =~ /:/
        fill_gav(@model, value)
      else
        @model.artifact_id = value
      end
    end
    
    def fill_gav(receiver, gav)
      if gav
        gav = gav.split(':')
        case gav.size
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
    end
    
    def fill_gav_no_version(receiver, gav)
      receiver.group_id, receiver.artifact_id = split_gav(gav) if gav
    end

    def inherit(value)
      @model.parent = org.apache.maven.model.Parent.new
      fill_gav(@model.parent, value)
    end
    
    def properties(props)
      props.each do |k,v|
        @model.properties[k.to_s] = v.to_s
      end
    end

    def add_dom(parent, map = {})
      map.each do |k, v|
        child = org.codehaus.plexus.util.xml.Xpp3Dom.new(k.to_s)
        if(v.is_a? Hash)
          p "TODO hash"
        else
          child.setValue(v.to_s);
        end
        parent.addChild(child);
      end
    end
    
    def plugin(gav, options = {}, &block)
      plugin = org.apache.maven.model.Plugin.new
      fill_gav(plugin, gav)
      if options.size > 0   
        config = org.codehaus.plexus.util.xml.Xpp3Dom.new("configuration")
        add_dom(config, options)
        plugin.configuration = config
      end
      @current.build ||= org.apache.maven.model.Build.new
      if @context == :overrides
        pm = (@current.build.plugin_management ||= org.apache.maven.model.PluginManagement.new)
        pm.plugins << plugin
      else
        @current.build.plugins << plugin
      end
    end
    
    def overrides(&block)
      nested_block(:overrides, @current, block)
    end
    
    def exclusions(values)
      values.each do |v|
        e = org.apache.maven.model.Exclusion.new
        fill_gav(e, v)
        @current.exclusions << e
      end
    end
    
    def dependency(type, gav, &block)
      d = org.apache.maven.model.Dependency.new
      fill_gav(d, gav)
      d.type = type.to_s
      if @context == :overrides
      	dm = (@current.dependency_management ||= org.apache.maven.model.DependencyManagement.new)
        dm.dependencies << d
      else
        @current.dependencies << d
      end
      nested_block(:dependency, d, block) if block
    end
    
    def nested_block(context, receiver, block)
      old_ctx = @context
      @context = context
      old = @current
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
