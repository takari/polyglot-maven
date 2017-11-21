/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.ModelParseException;
import org.apache.maven.model.io.ModelReader;
import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.maven.polyglot.io.ModelReaderSupport;
import org.sonatype.maven.polyglot.java.compiler.CompilerUtils;
import org.sonatype.maven.polyglot.java.dsl.ModelTemplate;

/**
 * Java model reader.
 *
 */
@Component(role = ModelReader.class, hint = "java")
public class JavaModelReader extends ModelReaderSupport {

	public JavaModelReader() {
	}
	
	@Override
	public Model read(Reader reader, Map<String, ?> options) throws IOException, ModelParseException {

		System.out.println("@@@" + System.getProperty("java.class.path") + "%%%");
		
		String packagename = "org.sonatype.maven.polyglot.java.test";
		String className = "ModelTest3";
		String javaCode = readFile(reader);

		ModelTemplate template = null;
		try {
			Class aClass = CompilerUtils.CACHED_COMPILER.loadFromJava(className, javaCode);
			template = (ModelTemplate)aClass.newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException 
				| SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return template.getModel();
	}

	private String readFile(Reader fileReader) {
		StringBuilder content = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(fileReader);
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line);
			}
			reader.close();
			return content.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
