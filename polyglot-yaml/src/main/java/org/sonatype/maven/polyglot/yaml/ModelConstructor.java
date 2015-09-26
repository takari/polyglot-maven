/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.yaml;

import org.apache.maven.model.*;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * YAML model constructor.
 *
 * @author jvanzyl
 * @author bentmann
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 0.7
 */
public final class ModelConstructor extends Constructor {

  /**
   * It maps the runtime class to its Construct implementation.
   */
  private final Map<Class<?>, Construct> pomConstructors = new HashMap<>();

  public ModelConstructor() {
    super(Model.class);

    yamlConstructors.put(Tag.MAP, new ConstructXpp3Dom());
    yamlClassConstructors.put(NodeId.mapping, new MavenObjectConstruct());
    pomConstructors.put(Dependency.class, new ConstructDependency());
    pomConstructors.put(Parent.class, new ConstructParent());
    pomConstructors.put(Extension.class, new ConstructExtension());
    pomConstructors.put(Plugin.class, new ConstructPlugin());
    pomConstructors.put(ReportPlugin.class, new ConstructReportPlugin());

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
  protected Construct getConstructor(Node node) {
    if (pomConstructors.containsKey(node.getType()) && node instanceof ScalarNode) {
      //construct compact form from scalar
      return pomConstructors.get(node.getType());
    } else {
      return super.getConstructor(node);
    }
  }

  private class ConstructXpp3Dom implements Construct {
    private Xpp3Dom toDom(Map<Object, Object> map) {
      Xpp3Dom dom = new Xpp3Dom("configuration");

      for (Map.Entry<Object, Object> entry : map.entrySet()) {
        if (entry.getValue() instanceof Xpp3Dom) {
          Xpp3Dom child = new Xpp3Dom((Xpp3Dom) entry.getValue(), entry.getKey().toString());
          dom.addChild(child);
        } else {
          //TODO issue 44 - what to do if it is a list of Strings
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

  class MavenObjectConstruct extends Constructor.ConstructMapping {
    @Override
    protected Object constructJavaBean2ndStep(MappingNode node, Object object) {
      Class<?> type = node.getType();
      List<Class> specialCases = new ArrayList<Class>();
      specialCases.add(Dependency.class);
      specialCases.add(Model.class);
      specialCases.add(Plugin.class);
      specialCases.add(ReportPlugin.class);
      if (specialCases.contains(type)) {
        String coordinate = removeId(node);
        if (coordinate == null) {
          return super.constructJavaBean2ndStep(node, object);
        }
        if (type.equals(Dependency.class)) {
          Dependency dep = (Dependency) super.constructJavaBean2ndStep(node, object);
          return ConstructDependency.createDependency(coordinate, dep);
        } else if (type.equals(Model.class)) {
          Coordinate coord = Coordinate.createCoordinate(coordinate);
          Model model = (Model) super.constructJavaBean2ndStep(node, object);
          return coord.mergeModel(model);
        } else if (type.equals(Plugin.class)) {
          Coordinate coord = Coordinate.createCoordinate(coordinate);
          Plugin plugin = (Plugin) super.constructJavaBean2ndStep(node, object);
          return coord.mergePlugin(plugin);
        } else if (type.equals(ReportPlugin.class)) {
          Coordinate coord = Coordinate.createCoordinate(coordinate);
          ReportPlugin plugin = (ReportPlugin) super.constructJavaBean2ndStep(node, object);
          return coord.mergeReportPlugin(plugin);
        }
      }
      // create JavaBean
      return super.constructJavaBean2ndStep(node, object);
    }
  }

  /**
   * Dirty hack - remove 'id' if it is present.
   *
   * @param node - the node to remove the coordinate from
   * @return removed coordinate if it was removed
   */
  private String removeId(MappingNode node) {
    NodeTuple id = null;
    String scalar = null;
    for (NodeTuple tuple : node.getValue()) {
      ScalarNode keyNode = (ScalarNode) tuple.getKeyNode();
      String key = keyNode.getValue();
      if ("id".equals(key)) {
        id = tuple;
        ScalarNode valueNode = (ScalarNode) tuple.getValueNode();
        scalar = valueNode.getValue();
      }
    }
    if (id != null) {
      node.getValue().remove(id);
    }
    return scalar;
  }
}
