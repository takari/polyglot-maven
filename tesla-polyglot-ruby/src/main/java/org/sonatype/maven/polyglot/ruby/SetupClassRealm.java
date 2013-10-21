/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.ruby;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.ArtifactResolutionRequest;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.plugin.LegacySupport;
import org.apache.maven.repository.RepositorySystem;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

@Component(role = SetupClassRealm.class)
public class SetupClassRealm {

    private static final String JRUBY_HOME = "jruby.home";

    @Requirement
    RepositorySystem system;

    @Requirement
    private LegacySupport legacySupport;
    
    protected void setupClassRealm() throws MalformedURLException
    {        
        try
        {
            // test if jruby classes are there
            Thread.currentThread().getContextClassLoader().loadClass( "org.jruby.embed.ScriptingContainer" );
        }
        catch (ClassNotFoundException e)
        {
            // see if we have a jruby-home set somewhere
            String jrubyHome = System.getenv( "JRUBY_HOME" );
            if ( jrubyHome == null ){
                jrubyHome = System.getProperty( JRUBY_HOME );
            }
            if ( jrubyHome == null ){
                jrubyHome = legacySupport.getSession().getRequest().getUserProperties().getProperty( JRUBY_HOME );
            }
            if ( jrubyHome == null ){
                jrubyHome = legacySupport.getSession().getRequest().getSystemProperties().getProperty( JRUBY_HOME );
            }
            if ( jrubyHome == null ){
                jrubyHome = legacySupport.getSession().getCurrentProject().getProperties().getProperty( JRUBY_HOME );
            }

            ClassRealm realm = (ClassRealm) Thread.currentThread().getContextClassLoader();
            if (jrubyHome != null ){
                setupFromJrubyHome( realm, jrubyHome );
            }
            else {
                
                // use jruby from an artifact
                setupFromArtifact( realm );                
            }
        }
    }


    protected void setupFromArtifact( ClassRealm realm )
            throws MalformedURLException
    {
        Artifact jruby = system.createArtifact( "org.jruby", "jruby", "1.7.5", "jar" );
        ArtifactResolutionResult result;
        result = system.resolve( new ArtifactResolutionRequest()
            .setArtifact( jruby )
            .setResolveRoot( true )
            .setOffline( legacySupport.getSession().getRequest().isOffline() )
            .setMirrors( legacySupport.getSession().getRequest().getMirrors() )
            .setProxies( legacySupport.getSession().getRequest().getProxies() )
            .setServers( legacySupport.getSession().getRequest().getServers())
            .setLocalRepository( legacySupport.getSession().getRequest().getLocalRepository() )
            .setRemoteRepositories( legacySupport.getSession().getRequest().getRemoteRepositories() )
            .setResolveTransitively( true ) );
        
        for( Artifact a: result.getArtifacts() ){
            if ( "jar".equals( a.getType() ) ){
                realm.addURL( a.getFile().toURI().toURL() );
            }
        }        
    }


    protected void setupFromJrubyHome( ClassRealm realm, String jrubyHome )
            throws MalformedURLException
    {
        System.setProperty( JRUBY_HOME, jrubyHome );
        File[] jars = new File( jrubyHome, "lib" ).listFiles( new FileFilter() {

            public boolean accept( File pathname )
            {
                return pathname.getName().endsWith( ".jar" );
            }
        } );
        for( File jar : jars ){
            realm.addURL( jar.toURI().toURL() );
        }
    }
    }