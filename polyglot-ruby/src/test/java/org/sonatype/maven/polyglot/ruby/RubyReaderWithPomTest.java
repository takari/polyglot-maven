/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.ruby;


public class RubyReaderWithPomTest extends AbstractInjectedTestCase {

  public void testPomWithExecute() throws Exception {
      assertModels( "pom_with_execute/pom.rb", false );
  }

  public void testPomMavenStyle() throws Exception {
      assertModels( "pom_maven_style/pom.rb", false );
  }
  
  public void testPomMavenAlternativeStyle() throws Exception {
      assertModels( "pom_maven_alternative_style/pom.rb", false );
  }

  public void testPomMavenHashStyle() throws Exception {
      assertModels( "pom_maven_hash_style/pom.rb", false );
  }
  
  public void testMavenfile() throws Exception {
      assertModels( "mavenfile/Mavenfile", false );
  }
}