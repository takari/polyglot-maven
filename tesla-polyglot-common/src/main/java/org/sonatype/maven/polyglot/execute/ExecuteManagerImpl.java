/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.execute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.sonatype.maven.polyglot.PolyglotModelManager;

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
    private static final String TESLA_POLYGLOT = "tesla-polyglot-";

    // FIXME do not hardcode the version
    private static final String VERSION = "0.0.8";

    private static final String IO_TESLA_POLYGLOT = "io.tesla.polyglot";

    @Requirement
    protected Logger log;

    @Requirement
    protected PolyglotModelManager manager;
    
    private final Map<String,List<ExecuteTask>> modelTasks = new HashMap<String,List<ExecuteTask>>();

    public void register(final Model model, final List<ExecuteTask> tasks) {
        assert model != null;
        assert tasks != null;

        // Need to copy the contents to avoid the elements
        List<ExecuteTask> copy = new ArrayList<ExecuteTask>(tasks.size());
        copy.addAll(tasks);
        modelTasks.put(model.getId(), Collections.unmodifiableList(copy));
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
    
    //@Override
    @Deprecated
    public void install(final Model model) {
        install( model, new HashMap<String, String>() );
    }
    
    //@Override
    public void install(final Model model, final Map<String, ?> options ) {
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

        Plugin plugin = new Plugin();
        plugin.setGroupId(IO_TESLA_POLYGLOT);
        plugin.setArtifactId(TESLA_POLYGLOT + "maven-plugin");

        // FIMXE: Should not need to hard-code the version here
        plugin.setVersion(VERSION);
        
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
            if ( id != null && id.length() > 0 ){
                execution.setId(id);
            }
            execution.setPhase(task.getPhase());
            execution.setGoals(goals);

            Xpp3Dom config = new Xpp3Dom("configuration");
            execution.setConfiguration(config);

            Xpp3Dom child = new Xpp3Dom("taskId");
            child.setValue( id != null && id.length() > 0 ? id : "default" );
            config.addChild(child);

            if ( model.getPomFile() != null ) {
                Xpp3Dom nativePom = new Xpp3Dom("nativePom");
                nativePom.setValue( model.getPomFile().getName() );
                config.addChild(nativePom);
            }

            plugin.addExecution(execution);
        }

        try {
            String flavour = manager.getFlavourFor( options );
            Dependency dep = new Dependency();
            dep.setGroupId( IO_TESLA_POLYGLOT );
            dep.setArtifactId( TESLA_POLYGLOT + flavour );
            dep.setVersion( VERSION );
            plugin.addDependency( dep );
        }
        catch( RuntimeException e ){
            e.printStackTrace();
            // ignore for the time being
        }
    }
}