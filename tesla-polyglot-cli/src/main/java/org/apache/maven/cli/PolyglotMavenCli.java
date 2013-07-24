/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.apache.maven.cli;

import org.apache.maven.model.building.ModelProcessor;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.classworlds.ClassWorld;
import org.codehaus.plexus.component.composition.CycleDetectedInComponentGraphException;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.ComponentRequirement;

/**
 * Polgyglot-aware Maven CLI.
 * 
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 0.7
 */
public class PolyglotMavenCli extends MavenCli {
  @Override
  protected void customizeContainer(final PlexusContainer container) {
    assert container != null;

    // HACK: Wedge our processor in as the default
    final ComponentDescriptor<?> source = container.getComponentDescriptor(ModelProcessor.class.getName(), "tesla-polyglot");
    final ComponentDescriptor<?> target = container.getComponentDescriptor(ModelProcessor.class.getName(), "default");
    target.setImplementation(source.getImplementation());
    
    // delete the old requirements and replace them with the new
    // with size == 0 is getRequirements is an emptyList which is unmutable
    if (target.getRequirements().size() > 0) {
      target.getRequirements().clear();
    }
    for (final ComponentRequirement requirement : source.getRequirements()) {
      target.addRequirement(requirement);
    }

    // TODO this should not be needed
    final ComponentRequirement manager = new ComponentRequirement();
    manager.setFieldName("manager");
    manager.setRole("org.sonatype.maven.polyglot.PolyglotModelManager");
    manager.setRoleHint("default");
    target.addRequirement(manager);

    try {
      container.addComponentDescriptor(target);
    } catch (final CycleDetectedInComponentGraphException e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(final String[] args) {
    final int result = main(args, null);
    System.exit(result);
  }

  public static int main(final String[] args, final ClassWorld classWorld) {
    assert classWorld != null;
    final PolyglotMavenCli cli = new PolyglotMavenCli();
    return cli.doMain(new CliRequest(args, classWorld));
  }
}
