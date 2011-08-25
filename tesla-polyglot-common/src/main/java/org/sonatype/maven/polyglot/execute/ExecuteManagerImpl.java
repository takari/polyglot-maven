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
@Component(role=ExecuteManager.class, instantiationStrategy="singleton")
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
            log.debug("Registered tasks for: " + model.getId() + "=" + tasks);
        }
    }

    public List<ExecuteTask> getTasks(final Model model) {
        assert model != null;

        List<ExecuteTask> tasks = modelTasks.get(model.getId());
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
        plugin.setGroupId("org.sonatype.pmaven");
        plugin.setArtifactId("pmaven-maven-plugin");
        plugin.setVersion("0.8-SNAPSHOT");
        model.getBuild().addPlugin(plugin);

        List<String> goals = Collections.singletonList("execute");

        for (ExecuteTask task : tasks) {
            if (log.isDebugEnabled()) {
                log.debug("Registering task: " + task);
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