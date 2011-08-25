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

package org.sonatype.maven.polyglot.mapping;

import org.codehaus.plexus.component.annotations.Component;

/**
 * Xml model mapping.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 0.7
 */
@Component(role=Mapping.class, hint="xml")
public class XmlMapping
    extends MappingSupport
{
    public XmlMapping() {
        super(null);
        setPomNames("pom.xml");
        setAcceptLocationExtensions(".xml", ".pom");
        setAcceptOptionKeys("xml:4.0.0");
    }
}