/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package io.takari.maven.polyglot;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.sonatype.maven.polyglot.TeslaModelTranslator;

/**
 * Polgyglot model translator Mojo.
 * 
 * @author Jason van Zyl
 */
@Mojo(name = "translate", requiresProject = false)
public class TranslatorMojo extends AbstractMojo {

  @Component
  private TeslaModelTranslator translator;

  @Parameter(required = true, property = "input")
  private File input;

  @Parameter(required = true, property = "output")
  private File output;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    getLog().info(String.format("Translating %s -> %s", input, output));
    try {
      translate(input, output);
    } catch (IOException e) {
      throw new MojoExecutionException(String.format("Error translating %s -> %s", input, output), e);
    }
  }

  public void translate(final File input, final File output) throws IOException {
    translate(input.toURI().toURL(), output.toURI().toURL());
  }

  public void translate(final URL input, final URL output) throws IOException {
    translator.translate(input, output);
  }
}