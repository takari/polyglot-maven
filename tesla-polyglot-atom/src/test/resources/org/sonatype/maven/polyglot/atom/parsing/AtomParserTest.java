package org.sonatype.maven.polyglot.atom.parsing;

import junit.framework.TestCase;
import org.apache.maven.model.building.ModelSource;
import org.apache.maven.model.building.StringModelSource;
import org.codehaus.plexus.util.IOUtil;

import java.net.MalformedURLException;

/**
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
public class AtomParserTest extends TestCase {
  private static final String REPO_URLS = "http://repository.codehaus.org," +
      " http://maven.org/central, http://repo1.maven.org/maven2";

  private String pom;
  private ModelSource modelSource;

  @Override
  protected void setUp() throws Exception {
    this.pom = IOUtil.toString(AtomParserTest.class.getResourceAsStream("example_pom.atom"));
    this.modelSource = new StringModelSource(pom);
  }

  public final void testRepositoryLineMalformedUrls() {
    Exception thrown = null;
    try {
      new AtomParser(modelSource, new Tokenizer("repositories << \"...\"\n").tokenize()).parse();
      fail("Expected exception for Malformed URL");
    } catch (RuntimeException e) {
      thrown = e;
    }
    assertTrue(thrown.getCause() instanceof MalformedURLException);
  }

  public final void testRepositoryLineParsing() {
    Project element = new AtomParser(modelSource, new Tokenizer(pom).tokenize()).parse();

    assertEquals(String.format("[%s]", REPO_URLS), element.getRepositories().toString());
  }

  public final void testProjectParsing() {
    Project project = new AtomParser(modelSource, new Tokenizer(pom).tokenize()).parse();

    assertEquals("\"Google Guice\"", project.getDescription());
    assertEquals("\"http://code.google.com/p/google-guice\"", project.getUrl());

    assertEquals("com.google.inject", project.getProjectId().getGroup());
    assertEquals("guice", project.getProjectId().getArtifact());
    assertEquals("2.0-SNAPSHOT", project.getProjectId().getVersion());

    assertEquals(3, project.getDeps().size());

    Id id = project.getDeps().get(0);
    assertEquals("junit:junit:3.8.1", id.toString());
    id = project.getDeps().get(1);
    assertEquals("junit:junit:4.0", id.toString());
    id = project.getDeps().get(2);
    assertEquals("kunit:org.kunit:SNAPSHOT-1.0b", id.toString());
    assertEquals("jdk15", id.getClassifier());

    assertEquals(2, project.getDirs().size());
    assertEquals("src", project.getDirs().get("src"));
    assertEquals("test", project.getDirs().get("test"));


    assertEquals("\"url:git:git@github.com:mikebrock/mvel.git\"", project.getScm().getUrl());
    assertEquals("\"con:git:git@github.com:mikebrock/mvel.git\"", project.getScm().getConnection());
    assertEquals("\"dev:git:git@github.com:mikebrock/mvel.git\"",
        project.getScm().getDeveloperConnection());
  }

  public final void testScmParsing() {

  }
}
