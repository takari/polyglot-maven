package org.sonatype.maven.polyglot.atom.parsing;

import java.util.List;

/**
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
public class Repositories extends Element {
  private final List<String> repositories;
  public Repositories(List<String> repositories) {
    this.repositories = repositories;
  }

  public List<String> getRepositories() {
    return repositories;
  }

  @Override
  public String toString() {
    return repositories.toString();
  }
}
