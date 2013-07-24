/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

/**
 * The simple Scala POM example from the Polyglot Maven
 * Confluence documentation page.
 **/

project("myGroupId:myArtifactId:1.0-SNAPSHOT") { proj =>

    //...adds scala library dependency and configures Maven Scala compiler plugin...
    proj includesScalaSourceCode "2.7.7"

    //...adds normal Maven artifact dependency to Apache Commons HttpClient 3.0.1...
    proj dependency "apache-httpclient:commons-httpclient:3.0.1"

}
