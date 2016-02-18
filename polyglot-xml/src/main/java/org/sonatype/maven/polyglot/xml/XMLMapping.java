/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.xml;

import java.util.Map;

import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.maven.polyglot.mapping.Mapping;
import org.sonatype.maven.polyglot.mapping.MappingSupport;

/**
 * XML model mapping.
 *
 */
@Component(role = Mapping.class, hint = "xml41")
public class XMLMapping extends MappingSupport {
	public XMLMapping() {
		super("xml41");
		setPomNames("pom.xml41");
		setAcceptLocationExtensions(".xml41");
		setAcceptOptionKeys("xml41:4.0.0");
		setPriority(2);
	}
}