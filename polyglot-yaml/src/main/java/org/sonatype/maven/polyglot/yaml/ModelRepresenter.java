/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.yaml;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

import java.util.*;

/**
 * YAML model representer.
 *
 * @author jvanzyl
 * @author bentmann
 * @since 0.7
 */
class ModelRepresenter extends Representer {
  public ModelRepresenter() {
    this.representers.put(Xpp3Dom.class, new RepresentXpp3Dom());
    Represent stringRepresenter = this.representers.get(String.class);
    this.representers.put(Boolean.class, stringRepresenter);
    this.multiRepresenters.put(Number.class, stringRepresenter);
    this.multiRepresenters.put(Date.class, stringRepresenter);
    this.multiRepresenters.put(Enum.class, stringRepresenter);
    this.multiRepresenters.put(Calendar.class, stringRepresenter);
  }

  protected NodeTuple representJavaBeanProperty(Object javaBean, Property property,
                                                Object propertyValue, Tag customTag) {
    if (propertyValue == null) return null;
    if (propertyValue instanceof Map) {
      Map map = (Map) propertyValue;
      if (map.isEmpty()) return null;
    }
    if (propertyValue instanceof List) {
      List map = (List) propertyValue;
      if (map.isEmpty()) return null;
    }
    return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
  }


  private class RepresentXpp3Dom implements Represent {

    public Node representData(Object data) {
      return representMapping(Tag.MAP, toMap((Xpp3Dom) data), null);
    }

    private Map<String, Object> toMap(Xpp3Dom dom) {
      Map<String, Object> map = new LinkedHashMap<String, Object>();

      int n = dom.getChildCount();
      for (int i = 0; i < n; i++) {
        Xpp3Dom child = dom.getChild(i);
        if (child.getValue() != null) {
          map.put(child.getName(), child.getValue());
        } else {
          map.put(child.getName(), toMap(child));
        }
      }

      return map;
    }
  }
}
