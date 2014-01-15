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

import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.component.annotations.Component;

@Component(role = SetupClassRealm.class, hint = "ruby")
public class SetupClassRealmRuby extends SetupClassRealm {

    private static final String JRUBY_HOME = "jruby.home";

    @Override
    public void setupArtifact( String gav, ClassRealm realm) throws MalformedURLException
    {        
        try
        {
            // test if class is already there
            realm.loadClass( "org.jruby.embed.ScriptingContainer" );
        }
        catch (ClassNotFoundException e)
        {
               
            // add the provided jars for the given artifact
            setup( gav, realm );
        }
    }
    
    private void setup( String gav, ClassRealm realm ) throws MalformedURLException
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
        if ( jrubyHome == null && legacySupport.getSession().getCurrentProject() != null ){
            jrubyHome = legacySupport.getSession().getCurrentProject().getProperties().getProperty( JRUBY_HOME );
        }

        if (jrubyHome != null ){
                
            setupFromJrubyHome( jrubyHome );
                
        }
        else {
                
            // use jruby from an artifact
            super.setupArtifact( gav, realm );
        }
    }

    protected void setupFromJrubyHome( String jrubyHome )
            throws MalformedURLException
    {
        ClassRealm realm = (ClassRealm) Thread.currentThread().getContextClassLoader();
        
        System.setProperty( JRUBY_HOME, jrubyHome );

        File[] jars = new File( jrubyHome, "lib" ).listFiles( new FileFilter() {

            public boolean accept( File pathname )
            {
                return pathname.getName().endsWith( ".jar" );
            }
        } );
        if ( jars == null || jars.length == 0 ){
            throw new RuntimeException( "found jruby-home variable but no jruby.jar: " + jrubyHome );
        }
        for( File jar : jars ){
            realm.addURL( jar.toURI().toURL() );
        }
    }
    }