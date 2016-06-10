require 'maven/tools/dsl'
require 'java'

%w(
org.apache.maven.model.Activation
org.apache.maven.model.ActivationFile
org.apache.maven.model.ActivationOS
org.apache.maven.model.ActivationProperty
org.apache.maven.model.Build
org.apache.maven.model.Contributor
org.apache.maven.model.CiManagement
org.apache.maven.model.Dependency
org.apache.maven.model.DependencyManagement
org.apache.maven.model.DeploymentRepository
org.apache.maven.model.DistributionManagement
org.apache.maven.model.Developer
org.apache.maven.model.Exclusion
org.apache.maven.model.Extension
org.apache.maven.model.IssueManagement
org.apache.maven.model.License
org.apache.maven.model.MailingList
org.apache.maven.model.Model
org.apache.maven.model.Notifier
org.apache.maven.model.Organization
org.apache.maven.model.Parent
org.apache.maven.model.Plugin
org.apache.maven.model.PluginExecution
org.apache.maven.model.PluginManagement
org.apache.maven.model.Prerequisites
org.apache.maven.model.Profile
org.apache.maven.model.Relocation
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

# ruby internal path uses / as separator on ALL platforms
# the to_pathname converts all platform dependent path to
# a ruby path
class ::Java::JavaIo::File
  def to_pathname
    to_s.gsub( /\\/, '/' )
  end
end
class ::Java::JavaLang::String
  def to_pathname
    to_s.gsub( /\\/, '/' )
  end
end
class ::String
  def to_pathname
    self.gsub( /\\/, '/' )
  end
end

module Maven
  module Polyglot
    class Parser

      def parse( pom, factory, src )
        @factory = factory

        case src
        when /[.]gemspec$/
          eval_pom( "tesla do\ngemspec '#{File.basename( src )}' \nend", src )
        when /Jarfile$/
          eval_pom( "tesla do\njarfile\nend", src )
        when /Gemfile$/
          eval_pom( "tesla do\ngemfile\nend", src )
        else
          eval_pom( "tesla do\n#{pom}\nend", src || '.' )
        end

      ensure
        # keep no state for the execute blocks
        @factory = nil
      end

      private

      include Maven::Tools::DSL

      # override hook from DSL
      def add_execute_task( options, &block )
        options[ :phase ] = retrieve_phase( options )
        profile_id = @context == :profile ? @current.id : nil
        @factory.add_execute_task( options[ :id ].to_s,
                                   options[ :phase ].to_s,
                                   profile_id,
                                   block )
      end

      def fill_options( receiver, options )
        options.each do |k,v|
          if v.is_a? Hash
            props = java.util.Properties.new
            v.each { |kk,vv| props[ kk.to_s ] = vv.to_s }
            receiver.send( "#{k}=".to_sym, props )
          else
            fill( receiver, k, v )
          end
        end
      end

      def configuration( v )
        if @context == :notifier
          props = java.util.Properties.new
          v.each { |kk,vv| props[ kk.to_s ] = vv.to_s }
          @current.configuration = props
        else
          set_config( @current, v )
        end
      end

      class PropertiesWrapper
        def initialize( prop )
          @prop = prop
        end

        def []=( key, val )
          @prop[ key.to_s ] = val.to_s
        end

        def method_missing( method, *args )
          @prop.send( method, *args )
        end
      end
    
      def properties(props = {})
        props.each do |k,v|
          @current.properties[k.to_s] = v.to_s
        end
        PropertiesWrapper.new @current.properties
      end
    
      def xml( xml )
        Xpp3DomBuilder.build( java.io.StringReader.new( xml ) )
      end

      def set_config( receiver, options )
        prepare_config( receiver, options )
        if options && options.size > 0
          config = Xpp3Dom.new("configuration")
          fill_dom( config, options )
          receiver.configuration = config
        end
      end

      def to_hash_key(key)
        case key
        when Symbol
          parts = key.to_s.split('_')
          parts[0] + parts[1..-1].collect(&:capitalize).join
        else
          key.to_s
        end
      end

      def fill_dom(parent, map = {})
        # sort attributes to stay consistent
        map.keys.select { |k| k.to_s =~ /^@/ }.sort{ |m,n| m.to_s <=> n.to_s }.each do |k|
          parent.setAttribute( to_hash_key(k)[ 1..-1 ], map[ k ] )
        end
        map.each do |k, v|
          case v
          when Hash
            child = Xpp3Dom.new(to_hash_key(k))
            fill_dom(child, v)
          when Array
            if k.to_s.match( /s$/ )
              node = Xpp3Dom.new(to_hash_key(k))
              name = k.to_s.sub( /s$/, '' )
            else
              node = parent
              name = to_hash_key(k)
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
                child.setValue( val.to_s )
                node.addChild( child )
              end
            end
            parent.addChild( node ) if node != parent
            child = nil
          else
            unless k.to_s =~ /^@/
              child = Xpp3Dom.new(to_hash_key(k))
              child.setValue(v.to_s) unless v.nil?
            end
          end
          parent.addChild(child) if child
        end
      end
    end
  end
end
Maven::Polyglot::Parser.new
