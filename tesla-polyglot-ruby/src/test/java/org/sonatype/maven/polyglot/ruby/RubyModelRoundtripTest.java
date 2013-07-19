package org.sonatype.maven.polyglot.ruby;


public class RubyModelRoundtripTest extends AbstractInjectedTestCase {

  public void testSitebricksPom() throws Exception {
	  assertRoundtrip( "sitebricks-pom.xml", false );
  }

  public void testSitebricksParentPom() throws Exception {
	  assertRoundtrip( "sitebricks-parent-pom.xml", false );
  }

  public void testJRubyCorePom() throws Exception {
      assertRoundtrip( "jruby-core-pom.xml", false );
  }

  public void testJRubyParentPom() throws Exception {
      assertRoundtrip( "jruby-parent-pom.xml", false );
  }
  
  public void testJRubyTestPom() throws Exception {
      assertRoundtrip( "jruby-test-pom.xml", false );
  }
  
  public void testMavenParentPom() throws Exception {
      assertRoundtrip( "maven-parent-pom.xml", false );
  }

  public void testJrubyOsgiTestPom() throws Exception {
      assertRoundtrip( "jruby-osgi-test-pom.xml", true );
  }

}
