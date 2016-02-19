/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.xml;

import java.io.FileInputStream;
import java.util.Map;

import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.maven.polyglot.mapping.Mapping;
import org.sonatype.maven.polyglot.mapping.MappingSupport;
import org.sonatype.maven.polyglot.xml.xpp3.MavenXpp3Reader;

/**
 * XML model mapping.
 *
 */
@Component(role = Mapping.class, hint = "xml41")
public class XMLMapping extends MappingSupport {
	
	public XMLMapping() {
		super("xml41");
		setPomNames("pom.xml41");
		setAcceptLocationExtensions(".xml41", ".xml");
		setAcceptOptionKeys("xml41:4.0.0");
		setPriority(-1);
	}

	@Override
	public boolean accept(Map<String, ?> options) {
		if (options != null) {			

			String location = getLocation(options);
			if (location != null) {
				for (String ext : getAcceptLocationExtensions()) {
					if (location.endsWith(ext)) {
						return canParse(options);
					}
				}
			}
		}

		return false;
	}
	
	private boolean canParse(Map<String, ?> options) {
		try {
			MavenXpp3Reader reader = new MavenXpp3Reader();
			reader.read(new FileInputStream(getLocation(options)));
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
}