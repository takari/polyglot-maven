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

package org.sonatype.maven.polyglot.ruby;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.Repository;
import org.apache.maven.model.io.ModelWriter;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.sonatype.maven.polyglot.io.ModelWriterSupport;

@Component(role = ModelWriter.class, hint = "ruby")
public class RubyModelWriter extends ModelWriterSupport {

    @Requirement
    protected Logger log;

    public void write(final Writer output, final Map<String, Object> options,
            final Model model) throws IOException {
        assert output != null;
        assert model != null;

        ModelPrinter p = new ModelPrinter(output);
        p.print(model);
    }

    static class ModelPrinter {

        private static final String HASH_INDENT = "                            ";
        private static final String INDENT = "  ";
        private final RubyPrintWriter p;

        ModelPrinter(Writer output) {
            this.p = new RubyPrintWriter(output);
        }

        void print(Model model) {

            project(model);

            p.flush();
            p.close();
        }

        void repositories(List<Repository> repositories) {
            printRepositories("repository", repositories);
        }

        void pluginRepositories(List<Repository> repositories) {
            printRepositories("plugin_repository", repositories);
        }

        private void printRepositories(String name,
                List<Repository> repositories) {
            if (repositories.size() > 0) {
                p.println();
            }
            for (int i = 0; i < repositories.size(); i++) {
                p.println(name, repositories.get(i).getUrl());
            }
        }

        void project(Model model) {
            String name = model.getName();
            if (name == null) {
                name = model.getArtifactId();
            }
            p.printStartBlock("project", name, model.getUrl());
            p.println();
            
            id(model);
            parent(model.getParent());
            
            p.println("packaging", model.getPackaging());
            
            description(model.getDescription());

            repositories(model.getRepositories());
            pluginRepositories(model.getPluginRepositories());
            
            properties(model.getProperties());
            
            dependencies(model.getDependencies());
            
            modules(model.getModules());
            
            managements(model.getDependencyManagement(), model.getBuild());
            
            build(model.getBuild());
            
            p.printEndBlock();
        }

        void description(String description) {
            if (description != null) {
                p.println();
                p.println("description", description);
            }
        }

        void build(Build build) {
            if (build != null) {
                plugins(build.getPlugins());
            }
        }

        void managements(DependencyManagement dependencyManagement, Build build) {
            if ((dependencyManagement != null && !dependencyManagement
                    .getDependencies().isEmpty())
                    || (build != null && build.getPluginManagement() != null && !build
                            .getPluginManagement().getPlugins().isEmpty())) {
                p.println();
                p.printStartBlock("overrides");
                dependencies(dependencyManagement.getDependencies());
                if (build.getPluginManagement() != null) {
                    plugins(build.getPluginManagement().getPlugins());
                }
                p.printEndBlock();
            }
        }

        void id(Model model) {
            String groupId = model.getGroupId();
            if (groupId == null & model.getParent() != null) {
                groupId = model.getParent().getGroupId();
            }

            String version = model.getVersion();
            if (version == null && model.getParent() != null) {
                version = model.getParent().getVersion();
            }
            p.println("id", groupId + ":" + model.getArtifactId() + ":"
                    + version);
        }

        void parent(Parent parent) {
            if (parent != null) {
                p.print("inherit", parent.getGroupId() + ":"
                        + parent.getArtifactId() + ":" + parent.getVersion());
                if (parent.getRelativePath() != null && !parent.getRelativePath().equals("../pom.xml")) {
                    // TODO ???
                    // pw.println(", '" + model.getParent().getRelativePath() +
                    // "'");
                    p.append(", '" + "../pom.rb" + "'").println();
                } else {
                    p.println();
                }
            }
        }

        void properties(Properties properties) {
            if (!properties.isEmpty()) {
                p.println();
                List<Object> keys = new ArrayList<Object>(properties.keySet());
                p.print("properties ");
                for (int i = 0; i < keys.size(); i++) {
                    Object key = keys.get(i);
                    if (i != 0) {
                        p.append("             ");
                    }
                    Object value = properties.get(key);
                    if (value != null) {
                        if (value instanceof String) {
                            p.append("'" + key + "' => '" + value + "'");
                        } else {
                            p.append("'" + key + "' => " + value);
                        }
                        if (i + 1 != keys.size()) {
                            p.append(",");
                        }
                        p.println();
                    }
                }
            }
        }

        void modules(List<String> modules) {
            if (!modules.isEmpty()) {
                p.println();
                p.print("modules [ ");
                for (int i = 0; i < modules.size(); i++) {
                    String module = modules.get(i);
                    if (i != 0) {
                        p.append("            ");
                    }
                    p.append("'").append(module).append("'");
                    if (i + 1 != modules.size()) {
                        p.append(",").println();
                    }
                }
                p.append(" ]").println();
            }
        }

        void dependencies(List<Dependency> deps) {
            if (!deps.isEmpty()) {
                p.println();
            }
            for (int i = 0; i < deps.size(); i++) {
                Dependency d = deps.get(i);
                p.print("jar");
                if (d.getVersion() != null) {
                    p.append(" '" + d.getGroupId() + ":" + d.getArtifactId()
                            + ":" + d.getVersion());
                } else {
                    //
                    // We are assuming the model is well-formed and that the
                    // parent or dependencyManagement
                    // is providing a version for this particular dependency.
                    //
                    p.append(" '" + d.getGroupId() + ":" + d.getArtifactId());
                }
                if (d.getClassifier() != null) {
                    p.append(":" + d.getClassifier());
                }
                p.append("'");
                boolean nested = print(false, "scope",  d.getScope() == null ? null : ":" + d.getScope());
                nested = print(nested, "optional", d.isOptional());
                if (!d.getExclusions().isEmpty()) {
                    nested = print(nested, "exclusions [");
                    boolean first = true;
                    for (Exclusion e : d.getExclusions()) {
                        if (first) {
                            first = false;
                        } else {
                            p.append(", ");
                        }
                        p.append("'" + e.getGroupId() + ":" + e.getArtifactId()
                                + "'");
                    }
                    p.append("]");
                }
                println(nested);
            }
        }

        private void println(boolean nested) {
            if (nested) {
                p.println();
                p.printEndBlock();
            }
            else {
                p.println();
            }
        }
        
        private boolean print(boolean nested, String val) {
            if (!nested) {
                p.printStartBlock();
            }
            else {
                p.println();
            }
            p.print(val);
            return true;
        }
            
        private boolean print(boolean nested, String prop, boolean val) {
            if(val){
                if (!nested) {
                    p.printStartBlock();
                }
                else {
                    p.println();
                }
                p.print(prop + " true");
                return true;
            }
            return nested;
        }

        private boolean print(boolean nested, String key, String val) {
            if (val != null){
                if (!nested) {
                    p.printStartBlock();
                    nested = true;
                }
                else {
                    p.println();
                }
                p.print(key, val);
            }
            return nested;
        }

        // need to write nested objects
        void plugins(List<Plugin> plugins) {
            if (!plugins.isEmpty()) {
                p.println();
            }
            for (int i = 0; i < plugins.size(); i++) {
                Plugin plugin = plugins.get(i);
                p.print("plugin", plugin.getGroupId() + ":"
                        + plugin.getArtifactId() + ":" + plugin.getVersion());
                if (plugin.getConfiguration() != null) {
                    Xpp3Dom configuration = (Xpp3Dom) plugin.getConfiguration();
                    if (configuration.getChildCount() != 0) {
                        p.append(",");
                        p.println();
                        printHashConfig(INDENT, configuration);
                    }
                }
                boolean nested = print(false, "extensions", plugin.isExtensions());
                //nested = print(nested, "inherited", plugin.isInherited());
                if (!plugin.getExecutions().isEmpty()){
                    for(PluginExecution exec : plugin.getExecutions()){
                        boolean execNested = false;
                        if(exec.getId().equals("default")){
                            nested = print(nested, "execution");
                            //print(false, "id", exec.getId().equals("default") ? null : exec.getId());
                            execNested = print(execNested, "phase", exec.getPhase());
                        }
                        else {
                            nested = print(nested, "execution('" + exec.getId() + 
                                    (exec.getPhase() != null ? "', '" + exec.getPhase() : "") + "')");
                        }
                        //execNested = print(execNested, "inherited", exec.isInherited());
                        if (!exec.getGoals().isEmpty()) {
//                            if(execNested){
//                                execNested = print(execNested, "goals [");
//                            }
//                            else {
//                                p.append(".goals= [");
//                            }
                            execNested = print(execNested, "goals [");
                            boolean first = true;
                            for (String goal : exec.getGoals()) {
                                if (first) {
                                    first = false;
                                } else {
                                    p.append(", ");
                                }
                                p.append("'" + goal + "'");
                            }
                            p.append("]");
                        }
                        println(execNested);
                    }
                }
                println(nested);
            }
        }
        
        private void printListConfig(String indent, Xpp3Dom base) {
            int count = base.getChildCount();
            for (int j = 0; j < count; j++) {
                Xpp3Dom c = base.getChild(j);
                if(j > 0){
                    p.append(",");
                }
                p.println();
                p.append(HASH_INDENT + indent);
                p.append("'" + c.getValue() + "'");
            }
        }
        
        private void printHashConfig(String indent, Xpp3Dom base) {
            int count = base.getChildCount();
            for (int j = 0; j < count; j++) {
                Xpp3Dom c = base.getChild(j);
                p.append(HASH_INDENT + indent);
                if (c.getChildCount() > 0) {
                    if (c.getChildCount() > 1 && c.getChild(0).getName().equals(c.getChild(1).getName())) {
                        p.append(":" + c.getName() + " => {").println();
                        p.append(HASH_INDENT + indent);
                        p.append(INDENT + ":" + c.getChild(0).getName() + " => [");
                        printListConfig(indent + INDENT + INDENT, c);
                        p.println();
                        p.append(HASH_INDENT + indent + INDENT + "]");
                    }
                    else {
                      p.append(":" + c.getName() + " => {").println();
                      printHashConfig(indent + INDENT, c);
                    }
                    p.println();
                    p.append(HASH_INDENT + indent + "}");
                } else if (c.getValue() == null) {
                    p.append(":" + c.getName() + " => true");
                } else {
                    p.append(":" + c.getName() + " => '"
                            + c.getValue() + "'");
                }
                if (j + 1 != count) {
                    p.append(",").println();
                }
            }
        }
    }
}
