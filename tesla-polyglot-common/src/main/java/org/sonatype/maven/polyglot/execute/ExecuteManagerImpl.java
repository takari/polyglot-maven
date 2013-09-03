/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.execute;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of the {@link ExecuteManager} component.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 0.7
 */
@Component(role=ExecuteManager.class)
public class ExecuteManagerImpl
    implements ExecuteManager
{
    @Requirement
    protected Logger log;

    private final Map<String,List<ExecuteTask>> modelTasks = new HashMap<String,List<ExecuteTask>>();

    public void register(final Model model, final List<ExecuteTask> tasks) {
        assert model != null;
        assert tasks != null;

        // Need to copy the contents to avoid the elements
        List<ExecuteTask> copy = new ArrayList<ExecuteTask>(tasks.size());
        copy.addAll(tasks);
        modelTasks.put(model.getId(), Collections.unmodifiableList(copy));

        if (log.isDebugEnabled()) {
          log.debug("!!!!!! Registered tasks for: " + model.getId() + "=" + tasks);
        }
    }

    public List<ExecuteTask> getTasks(final Model model) {
        assert model != null;

        List<ExecuteTask> tasks = modelTasks.get(model.getId());

        // If we cannot find the model then look for the model where it has been registered with less
        // specificity.
        if (tasks == null) {
            Model inheritingModel = new Model();
            inheritingModel.setArtifactId(model.getArtifactId());
            inheritingModel.setPackaging(model.getPackaging());
            inheritingModel.setVersion(model.getVersion());
            tasks = modelTasks.get(inheritingModel.getId());
        }
        if (tasks == null) {
            Model inheritingModel = new Model();
            inheritingModel.setArtifactId(model.getArtifactId());
            inheritingModel.setGroupId(model.getGroupId());
            inheritingModel.setPackaging(model.getPackaging());
            tasks = modelTasks.get(inheritingModel.getId());
        }
        if (tasks == null) {
            Model inheritingModel = new Model();
            inheritingModel.setArtifactId(model.getArtifactId());
            inheritingModel.setPackaging(model.getPackaging());
            tasks = modelTasks.get(inheritingModel.getId());
        }

        // Well, we've tried our hardest...
        if (tasks == null) {
            return Collections.emptyList();
        }

        return tasks;
    }

    public void install(final Model model) {
        assert model != null;

        List<ExecuteTask> tasks = getTasks(model);
        if (tasks.isEmpty()) {
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("Registering tasks for: " + model.getId());
        }

        if (model.getBuild() == null) {
            model.setBuild(new Build());
        }

        // FIMXE: Should not need to hard-code the version here
        Plugin plugin = new Plugin();
        plugin.setGroupId("io.tesla.polyglot");
        plugin.setArtifactId("tesla-polyglot-maven-plugin");
        plugin.setVersion("0.0.1-SNAPSHOT");
        // Do not assume that the existing list is mutable.
        List<Plugin> existingPlugins = model.getBuild().getPlugins();
        List<Plugin> plugins = new ArrayList<Plugin>(existingPlugins);
        model.getBuild().setPlugins(plugins);
        model.getBuild().addPlugin(plugin);

        List<String> goals = Collections.singletonList("execute");

        for (ExecuteTask task : tasks) {
            if (log.isDebugEnabled()) {
                log.debug("Registering task: " + task.getId());
            }

            String id = task.getId();

            PluginExecution execution = new PluginExecution();
            execution.setId(id);
            execution.setPhase(task.getPhase());
            execution.setGoals(goals);

            Xpp3Dom config = new Xpp3Dom("configuration");
            execution.setConfiguration(config);

            Xpp3Dom child = new Xpp3Dom("taskId");
            child.setValue(id);
            config.addChild(child);

            plugin.addExecution(execution);
        }
    }
}