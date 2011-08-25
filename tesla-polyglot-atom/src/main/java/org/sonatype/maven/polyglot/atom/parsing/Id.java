package org.sonatype.maven.polyglot.atom.parsing;

/**
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
public class Id {
  private final String group;
  private final String artifact;
  private final String version;
  private String classifier;

  public Id(String group, String artifact, String version) {
    this.group = group;
    this.artifact = artifact;
    this.version = version;
  }

  public String getGroup() {
    return group;
  }

  public String getArtifact() {
    return artifact;
  }

  public String getVersion() {
    return version;
  }

  public String getClassifier() {
    return classifier;
  }

  public void setClassifier(String classifier) {
    this.classifier = classifier;
  }

  @Override
  public String toString() {
    return group + ":" + artifact + ":" + version;
  }
}
