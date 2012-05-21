/*
 * Copyright (C) 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
// */

package org.sonatype.maven.polyglot.atom;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.Repository;
import org.apache.maven.model.io.ModelWriter;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.sonatype.maven.polyglot.io.ModelWriterSupport;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component(role = ModelWriter.class, hint = "atom")
public class AtomModelWriter extends ModelWriterSupport {

  @Requirement
  protected Logger log;
  String indent = "  ";

  public void write(final Writer output, final Map<String, Object> options, final Model model) throws IOException {
    assert output != null;
    assert model != null;

    PrintWriter pw = new PrintWriter(output);

    repositories(pw, model);
    project(pw, model);
    id(pw, model);
    parent(pw, model);
    properties(pw, model);
    dependencyManagement(pw, model);
    dependencies(pw, model);
    modules(pw, model);
    pluginManagement(pw, model);
    plugins(pw, model);

    pw.flush();
    pw.close();
  }

  private void repositories(PrintWriter pw, Model model) {
    List<Repository> repositories = model.getRepositories();
    if (!repositories.isEmpty()) {
      pw.print("repositories << \"");
      for (int i = 0; i < repositories.size(); i++) {
        pw.print(repositories.get(i).getUrl());
        if (i + 1 != repositories.size()) {
          pw.print(",");
        }
      }
      pw.println("\"");
      pw.println();
    }
  }

  private void project(PrintWriter pw, Model model) {
    String name = model.getName();
    if (name == null) {
      name = model.getArtifactId();
    }
    String url = model.getUrl() == null ? "" : model.getUrl();
    pw.print("project \"" + name + "\" @ \"" + url + "\"");
    packaging(pw, model);
  }

  private void id(PrintWriter pw, Model model) {
    String groupId = model.getGroupId();
    if (groupId == null & model.getParent() != null) {
      groupId = model.getParent().getGroupId();
    }

    String version = model.getVersion();
    if (version == null && model.getParent() != null) {
      version = model.getParent().getVersion();
    }
    pw.println(indent + "id: " + groupId + ":" + model.getArtifactId() + ":" + version);
  }

  private void parent(PrintWriter pw, Model model) {
    if (model.getParent() != null) {
      pw.print(indent + "inherits: " + model.getParent().getGroupId() + ":" + model.getParent().getArtifactId() + ":" + model.getParent().getVersion());
      if (model.getParent().getRelativePath() != null) {
        //pw.println(":" + model.getParent().getRelativePath());
//        pw.println(":" + "../pom.atom");
      }
      pw.println();
    }
  }

  private void packaging(PrintWriter pw, Model model) {
    pw.println(" as " + model.getPackaging());
  }

  private void properties(PrintWriter pw, Model model) {
    if (!model.getProperties().isEmpty()) {
      List<Object> keys = new ArrayList<Object>(model.getProperties().keySet());
      pw.print(indent + "properties: [ ");
      for (int i = 0; i < keys.size(); i++) {
        Object key = keys.get(i);
        if (i != 0) {
          pw.print("                ");
        }
        Object value = model.getProperties().get(key);
        if (value != null) {
          pw.print(key + ": " + value);
          if (i + 1 != keys.size()) {
            pw.println();
          }
        }
      }
      pw.println(" ]");
      pw.println();
    }
  }

  private void modules(PrintWriter pw, Model model) {
    List<String> modules = model.getModules();
    if (!modules.isEmpty()) {
      pw.print(indent + "modules: [ ");
      for (int i = 0; i < modules.size(); i++) {
        String module = modules.get(i);
        if (i != 0) {
          pw.print("             ");
        }
        pw.print(module);
        if (i + 1 != modules.size()) {
          pw.println();
        }
      }
      pw.println(" ]");
      pw.println();
    }
  }

  private void dependencyManagement(PrintWriter pw, Model model) {
    if (model.getDependencyManagement() != null) {
      deps(pw, "overrides", model.getDependencyManagement().getDependencies());
    }
  }

  private void dependencies(PrintWriter pw, Model model) {
    deps(pw, "deps", model.getDependencies());
  }

  private void deps(PrintWriter pw, String elementName, List<Dependency> deps) {
    if (!deps.isEmpty()) {
      pw.print(indent + elementName + ": [ ");
      for (int i = 0; i < deps.size(); i++) {
        Dependency d = deps.get(i);
        if (i != 0) {
          pw.print("               ");
        }
        if (d.getVersion() != null) {
          pw.print(d.getGroupId() + ":" + d.getArtifactId() + ":" + d.getVersion());
        } else {
          //
          // We are assuming the model is well-formed and that the parent is providing a version
          // for this particular dependency.
          //
          pw.print(d.getGroupId() + ":" + d.getArtifactId());
        }
        if (d.getClassifier() != null) {
          pw.print("(" + d.getClassifier() + ")");
        }
        if (i + 1 != deps.size()) {
          pw.println();
        }
      }
      pw.println(" ]");
      pw.println();
    }
  }

  private void pluginManagement(PrintWriter pw, Model model) {
    if (model.getBuild() != null && model.getBuild().getPluginManagement() != null) {
      plugins(pw, "pluginOverrides", model.getBuild().getPluginManagement().getPlugins());
    }
  }

  private void plugins(PrintWriter pw, Model model) {
    if (model.getBuild() != null && !model.getBuild().getPlugins().isEmpty()) {
      plugins(pw, "plugins", model.getBuild().getPlugins());
    }
  }

  // need to write nested objects
  private void plugins(PrintWriter pw, String elementName, List<Plugin> plugins) {
    if (!plugins.isEmpty()) {
      pw.print(indent + elementName + ": [ ");
      for (int i = 0; i < plugins.size(); i++) {
        Plugin plugin = plugins.get(i);
        if (i != 0) {
          pw.print("             ");
        }
        pw.print(plugin.getGroupId() + ":" + plugin.getArtifactId() + ":" + plugin.getVersion());
        if (plugin.getConfiguration() != null) {
          pw.println();
          Xpp3Dom configuration = (Xpp3Dom) plugin.getConfiguration();
          if (configuration.getChildCount() != 0) {
            int count = configuration.getChildCount();
            for (int j = 0; j < count; j++) {
              Xpp3Dom c = configuration.getChild(j);
              if (c.getValue() != null) {
                if (j != 0) {
                  pw.print("                            ");
                }
                pw.print(c.getName() + ": " + c.getValue());
                if (j + 1 != count) {
                  pw.println();
                }
              }
            }
          }
        }
        if (i + 1 != plugins.size()) {
          pw.println();
        }
      }
      pw.println(" ]");
      pw.println();
    }
  }
}
