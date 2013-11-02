/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.ruby;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.TreeSet;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.ArtifactResolutionRequest;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugin.LegacySupport;
import org.apache.maven.repository.RepositorySystem;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

@Component(role = SetupClassRealm.class)
public class SetupClassRealm {

    @Requirement
    RepositorySystem system;

    @Requirement
    protected LegacySupport legacySupport;
    
    public void setupArtifact( String gav, ClassRealm realm )
            throws MalformedURLException
    {
        String[] parts = gav.split( ":" );
        Artifact root = system.createArtifact( parts[ 0 ], parts[ 1 ], parts[ 2 ], "jar" );
        ArtifactResolutionResult result;
        result = system.resolve( new ArtifactResolutionRequest()
            .setArtifact( root )
            .setCollectionFilter( new ArtifactFilter() {
                
                public boolean include( Artifact artifact )
                {
                    return !"tesla-polyglot-common".equals( artifact.getArtifactId() ) &&
                           !"test".equals( artifact.getScope() );
                }
            } )
            .setResolveRoot( true )
            .setOffline( legacySupport.getSession().getRequest().isOffline() )
            .setMirrors( legacySupport.getSession().getRequest().getMirrors() )
            .setProxies( legacySupport.getSession().getRequest().getProxies() )
            .setServers( legacySupport.getSession().getRequest().getServers())
            .setLocalRepository( legacySupport.getSession().getRequest().getLocalRepository() )
            .setRemoteRepositories( legacySupport.getSession().getRequest().getRemoteRepositories() )
            .setResolveTransitively( true ) );

        // get searchable list of existing urls
        Set<String> urls = new TreeSet<String>();
        for( URL url : realm.getURLs() ){
            urls.add( url.toString() );
        }

        for( Artifact a: result.getArtifacts() ){
            if ( "jar".equals( a.getType() ) && a.getFile() != null ){
                URL url = a.getFile().toURI().toURL();
                // add only if not already exist
                if ( !urls.contains( url.toString() ) ){
                    realm.addURL( a.getFile().toURI().toURL() );
                }
            }
        }        
    }
}