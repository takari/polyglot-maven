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
  end
end

Tesla::Parser.new
