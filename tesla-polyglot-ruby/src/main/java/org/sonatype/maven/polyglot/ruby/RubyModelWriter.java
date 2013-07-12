/*
 * Copyright (C) 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
// */

package org.sonatype.maven.polyglot.ruby;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.model.BuildBase;
import org.apache.maven.model.ConfigurationContainer;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.Profile;
import org.apache.maven.model.ReportPlugin;
import org.apache.maven.model.ReportSet;
import org.apache.maven.model.Reporting;
import org.apache.maven.model.Repository;
import org.apache.maven.model.RepositoryPolicy;
import org.apache.maven.model.Scm;
import org.apache.maven.model.Site;
import org.apache.maven.model.io.ModelWriter;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.sonatype.maven.polyglot.io.ModelWriterSupport;

@Component(role = ModelWriter.class, hint = "ruby")
public class RubyModelWriter extends ModelWriterSupport {

    @Requirement
    protected Logger log;

    public void write( final Writer output, final Map<String, Object> options,
            final Model model ) throws IOException {
        assert output != null;
        assert model != null;

        ModelPrinter p = new ModelPrinter( output );
        p.print( model );
    }

    static class ModelPrinter {
        private static final String INDENT = "  ";
        private final RubyPrintWriter p;

        ModelPrinter( Writer output ) {
            this.p = new RubyPrintWriter(output);
        }

        void print( Model model ) {

            project( model );

            p.flush();
            p.close();
        }

        void repositories(Repository... repositories) {
            printRepositories("repository", repositories);
            if (repositories.length > 0) {
                p.println();
            }
        }

        void pluginRepositories(Repository... repositories) {
            printRepositories("plugin_repository", repositories);
            if (repositories.length > 0) {
                p.println();
            }
        }

        void distribution( DistributionManagement distribution ){  
        	if ( distribution != null ){
	        	p.printStartBlock( "distribution" );
	        	if ( distribution.getRepository() != null ){
	        		printRepositories("repository", distribution.getRepository() );
	        	}
	        	if ( distribution.getSnapshotRepository() != null ){
	        		printRepositories("snapshot_repository", distribution.getSnapshotRepository() );
	        	}
	        	if ( distribution.getSite() != null ){
	        		Site site = distribution.getSite();
	        		printWithOptions( "site", 
	        					  	  options( "id", site.getId(),
	        					  			   "name", site.getName() ),
	        					      site.getUrl() );
	        	}
	        	p.printEndBlock();
	        	p.println();
        	}
        }
        
        private Map<String, Object> options(Object... args) {
        	Map<String, Object> options = new LinkedHashMap<String, Object>();
        	String key = null;
        	for( Object arg : args ){
        		if( key == null ){
        			key = arg.toString();
        			continue;
        		}
        		else {
        			if (arg != null ){
        				options.put( key, arg );
        			}
        			key = null;
        		}
        	}
			return options;
		}

		void sourceCode( Scm scm ){
//        	Map<String,String> options = new LinkedHashMap<String, String>();
//        	options.put( "connection", scm.getConnection() );
//        	options.put( "developer_connection", scm.getDeveloperConnection() );
//        	if ( !scm.getTag().equals( "HEAD" ) ){
//        		options.put( "tag", scm.getTag() );
//        	}
        	printWithOptions( "source_code", 
        			          options( "connection", scm.getConnection(), 
           			        		   "developer_connection", scm.getDeveloperConnection(),
        			        		   "tag", scm.getTag().equals( "HEAD" ) ? null : scm.getTag() ),
        			          scm.getUrl() );
        	p.println();
        }
        
        private void printRepositories(String name,
                Repository... repositories) {
            for (Repository r: repositories) {
                if ( r.getReleases() != null ){
                	printRepositoryPolicy( r.getReleases() );
                }
                if ( r.getSnapshots() != null ){
                	printRepositoryPolicy( r.getSnapshots() );
                }
                printWithOptions( name, 
                		          options( "id", r.getId(),
                		        		   "name", r.getName() ), 
                                  r.getUrl() );
            }
        }

        private void printWithOptions( String prefix, Map<String, Object> options, String... args){
            printWithOptions( prefix, options, null, args);
        }
        
        private void printWithOptions( String prefix, Map<String, Object> options, Object config, String... args){
            if (!options.isEmpty() || config != null ){
            	prefix += "(";
                p.print( prefix, args );
            	String indent = prefix.replaceAll( ".", " " ) + ' ';
            	for( Map.Entry<String, Object> item: options.entrySet() ){
            		p.append(",").println();
            		p.print( indent );
            		p.append( ':' ).append( item.getKey() ).append( " => " );
            		if ( item.getValue() instanceof String ) {
            			p.append( "'" ).append( item.getValue().toString() ).append( "'" );
            		}
            		else {
            			p.append( item.getValue().toString() );
            		}
            	}
            	if ( config != null ){
            		printConfiguration( indent, config );
            		p.println();
            	}
            	else {
            		p.append( " )" ).println();
            	}
            }
            else {
                p.println( prefix, args );
            }
        }
        
        private void printRepositoryPolicy( RepositoryPolicy policy ){
        	//p.println( "TODO: " + policy );
        }
        
        void project(Model model) {
            String name = model.getName();
            if (name == null) {
                name = model.getArtifactId();
            }
            p.printStartBlock("project", name, model.getUrl());
            p.println();
            
            id(model);
            parent(model.getParent());
            
            p.println("packaging", model.getPackaging());
            p.println();
            
            description(model.getDescription());

            repositories( toRepositoryArray( model.getRepositories() ) );
            
            pluginRepositories( toRepositoryArray( model.getPluginRepositories() ) );

            sourceCode( model.getScm() );
            
            distribution( model.getDistributionManagement() );
            
            properties(model.getProperties());
            
            dependencies(model.getDependencies());
            
            modules(model.getModules());
            
            managements(model.getDependencyManagement(), model.getBuild());
            
            build(model.getBuild());
            
            profiles( model.getProfiles() );
            
            reporting( model.getReporting() );
            
            p.printEndBlock();
        }

        void reporting(Reporting reporting) {
        	if ( reporting != null ){
        		p.printStartBlock( "reporting" );
        		plugins( reporting.getPlugins() );
        		p.printEndBlock();
        		p.println();
        	}
		}

		void profiles( List<Profile> profiles ) {
    		if ( profiles != null ){
    			for( Profile profile: profiles ){
    				p.print( "profile" );
    				if (profile.getId() != null ){
    					p.append( " '" ).append( profile.getId() ).append( "'" );
    					p.printStartBlock();
    					p.println();
    					
    		            repositories( toRepositoryArray( profile.getRepositories() ) );
    		            
    		            pluginRepositories( toRepositoryArray( profile.getPluginRepositories() ) );
    		            
    		            distribution( profile.getDistributionManagement() );
    		            
    		            properties(profile.getProperties());
    		            
    		            dependencies(profile.getDependencies());
    		            
    		            modules(profile.getModules());
    		            
    		            managements(profile.getDependencyManagement(), profile.getBuild());
    		            
    		            build(profile.getBuild());

    					p.printEndBlock();
    					p.println();
    				}
    			}
    		}
    	}
        
        void description(String description) {
            if (description != null) {
                p.println("description", description);
                p.println();
            }
        }

        void build( BuildBase build ) {
            if ( build != null ) {
                plugins( build.getPlugins() );
                
                if ( build.getDefaultGoal() != null ){
                	p.printStartBlock( "build" );
                	p.println( "default_goal", build.getDefaultGoal() );
                	p.printEndBlock();
                	p.println();
                }
            }
        }

        void managements(DependencyManagement dependencyManagement, BuildBase build) {
            if ((dependencyManagement != null && !dependencyManagement
                    .getDependencies().isEmpty())
                    || (build != null && build.getPluginManagement() != null && !build
                            .getPluginManagement().getPlugins().isEmpty())) {
                p.printStartBlock("overrides");
                if ( dependencyManagement != null ){
                	dependencies(dependencyManagement.getDependencies());
                }
                if (build != null && build.getPluginManagement() != null) {
                    plugins(build.getPluginManagement().getPlugins());
                }
                p.printEndBlock();
                p.println();
            }
        }

        void id(Model model) {
            String groupId = model.getGroupId();
            if (groupId == null & model.getParent() != null) {
                groupId = model.getParent().getGroupId();
            }

            String version = model.getVersion();
            if (version == null && model.getParent() != null) {
                version = model.getParent().getVersion();
            }
            p.println("id", groupId + ":" + model.getArtifactId() + ":"
                    + version);
        }

        void parent(Parent parent) {
            if (parent != null) {
                p.print("inherit", parent.getGroupId() + ":"
                        + parent.getArtifactId() + ":" + parent.getVersion());
                if (parent.getRelativePath() != null ) {
                	if ( parent.getRelativePath().equals( "../pom.xml" ) ) {
                		//p.append( ", '" ).append( "../pom.rb" ).append( "'" );
                		p.println();
                	}
                    else {
                        p.append(", '" + parent.getRelativePath() + "'").println();
                    }	
                } 
                else {
                    p.println();
                }
            }
        }

        void properties(Properties properties) {
            if (!properties.isEmpty()) {
                List<Object> keys = new ArrayList<Object>(properties.keySet());
                String prefix = "properties( ";
                String indent = prefix.replaceAll( ".", " " );
                p.print( prefix );
                for (int i = 0; i < keys.size(); i++) {
                    Object key = keys.get(i);
                    if (i != 0) {
                        p.print( indent );
                    }
                    Object value = properties.get( key );
                    if (value != null) {
                        if (value instanceof String) {
                            p.append( "'" + key + "' => '" + value + "'" );
                        } else {
                            p.append( "'" + key + "' => " + value );
                        }
                        if (i + 1 != keys.size()) {
                            p.append( "," );
                        }
                        else {
                        	p.append( " )" );
                        }
                        p.println();
                    }
                }
                p.println();
            }
        }

        void modules(List<String> modules) {
            if (!modules.isEmpty()) {
                p.print("modules [ ");
                for (int i = 0; i < modules.size(); i++) {
                    String module = modules.get(i);
                    if (i != 0) {
                        p.append("            ");
                    }
                    p.append("'").append(module).append("'");
                    if (i + 1 != modules.size()) {
                        p.append(",").println();
                    }
                }
                p.append(" ]").println();
                p.println();
            }
        }

        void dependencies( List<Dependency> deps ) {
            for (int i = 0; i < deps.size(); i++) {
                Dependency d = deps.get(i);
                Map<String, Object> options = new HashMap<String, Object>();
                String gav;
                if (d.getVersion() != null) {
                    gav = d.getGroupId() + ":" + d.getArtifactId() + ":" + d.getVersion();
                } else {
                    //
                    // We are assuming the model is well-formed and that the
                    // parent or dependencyManagement
                    // is providing a version for this particular dependency.
                    //
                	gav = d.getGroupId() + ":" + d.getArtifactId();
                }
                if (d.getClassifier() != null) {
                	if (d.getVersion() == null ){
                		options.put( "classifier", d.getClassifier() );
                	}
                	else {
                		gav += ":" + d.getClassifier();
                	}
                }
                if ( d.getScope() != null ){
                	options.put( "scope", d.getScope() );
                }
                if ( d.getOptional() != null ){
                	options.put( "optional", d.isOptional() );
                }
                if ( d.getExclusions().size() == 1 ) {
                	Exclusion e = d.getExclusions().get( 0 );
                	String ga = e.getGroupId() + ":" + e.getArtifactId();
                	options.put( "exclusions",  ga );
                }
                else if ( d.getExclusions().size() > 1 ) {
                	List<String> exclusions = new ArrayList<String>( d.getExclusions().size() );
                    for (Exclusion e : d.getExclusions()) {
                    	String ga = e.getGroupId() + ":" + e.getArtifactId();
                    	exclusions.add( ga );
                    }
                	options.put( "exclusions",  exclusions );
                }
                final String prefix = options.size() == 0 ? "jar " : "jar( ";
                final String indent = prefix.replaceAll( ".", " " );
                
                p.print( prefix );
                p.append( "'" ).append( gav ).append( "'" );
                if ( options.size() > 0 ) {
                	for( Map.Entry<String, Object> item : options.entrySet() ){
                		p.append( "," );
                		p.println();
                		p.print( indent );
                		p.append( ":" );
                		p.append( item.getKey() );
                		p.append( " => ");
                		if ( item.getValue() instanceof String ){
                			p.append( "'" );
                			p.append( item.getValue().toString() );
                			p.append( "'" );
                		}
                		else {
                			@SuppressWarnings("unchecked")
							List<String> list = (List<String>) item.getValue();
                            boolean first = true;
                    		p.append( "[ " );
                            for( String ex: list ){
                        		if ( first ) {
                        			first = false;
                        		}
                        		else {
                        			p.append( "," );
                        			p.println();
                            		p.print( indent );
                            		p.append( "                 " );
                        		}
                        		p.append( "'" );
                            	p.append( ex );
                        		p.append( "'" );                            	
                            }
                    		p.append( " ]" );
                		}
                	}
                	p.append( " )");
                }
                p.println();
            }
            if (!deps.isEmpty()) {
                p.println();
            }
        }
            
        <T extends ConfigurationContainer> void plugins(List<T> plugins) {
            for (int i = 0; i < plugins.size(); i++) {
                T container = plugins.get(i); 
                String prefix = container.getConfiguration() == null ? "plugin " : "plugin( ";
                String indent = prefix.replaceAll( ".", " " );
                p.print( prefix);
                Plugin plugin = null;
                ReportPlugin rplugin = null;
                if ( container instanceof Plugin ){
                	plugin = (Plugin) container;
                	pluginProlog( plugin.getGroupId(), plugin.getArtifactId(), plugin.getVersion() );
                }
                else {
                	rplugin = (ReportPlugin) container;
                	pluginProlog( rplugin.getGroupId(), rplugin.getArtifactId(), rplugin.getVersion() );
                }
                printConfiguration( indent, container.getConfiguration() );
                if ( plugin != null && !plugin.getExecutions().isEmpty()){
                    p.printStartBlock();
                    for(PluginExecution exec : plugin.getExecutions()){
                		printWithOptions( "execute_goals",
      				          options( "id", "default".equals( exec.getId() ) ? null : exec.getId(),
      				        		   "inherited", exec.isInherited() ? null : "false" ,
      				        		   "phase", exec.getPhase() ),
      				          exec.getConfiguration(),
      				          toArray( exec.getGoals() ) );
//                		
//                    	boolean hasOptions = !exec.getId().equals( "default" ) || 
//                    				exec.getPhase() != null;
//                        String prefix2 = hasOptions ? "execute_goals( " : "execute_goals ";
//                        p.print( prefix2 );
//                        boolean first = true;
//                        for (String goal : exec.getGoals()) {
//                            if (first) {
//                                first = false;
//                            } else {
//                                p.append( ", " );
//                            }
//                            p.append( "'" ).append( goal ).append( "'" );
//                        }
//                        if ( hasOptions ){
//                            String indent2 = prefix2.replaceAll( ".", " " );
//                        	if( !exec.getId().equals( "default" ) ){
//                            	p.append( "," );
//                        		p.println();
//                        		p.print( indent2 );
//                        		p.append( ":id => '" ).append( exec.getId() ).append( "'" );
//                            }
//                            if( exec.getPhase() != null ){
//                            	p.append( "," );
//                        		p.println();
//                        		p.print( indent2 );
//                        		p.append( ":phase => '" ).append( exec.getPhase() ).append( "'" );
//                            }
//                        	p.append( " )" );
//                        }
//                        p.println();
                    }
                    p.printEndBlock();
                }
                if ( rplugin != null && !rplugin.getReportSets().isEmpty() ) {
                	p.printStartBlock();
                	for( ReportSet set : rplugin.getReportSets() ){
                		printWithOptions( "report_set",
                				          options( "id", "default".equals( set.getId() ) ? null : set.getId(),
                				        		   "inherited", set.isInherited() ? null : "false" ),
                				          set.getConfiguration(),
                				          toArray( set.getReports() ) );
                	}
                	p.printEndBlock();
                }
                p.println();
            }
        }

		private String[] toArray( List<String> list ) {
			return list.toArray( new String[ list.size() ] );
		}

		private Repository[] toRepositoryArray( List<Repository> list ) {
			return list.toArray( new Repository[ list.size() ] );
		}

		private void printConfiguration( String indent,  Object config ) {
			if (config != null ){
				Xpp3Dom configuration = (Xpp3Dom) config;
				if (configuration.getChildCount() != 0) {
				    p.append(",");
				    p.println();
				    printHashConfig( indent, configuration );
				}
				p.append( " )");
			}
		}
		
		private void pluginProlog( String groupId, String artifactId, String version ) {
			if( "org.apache.maven.plugins".equals( groupId ) && artifactId.startsWith( "maven-") ) {
				String name = artifactId.replaceAll( "maven-|-plugin", ""); 
				if ( name.contains( "-" ) ){
			    	p.append( ":'" ).append( name ).append( "'" );                		
				}
				else {
			    	p.append( ":" ).append( name );                		                		
				}
			    if ( version != null ){
			    	p.append( ", '").append( version ).append( "'" );
			    }                	
			}
			else {
				p.append( "'").append( groupId ).append( ":" ).append( artifactId );
			    if ( version != null ){
			    	p.append( ":" ).append( version );
			    }
			    p.append( "'" );
			}
		}
        
        private void printListConfig(String indent, Xpp3Dom base) {
            int count = base.getChildCount();
            for (int j = 0; j < count; ) {
                Xpp3Dom c = base.getChild(j);
                if (c.getValue() != null ){
                	p.append( " '" ).append( c.getValue() ).append( "'" );
                	if( ++j < count ){
                		p.append(",");
                		p.println();
                		p.print( indent );
                		p.append( "  ");
                	}
                }
                else {
                	p.append( " { ");
                	printHashConfig( "   " + indent + INDENT, c, true );
                	p.append( " }");
                	if( ++j < count ){
                		p.append(",");
                		p.println();
                		p.print( indent );
                		p.append( "  ");
                	}
                }
            }
        }

        private void printHashConfig(String indent, Xpp3Dom base) {
        	printHashConfig( indent, base, false );
        }
        
        private void printHashConfig(String indent, Xpp3Dom base, boolean skipFirst ) {
            int count = base.getChildCount();
            for (int j = 0; j < count; j++) {
                Xpp3Dom c = base.getChild(j);
                if ( j != 0 || !skipFirst ) {
                	p.print(indent);
                }
                if (c.getChildCount() > 0) {
                    if ( ( c.getChildCount() > 1 && c.getChild(0).getName().equals( c.getChild(1).getName() ) ) 
                    	|| ( c.getChildCount() == 1 &&  c.getName().equals( c.getChild(0).getName() + "s" ) ) ) {
                        p.append( ":" ).append( c.getName() ).append( " => [" );
                        printListConfig(indent + "    " + c.getName().replaceAll( ".", " " ), c);
                        p.append( " ]");
                    }
                    else {
                      p.append( ":" ).append( c.getName() ).append( " => {").println();
                      printHashConfig(indent + INDENT, c);
                      p.println();
                      p.print( indent );
                      p.append( "}");
                    }
                } else if (c.getValue() == null) {
                    p.append(":" + c.getName() + " => true");
                } else {
                    p.append(":" + c.getName() + " => '"
                            + c.getValue() + "'");
                }
                if (j + 1 != count) {
                    p.append(",").println();
                }
            }
        }
    }
}
