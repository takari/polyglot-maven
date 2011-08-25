package org.sonatype.maven.polyglot.atom.parsing;

import org.apache.maven.model.*;

import java.util.List;
import java.util.Map;

/**
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
public class Project extends Element {
  private final Id projectId;
  private final Repositories repositories;
  private final String description;
  private final String url;
  private List<Id> deps;
  private Map<String, String> dirs;
  private static final String MAVEN_CENTRAL_URL = "http://repo1.maven.org/maven2";
  private final ScmElement scm;

  public Project(Id projectId, Repositories repositories, String description, String url,
                 List<Id> deps, Map<String, String> dirs, ScmElement scm) {
    this.projectId = projectId;
    this.repositories = repositories;
    this.description = description;
    this.url = url;
    this.deps = deps;
    this.dirs = dirs;
    this.scm = scm;
  }

  public Id getProjectId() {
    return projectId;
  }

  public Repositories getRepositories() {
    return repositories;
  }

  public String getDescription() {
    return description;
  }

  public String getUrl() {
    return url;
  }

  public List<Id> getDeps() {
    return deps;
  }

  public Map<String, String> getDirs() {
    return dirs;
  }

  public Model toMavenModel() {
    Model model = new Model();
    model.setDescription(description);
    model.setUrl(url);
    model.setName(projectId.getArtifact());
    model.setGroupId(projectId.getGroup());
    model.setVersion(projectId.getVersion());
    model.setArtifactId(projectId.getArtifact());
    model.setModelVersion("4.0.0");
    
    // Add jar repository urls.
    if (null == repositories) {
      // Add maven central if no repos exist.
      Repository repository = new Repository();
      repository.setId("Maven Central");
      repository.setUrl(MAVEN_CENTRAL_URL);
      model.addRepository(repository);

    } else {
      for (String repoUrl : repositories.getRepositories()) {
        Repository repository = new Repository();
        repository.setId(Integer.toString(repoUrl.hashCode()));
        repository.setUrl(repoUrl);
        model.addRepository(repository);
      }
    }

    // Add project dependencies.
    for (Id dep : deps) {
      Dependency dependency = new Dependency();
      dependency.setGroupId(dep.getGroup());
      dependency.setArtifactId(dep.getArtifact());
      dependency.setVersion(dep.getVersion());
      
      if (null != dep.getClassifier())
        dependency.setClassifier(dep.getClassifier());
      model.addDependency(dependency);
    }

    // Optional source dirs customization.
    if (dirs != null) {
      Build build = new Build();
      String srcDir = dirs.get("src");
      String testDir = dirs.get("test");

      if (null != srcDir)
        build.setSourceDirectory(srcDir);

      if (null != testDir)
        build.setTestSourceDirectory(testDir);

      model.setBuild(build);
    }

    if (null != scm) {
      Scm scm = new Scm();
      scm.setConnection(this.scm.getConnection());
      scm.setDeveloperConnection(this.scm.getDeveloperConnection());
      scm.setUrl(this.scm.getUrl());

      model.setScm(scm);
    }
    return model;
  }

  public ScmElement getScm() {
    return scm;
  }
}
