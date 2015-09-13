/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.yaml;

import org.apache.maven.model.Build;
import org.apache.maven.model.BuildBase;
import org.apache.maven.model.CiManagement;
import org.apache.maven.model.Contributor;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Developer;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.Extension;
import org.apache.maven.model.IssueManagement;
import org.apache.maven.model.License;
import org.apache.maven.model.MailingList;
import org.apache.maven.model.Model;
import org.apache.maven.model.Notifier;
import org.apache.maven.model.Organization;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.model.Profile;
import org.apache.maven.model.ReportPlugin;
import org.apache.maven.model.ReportSet;
import org.apache.maven.model.Reporting;
import org.apache.maven.model.Repository;
import org.apache.maven.model.Resource;
import org.apache.maven.model.Scm;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * YAML model constructor.
 *
 * @author jvanzyl
 * @author bentmann
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 0.7
 */
public class ModelConstructor extends Constructor {
  public ModelConstructor() {
    super(Model.class);

    yamlConstructors.put(Tag.MAP, new ConstructXpp3Dom());

    TypeDescription desc;

    desc = new TypeDescription(Model.class);
    desc.putListPropertyType("licenses", License.class);
    desc.putListPropertyType("mailingLists", MailingList.class);
    desc.putListPropertyType("dependencies", Dependency.class);
    desc.putListPropertyType("modules", String.class);
    desc.putListPropertyType("profiles", Profile.class);
    desc.putListPropertyType("repositories", Repository.class);
    desc.putListPropertyType("pluginRepositories", Repository.class);
    desc.putListPropertyType("developers", Developer.class);
    desc.putListPropertyType("contributors", Contributor.class);
    addTypeDescription(desc);

    desc = new TypeDescription(Dependency.class);
    desc.putListPropertyType("exclusions", Exclusion.class);
    addTypeDescription(desc);

    desc = new TypeDescription(DependencyManagement.class);
    desc.putListPropertyType("dependencies", Dependency.class);
    addTypeDescription(desc);

    desc = new TypeDescription(Build.class);
    desc.putListPropertyType("extensions", Extension.class);
    desc.putListPropertyType("resources", Resource.class);
    desc.putListPropertyType("testResources", Resource.class);
    desc.putListPropertyType("filters", String.class);
    desc.putListPropertyType("plugins", Plugin.class);
    addTypeDescription(desc);

    desc = new TypeDescription(BuildBase.class);
    desc.putListPropertyType("resources", Resource.class);
    desc.putListPropertyType("testResources", Resource.class);
    desc.putListPropertyType("filters", String.class);
    desc.putListPropertyType("plugins", Plugin.class);
    addTypeDescription(desc);

    desc = new TypeDescription(PluginManagement.class);
    desc.putListPropertyType("plugins", Plugin.class);
    addTypeDescription(desc);

    desc = new TypeDescription(Plugin.class);
    desc.putListPropertyType("executions", PluginExecution.class);
    addTypeDescription(desc);

    desc = new TypeDescription(PluginExecution.class);
    desc.putListPropertyType("goals", String.class);
    addTypeDescription(desc);

    desc = new TypeDescription(Reporting.class);
    desc.putListPropertyType("plugins", ReportPlugin.class);
    addTypeDescription(desc);

    desc = new TypeDescription(ReportPlugin.class);
    desc.putListPropertyType("reportSets", ReportSet.class);
    addTypeDescription(desc);

    desc = new TypeDescription(ReportSet.class);
    desc.putListPropertyType("reports", String.class);
    addTypeDescription(desc);

    desc = new TypeDescription(CiManagement.class);
    desc.putListPropertyType("notifiers", Notifier.class);
    addTypeDescription(desc);

    desc = new TypeDescription(Developer.class);
    desc.putListPropertyType("roles", String.class);
    addTypeDescription(desc);

    desc = new TypeDescription(Contributor.class);
    desc.putListPropertyType("roles", String.class);
    addTypeDescription(desc);

    desc = new TypeDescription(MailingList.class);
    desc.putListPropertyType("otherArchives", String.class);
    addTypeDescription(desc);

    // Simple types
    addTypeDescription(new TypeDescription(DistributionManagement.class));
    addTypeDescription(new TypeDescription(Scm.class));
    addTypeDescription(new TypeDescription(IssueManagement.class));
    addTypeDescription(new TypeDescription(Parent.class));
    addTypeDescription(new TypeDescription(Organization.class));
  }

  @Override
  protected Map<Object, Object> constructMapping(MappingNode node) {
    Map<Object, Object> mapping = createDefaultMap();
    constructMapping2ndStep(node, mapping);
    return mapping;
  }

  private class ConstructXpp3Dom implements Construct {
    private Xpp3Dom toDom(Map<Object, Object> map) {
      Xpp3Dom dom = new Xpp3Dom("configuration");

      for (Map.Entry<Object, Object> entry : map.entrySet()) {
        if (entry.getValue() instanceof Xpp3Dom) {
          Xpp3Dom child = new Xpp3Dom((Xpp3Dom) entry.getValue(), entry.getKey().toString());
          dom.addChild(child);
        } else {
          Xpp3Dom child = new Xpp3Dom(entry.getKey().toString());
          child.setValue(entry.getValue().toString());
          dom.addChild(child);
        }
      }

      return dom;
    }

    public Object construct(Node node) {
      return toDom(constructMapping((MappingNode) node));
    }

    public void construct2ndStep(Node node, Object object) {
      throw new YAMLException("Unexpected recursive mapping structure. Node: " + node);
    }
  }
}
