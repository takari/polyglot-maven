/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot;

public class Constants {

    private static final String TESLA_POLYGLOT = "tesla-polyglot-";
    private static final String IO_TESLA_POLYGLOT = "io.tesla.polyglot";
    // FIXME do not hardcode the version
    private static final String VERSION = "0.0.8";
        
    public static String getGroupId(){
        return IO_TESLA_POLYGLOT;
    }
    
    public static String getArtifactId( String postfix){
        return TESLA_POLYGLOT + postfix;
    }
    
    public static String getVersion(){
        return VERSION;
    }

    public static String getGAV( String postfix ){
        return getGroupId() + ":" + getArtifactId( postfix ) + ":" + getVersion();
    }

}