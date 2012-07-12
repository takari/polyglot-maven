/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.plugin;

import org.apache.maven.model.Model;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.sonatype.maven.polyglot.execute.ExecuteContext;
import org.sonatype.maven.polyglot.execute.ExecuteManager;
import org.sonatype.maven.polyglot.execute.ExecuteTask;

import java.util.List;

/**
 * Executes registered {@link org.sonatype.maven.polyglot.execute.ExecuteTask}s.
 *
 * @goal execute
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class ExecuteMojo
    extends AbstractMojo
{
    /**
     * @component role="org.sonatype.maven.polyglot.execute.ExecuteManager"
     */
    private ExecuteManager manager;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @parameter expression="${taskId}"
     * @required
     */
    private String taskId;

    public void execute() throws MojoExecutionException, MojoFailureException {
        Log log = getLog();
        Model model = project.getModel();
        log.debug("Executing task '" + taskId + "' for model: " + model.getId());

        assert manager != null;
        List<ExecuteTask> tasks = manager.getTasks(model);

        ExecuteContext ctx = new ExecuteContext()
        {
            public MavenProject getProject() {
                return project;
            }
        };

        for (ExecuteTask task : tasks) {
            if (taskId.equals(task.getId())) {
                log.debug("Executing task: " + task);

                try {
                    task.execute(ctx);
                    return;
                }
                catch (Exception e) {
                    throw new MojoExecutionException(e.getMessage(), e);
                }
            }
        }

        throw new MojoFailureException("Unable to find task for id: " + taskId);
    }
}