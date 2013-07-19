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

module Tesla
  class Parser

    def parse(pom, factory)
      @factory = factory
      result = eval(pom)
      # keep no state for the execute blocks
      @factory = nil
      result
    end
    private

    attr_reader :context

    def build(&block)
      build = @current.build ||= Build.new
      nested_block(:build, build, block) if block
    end

    def execute(id, phase, &block)
      @factory.add_execute_task(id.to_s, phase.to_s, block)
    end

    def project(name, url = nil, &block)
      model = Model.new
      model.name = name
      model.url = url if url

      nested_block(:project, model, block)

      model
    end

    def id(*value)
      value = value.join( ':' )
      if @context == :project
        fill_gav(@current, value)
        reduce_id
      else
        @current.id = value
      end
    end

    def site( url, options = {} )
      site = Site.new
      fill_options( site, url, options )
      @current.site = site
    end

    def source_code( url, options = {} )
      scm = Scm.new
      fill_options( scm, url, options )
      @current.scm = scm
    end

    def issue_management( url, system = nil )
      issues = IssueManagement.new
      issues.url = url
      issues.system = system
      @current.issue_management = issues
    end

    def mailing_list( name = nil, &block )
      list = MailingList.new
      list.name = name
      nested_block( :mailing_list, list, block )
      @current.mailing_lists <<  list
    end

    def archives( *archives )
      @current.archive = archives.shift
      @current.other_archives = archives
    end

    def developer( id = nil, &block )
      dev = Developer.new
      dev.id = id
      nested_block( :developer, dev, block )
      @current.developers <<  dev
    end

    def roles( *roles )
      @current.roles = roles
    end

    def property( options )
      prop = ActivationProperty.new
      prop.name = options[ :name ] || options[ 'name' ]
      prop.value = options[ :value ] || options[ 'value' ]
      @current.property = prop
    end

    def file( options )
      file = ActivationFile.new
      file.missing = options[ :missing ] || options[ 'missing' ]
      file.exists = options[ :exists ] || options[ 'exists' ]
      @current.file = file
    end

    def activation( &block )
      activation = Activation.new
      nested_block( :activation, activation, block )
      @current.activation = activation
    end

    def distribution( &block )
      dist = DistributionManagement.new
      nested_block( :distribution, dist, block )
      @current.distribution_management = dist
    end

    def includes( *items )
      @current.includes = items
    end

    def excludes( *items )
      @current.excludes = items
    end

    def test_resource( &block )
      resource = Resource.new
      nested_block( :resource, resource, block )
      @current.test_resources << resource
    end

    def resource( &block )
      resource = Resource.new
      nested_block( :resource, resource, block )
      @current.resources << resource
    end

    def repository( url, options = {}, &block )
      do_repository( :repository=, url, options, block )
    end

    def plugin_repository( url, options = {}, &block )
      do_repository( :plugin, url, options, block )
    end

    def snapshot_repository( url, options = {}, &block )
      do_repository( :snapshot_repository=, url, options, block )
    end

    def releases( config )
      respository_policy( :releases=, config )
    end

    def snapshots( config )
      respository_policy( :snapshots=, config )
    end

    def respository_policy( method, config )
      rp = RepositoryPolicy.new
      case config
      when Hash
        rp.enabled = snapshot[ :enabled ]
        rp.update_policy = snapshot[ :update ]
        rp.checksum_policy = snapshot[ :checksum ]
      when TrueClass
        rp.enabled = true
      when FalseClass
        rp.enabled = false
      else
        rp.enabled = 'true' == config
      end
      @current.send( method, rp )
    end

    def inherit( *value )
      @current.parent = fill_gav( Parent, value.join( ':' ) )
      reduce_id
    end

    def properties(props)
      props.each do |k,v|
        @current.properties[k.to_s] = v.to_s
      end
      @current.properties
    end

    def plugin( *gav, &block )
      if gav.last.is_a? Hash
        options = gav.last
        gav = gav[ 0..-2 ]
      else
        options = {}
      end
      unless gav.first.match( /:/ )
        gav[ 0 ] = "org.apache.maven.plugins:maven-#{gav.first}-plugin"
      end
      gav = gav.join( ':' )
      plugin = fill_gav( @context == :reporting ? ReportPlugin : Plugin,
                         gav)
      set_config( plugin, options )
      if @current.respond_to? :build
        @current.build ||= Build.new
        if @context == :overrides
          @current.build.plugin_management ||= PluginManagement.new
          @current.build.plugin_management.plugins << plugin
        else
          @current.build.plugins << plugin
        end
      else
        @current.plugins << plugin
      end
      nested_block(:plugin, plugin, block) if block
      plugin
    end

    def overrides(&block)
      nested_block(:overrides, @current, block)
    end

    def execute_goals( *goals )
      if goals.last.is_a? Hash
        options = goals.last
        goals = goals[ 0..-2 ]
      else
        options = {}
      end
      exec = PluginExecution.new
      # keep the original default of id
      id = options.delete( :id ) || options.delete( 'id' )
      exec.id = id if id
      if @phase
        if options[ :phase ] || options[ 'phase' ]
          raise 'inside phase block and phase option given'
        end
        exec.phase = @phase
      else
        exec.phase = options.delete( :phase ) || options.delete( 'phase' )
      end
      exec.goals = goals
      set_config( exec, options )
      @current.executions << exec
      # nested_block(:execution, exec, block) if block
      exec
    end

    def dependency( type, *args )
      if args.size > 1 and args.last.is_a? Hash
        options = args.last
        args = args[ 0..-2 ]
      end
      gav = args.join( ':' )
      d = fill_gav(Dependency, gav)
      d.type = type.to_s
      if @context == :overrides
        @current.dependency_management ||= DependencyManagement.new
        @current.dependency_management.dependencies << d
      else
        @current.dependencies << d
      end
      if options || @scope
        options ||= {}
        if @scope
          if options[ :scope ] || options[ 'scope' ]
            raise "scope block and scope option given"
          end
          options[ :scope ] = @scope
        end
        exclusions = options.delete( :exclusions ) ||
          options.delete( "exclusions" )
        case exclusions
        when Array
          exclusions.each do |v|
            d.exclusions << fill_gav( Exclusion, v )
          end
        when String
          d.exclusions << fill_gav( Exclusion, exclusions )
        end
        options.each do |k,v|
          d.send "#{k}=".to_sym, v
        end
      end
      d
    end

    def scope( name )
      @scope = name
      yield
      @scope = nil
    end

    def phase( name )
      @phase = name
      yield
      @phase = nil
    end

    def profile( id, &block )
      profile = Profile.new
      profile.id = id if id
      @current.profiles << profile
      nested_block( :profile, profile, block )
    end

    def report_set( *reports, &block )
      set = ReportSet.new
      case reports.last
      when Hash
        options = reports.last
        reports = reports[ 0..-2 ]
        id = options.delete( :id ) || options.delete( 'id' )
        set.id = id if id
        inherited = options.delete( :inherited ) ||
          options.delete( 'inherited' )
        set.inherited = inherited if inherited
      end
      set_config( set, options )
      set.reports = reports#.to_java
      @current.report_sets << set
    end

    def reporting( &block )
      reporting = Reporting.new
      @current.reporting = reporting
      nested_block( :reporting, reporting, block )
    end

    def gem( *args )
      dependency( :gem, *args )
    end

    def method_missing(method, *args, &block)
      if @context
        m = "#{method}=".to_sym
        if @current.respond_to? m
#p @context
#p m
#p args
          @current.send(m, *args)
          @current
        else
          if args.size > 0 &&
              args[0].is_a?(String) &&
              args[0] =~ /^[${}0-9a-zA-Z._-]+(:[${}0-9a-zA-Z._-]+)+$/
            dependency( method, *args )
          else
            p @context
            p m
            p args
          end
        end
      else
        super
      end
    end

    def xml( xml )
      Xpp3DomBuilder.build( java.io.StringReader.new( xml ) )
    end

    private

    def set_config( receiver, options )
      if options && options.size > 0
        config = Xpp3Dom.new("configuration")
        fill_dom( config, options )
        receiver.configuration = config
      end
    end

    def do_repository( method, url, options = {}, block )
      if @current.respond_to?( method )
        r = DeploymentRepository.new
      else
        r = Repository.new
      end
      # if config = ( options.delete( :snapshot ) ||
      #               options.delete( 'snapshot' ) )
      #   r.snapshot( repository_policy( config ) )
      # end
      # if config = ( options.delete( :release ) ||
      #               options.delete( 'release' ) )
      #   r.snapshot( repository_policy( config ) )
      # end
      nested_block( :repository, r, block ) if block
      fill_options( r, url, options )
      case method
      when :plugin
          @current.plugin_repositories << r
      else
        if @current.respond_to?( method )
          @current.send method, r
        else
          @current.repositories << r
        end
      end
    end

    def fill_options( receiver, url, options )
      options.each do |k,v|
        receiver.send "#{k}=".to_sym, v
      end
      receiver.url = url
    end

    def reduce_id
      if parent = @current.parent
        @current.version = nil if parent.version == @current.version
        @current.group_id = nil if parent.group_id == @current.group_id
      end
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
            child.setValue(v.to_s) if v
          end
        end
        parent.addChild(child) if child
      end
    end
  end
end

Tesla::Parser.new
