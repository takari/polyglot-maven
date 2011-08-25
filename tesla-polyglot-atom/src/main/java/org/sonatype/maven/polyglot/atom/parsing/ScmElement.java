package org.sonatype.maven.polyglot.atom.parsing;

/**
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
class ScmElement {
  private final String connection;
  private final String developerConnection;
  private final String url;

  ScmElement(String connection, String developerConnection, String url) {
    this.connection = connection;
    this.developerConnection = developerConnection;
    this.url = url;
  }

  public String getConnection() {
    return connection;
  }

  public String getDeveloperConnection() {
    return developerConnection;
  }

  public String getUrl() {
    return url;
  }
}
