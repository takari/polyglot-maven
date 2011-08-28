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
import org.apache.maven.model.io.DefaultModelWriter;
import org.apache.maven.model.io.ModelWriter;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.sonatype.maven.polyglot.io.ModelWriterSupport;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Component(role = ModelWriter.class, hint = "atom")
public class AtomModelWriter extends ModelWriterSupport {

  @Requirement
  protected Logger log;
  String indent = "  ";

  public void write(final Writer output, final Map<String, Object> options, final Model model) throws IOException {
    assert output != null;
    assert model != null;

    PrintWriter pw = new PrintWriter(output);

    // repos
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
    // name
    pw.println("project \"" + model.getName() + "\" @ " + model.getUrl() + "\"");
    // id
    pw.println(indent + "id: " + model.getGroupId() + ":" + model.getArtifactId() + ":" + model.getVersion());
    // id
    if (model.getParent() != null) {
      pw.println(indent + "parent: " + model.getParent().getGroupId() + ":" + model.getParent().getArtifactId() + ":" + model.getParent().getVersion());
    }
    // packaging
    pw.println(indent + "packaging: " + model.getPackaging());
    pw.println();
    // properties
    if (!model.getProperties().isEmpty()) {
      List<Object> keys = new ArrayList<Object>(model.getProperties().keySet());
      pw.print(indent + "properties: [ ");
      for (int i = 0; i < keys.size(); i++) {
        Object key = keys.get(i);
        if (i != 0) {
          pw.print("                ");
        }
        pw.print(key + ": " + model.getProperties().get(key));
        if (i + 1 != keys.size()) {
          pw.println();
        }
      }
      pw.println(" ]");
      pw.println();
    }
    // depMan
    deps(pw, "overrides", model.getDependencyManagement().getDependencies());
    // deps
    deps(pw, "deps", model.getDependencies());
    // modules
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
    // pluginManagenent
    if (model.getBuild().getPluginManagement() != null) {
      plugins(pw, "plugins", model.getBuild().getPluginManagement().getPlugins());
    }
    // plugins
    if (!model.getBuild().getPlugins().isEmpty()) {
      plugins(pw, "plugins", model.getBuild().getPlugins());
    }
    // dependencyManagement
    if (model.getDistributionManagement() != null) {

    }
  }

  private void deps(PrintWriter pw, String elementName, List<Dependency> deps) {
    if (!deps.isEmpty()) {
      pw.print(indent + elementName + ": [ ");
      for (int i = 0; i < deps.size(); i++) {
        Dependency d = deps.get(i);
        if (i != 0) {
          pw.print("               ");
        }
        pw.print(d.getGroupId() + ":" + d.getArtifactId() + ":" + d.getVersion());
        if (i + 1 != deps.size()) {
          pw.println();
        }
      }
      pw.println(" ]");
      pw.println();
    }
  }

  private void plugins(PrintWriter pw, String elementName, List<Plugin> plugins) {
    if (!plugins.isEmpty()) {
      pw.print(indent + elementName + ": [ ");
      for (int i = 0; i < plugins.size(); i++) {
        Plugin plugin = plugins.get(i);
        if (i != 0) {
          pw.print("            ");
        }
        pw.print(plugin.getGroupId() + ":" + plugin.getArtifactId() + ":" + plugin.getVersion());
        if (plugin.getConfiguration() != null) {
          pw.println();
          pw.print("               configuration:[ ");
          Xpp3Dom configuration = (Xpp3Dom) plugin.getConfiguration();
          for (Xpp3Dom c : configuration.getChildren()) {
            pw.print(c.getName() + ": " + c.getValue() + " ");
          }
          pw.print(" ]");
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