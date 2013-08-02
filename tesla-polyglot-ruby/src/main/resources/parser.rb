require 'maven/tools/dsl'
require 'java'

%w(
org.apache.maven.model.Activation
org.apache.maven.model.ActivationFile
org.apache.maven.model.ActivationOS
org.apache.maven.model.ActivationProperty
org.apache.maven.model.Build
org.apache.maven.model.Dependency
org.apache.maven.model.DependencyManagement
org.apache.maven.model.DeploymentRepository
org.apache.maven.model.DistributionManagement
org.apache.maven.model.Developer
org.apache.maven.model.Exclusion
org.apache.maven.model.Extension
org.apache.maven.model.IssueManagement
org.apache.maven.model.MailingList
org.apache.maven.model.Model
org.apache.maven.model.Parent
org.apache.maven.model.Plugin
org.apache.maven.model.PluginExecution
org.apache.maven.model.PluginManagement
org.apache.maven.model.Profile
org.apache.maven.model.Reporting
org.apache.maven.model.ReportPlugin
org.apache.maven.model.ReportSet
org.apache.maven.model.Repository
org.apache.maven.model.RepositoryPolicy
org.apache.maven.model.Resource
org.apache.maven.model.Scm
org.apache.maven.model.Site
org.codehaus.plexus.util.xml.Xpp3Dom
org.codehaus.plexus.util.xml.Xpp3DomBuilder
).each {|i| java_import i }

Execution=PluginExecution unless defined? Execution

module Tesla
  class Parser

    def parse( pom, factory, src )
      @factory = factory

      eval_pom( "tesla do\n#{pom}\nend", src )

    ensure
      # keep no state for the execute blocks
      @factory = nil      
    end

    private

    def execute( id, phase, &block )
      @factory.add_execute_task( id.to_s, phase.to_s, block )
    end

    include Maven::Tools::DSL

    def xml( xml )
      Xpp3DomBuilder.build( java.io.StringReader.new( xml ) )
    end

    def set_config( receiver, options )
      if options && options.size > 0
        config = Xpp3Dom.new("configuration")
        fill_dom( config, options )
        receiver.configuration = config
      end
    end

    def fill_dom(parent, map = {})
      map.each do |k, v|
        case v
        when Hash
          child = Xpp3Dom.new(k.to_s)
          fill_dom(child, v)
        when Array
          if k.to_s.match( /s$/ )
            node = Xpp3Dom.new( k.to_s )
            name = k.to_s.sub( /s$/, '' )
          else
            node = parent
            name = k.to_s
          end
          v.each do |val|
            child = Xpp3Dom.new( name )
            case val
            when Hash
              fill_dom( child, val )
              node.addChild( child )
            when Xpp3Dom
              node.addChild( val )
            else
              child.setValue( val )
              node.addChild( child )
            end
          end
          parent.addChild( node ) if node != parent
          child = nil
        else
          case k.to_s
          when /^@/
            parent.setAttribute( k.to_s[ 1..-1 ], v.to_s )
          else
            child = Xpp3Dom.new(k.to_s)
            child.setValue(v.to_s) unless v.nil?
          end
        end
        parent.addChild(child) if child
      end
    end
  end
end

Tesla::Parser.new
