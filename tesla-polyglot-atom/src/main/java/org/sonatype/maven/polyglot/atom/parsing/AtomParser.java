package org.sonatype.maven.polyglot.atom.parsing;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Parses the atom token stream into an internal model, which can be emitted
 * as a Maven model.
 *
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
public class AtomParser {
  // TODO(dhanji): Replace with proper logging/console out
  private final Logger log = Logger.getLogger(AtomParser.class.getName());
  private final List<Token> tokens;

  // Parser state. (current index)
  private int i = 0;

  public AtomParser(List<Token> tokens) {
    this.tokens = tokens;
  }

  public Project parse() {
    chewEols();
    Repositories repositories = repositories();
    if (null == repositories) {
      log.warning("No repositories specified in atom file, defaulting to Maven Central.");
    }

    chewEols();
    Project project = project(repositories);

    return project;
  }

  /**
   * Parsing rule for a single project build definition.
   *
   * project := 'project' STRING AT URL EOL
   */
  private Project project(Repositories repositories) {
    if (match(Token.Kind.PROJECT) == null) {
      return null;
    }

    List<Token> signature = match(Token.Kind.STRING);
    if (null == signature) {
      log.severe("Expected string describing project after 'project'.");
    }

    String projectDescription = signature.get(0).value;
    String projectUrl = null;

    signature = match(Token.Kind.AT, Token.Kind.STRING);
    if (null != signature) {
      projectUrl = signature.get(1).value;
    }

    if (match(Token.Kind.EOL) == null) {
      log.severe("Expected end of line after project declaration");
      return null;
    }

    // id definition
    indent();
    if (match(Token.Kind.ID) == null) {
      log.severe("Expected 'id' after project declaration");
      return null;
    }

    // Now expect a colon.
    if (match(Token.Kind.COLON) == null) {
      log.severe("Expected ':' after 'id'");
      return null;
    }

    Id projectId = id();

    chewEols();
    Map<String, String> dirs = srcs();

    chewEols();
    List<Id> deps = dependencies();

    chewEols();
    ScmElement scm = scm();

    return new Project(projectId, repositories, projectDescription, projectUrl, deps, dirs, scm);
  }

  private ScmElement scm() {
    chewIndents();
    if (match(Token.Kind.SCM, Token.Kind.COLON, Token.Kind.LBRACKET) == null) {
      return null; // no scm element
    }

    chewEols();
    chewIndents();

    String connection = null, developerConnection = null, url = null;
    List<Token> ident;
    while ((ident = match(Token.Kind.IDENT)) != null) {
      String label = ident.get(0).value;

      if (match(Token.Kind.COLON) == null) {
        throw new RuntimeException("Expected : after label");
      }

      List<Token> valueToken = match(Token.Kind.STRING);
      if (null == valueToken) {
        throw new RuntimeException("Expected String after :");
      }
      String value = valueToken.get(0).value;

      if ("connection".equals(label))
        connection = value;
      else if ("developerConnection".equals(label))
        developerConnection = value;
      else if ("url".equals(label))
        url = value;

      chewEols();
      chewIndents();
    }

    if (match(Token.Kind.RBRACKET) == null) {
      throw new RuntimeException("Expected ] after srcs list");
    }

    return new ScmElement(connection, developerConnection, url);
  }

  /**
   * Custom directory structure for maven builds.
   * 
   */
  private Map<String, String> srcs() {
    indent();
    if (match(Token.Kind.SRCS, Token.Kind.COLON, Token.Kind.LBRACKET) == null) {
      return null; // No dirs!
    }

    chewEols();
    chewIndents();
    List<Token> aMatch = match(Token.Kind.IDENT);
    if (aMatch == null) {
      throw new RuntimeException("Expected 'src' or 'test'");
    }

    String srcDir = null;
    if ("src".equals(aMatch.get(0).value)) {
      if (null == match(Token.Kind.COLON)) {
        throw new RuntimeException("Expected : after src");
      }

      List<Token> srcDirToken = match(Token.Kind.STRING);
      if (null == srcDirToken) {
        throw new RuntimeException("Expected string after src:");
      }

      srcDir = srcDirToken.get(0).value;
    }

    aMatch = match(Token.Kind.IDENT);

    String testDir = null;
    if (null != aMatch && "test".equals(aMatch.get(0).value)) {
      if (null == match(Token.Kind.COLON)) {
        throw new RuntimeException("Expected : after test");
      }

      List<Token> testDirToken = match(Token.Kind.STRING);
      if (null == testDirToken) {
        throw new RuntimeException("Expected string after test:");
      }

      testDir = testDirToken.get(0).value;
    }

    Map<String, String> dirs = new HashMap<String, String>();
    // Strip quotes and store.
    if (null != srcDir)
      dirs.put("src", srcDir.substring(1, srcDir.length() - 1));
    if (null != testDir)
    dirs.put("test", testDir.substring(1, testDir.length() - 1));

    if (match(Token.Kind.RBRACKET) == null) {
      throw new RuntimeException("Expected ] after srcs list");
    }

    return dirs;
  }

  /**
   * Dependencies of a project. The real meat of it.
   */
  private List<Id> dependencies() {
    indent();
    if (match(Token.Kind.DEPS, Token.Kind.COLON, Token.Kind.LBRACKET) == null) {
      return null; // no deps.
    }
    List<Id> deps = new ArrayList<Id>();

    chewEols();
    chewIndents();

    // Slurp up the dep ids.
    Id id;
    while ((id = id()) != null) {
      // Optional additional params at the end.
      String classifier = classifier();
      if (null != classifier)
        id.setClassifier(classifier);

      chewEols();
      chewIndents();
      deps.add(id);
    }

    if (match(Token.Kind.RBRACKET) == null) {
      // ERROR!
      throw new RuntimeException("Expected ]");
    }

    return deps;
  }

  /**
   * classifier := LPAREN IDENT RPAREN
   */
  private String classifier() {
    if (match(Token.Kind.LPAREN) == null) {
      return null;
    }

    List<Token> classifier = match(Token.Kind.IDENT);
    if (classifier == null) {
      log.severe("Expected identifier after '(' in classifier clause");
      return null;
    }

    if (match(Token.Kind.RPAREN) == null) {
      log.severe("Expected ')' in classifier clause before end of line");
      return null;
    }

    return classifier.get(0).value;
  }

  /**
   * Id of a project definition.
   *
   * id := IDENT (DOT IDENT)* COLON IDENT COLON IDENT EOL
   */
  private Id id() {

    String groupId = idFragment();
    if (groupId == null) {
      return null;
    }

    // Now expect a colon.
    if (match(Token.Kind.COLON) == null) {
      return null;
    }

    String artifactId = idFragment();
    if (artifactId == null) {
      return null;
    }

    // Now expect a colon.
    if (match(Token.Kind.COLON) == null) {
      return null;
    }

    String version = idFragment();
    if (version == null) {
      return null;
    }

    return new Id(groupId, artifactId, version);
  }


  private String idFragment() {
    StringBuilder fragment = new StringBuilder();
    List<Token> idFragment;
    while ((idFragment = match(Token.Kind.IDENT)) != null) {
      fragment.append(idFragment.get(0).value);
      if (match(Token.Kind.DOT) != null) {
        fragment.append('.');
      }
    }

    return fragment.toString();
  }

  /**
   * Optional repositories declaration at the top of the file.
   *
   * repositories := 'repositories' LEFT_WAVE STRING (COMMA STRING)*
   */
  private Repositories repositories() {
    if (match(Token.Kind.REPOSITORIES, Token.Kind.LEFT_WAVE) == null) {
      return null;
    }
    List<String> repoUrls = new ArrayList<String>();

    List<Token> repositories = match(Token.Kind.STRING);
    if (repositories == null) {
      // ERROR expected String.
      throw new RuntimeException("Error: expected URL string after 'respositories");
    }

    // Validate first URL...
    String url = repositories.get(0).value; // Strip ""
    repoUrls.add(validateUrl(url));

    while ((repositories = match(Token.Kind.COMMA)) != null) {
      chewEols();
      chewIndents();
      repositories = match(Token.Kind.STRING);
      chewEols();
      chewIndents();
      if (null != repositories) {
        url = validateUrl(repositories.get(0).value);

        repoUrls.add(url);
      }
    }

    return new Repositories(repoUrls);
  }

  private String validateUrl(String url) {
    url = url.substring(1, url.length() - 1);

    // Validate URL...
    try {
      new URL(url);
    } catch (MalformedURLException e) {
      throw new RuntimeException("Invalid URL: " + url, e);
    }
    return url;
  }

  // Production tools.


  private void indent() {
    if (match(Token.Kind.INDENT, Token.Kind.INDENT) != null) {
      // ERROR!
    }
  }

  private Token anyOf(Token.Kind... ident) {
    if (i >= tokens.size()) {
      return null;
    }
    for (Token.Kind kind : ident) {
      Token token = tokens.get(i);
      if (kind == token.kind) {
        i++;
        return token;
      }
    }

    // No match =(
    return null;
  }

  private List<Token> match(Token.Kind... ident) {
    int cursor = i;
    for (Token.Kind kind : ident) {

      // What we want is more than the size of the token stream.
      if (cursor >= tokens.size()) {
        return null;
      }

      Token token = tokens.get(cursor);
      if (token.kind != kind) {
        return null;
      }

      cursor++;
    }

    // Forward cursor in token stream to match point.
    int start = i;
    i = cursor;
    return tokens.subList(start, i);
  }

  private void chewEols() {
    // Chew up end-of-lines.
    while (match(Token.Kind.EOL) != null);
  }

  private void chewIndents() {
    // Chew up end-of-lines.
    while (match(Token.Kind.INDENT) != null);
  }
}
