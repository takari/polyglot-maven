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

package org.sonatype.maven.polyglot;

import org.apache.maven.model.io.ModelReader;
import org.apache.maven.model.io.ModelWriter;
import org.apache.maven.model.locator.ModelLocator;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.sonatype.maven.polyglot.mapping.Mapping;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Manages the mapping for polyglot model support.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 0.7
 */
@Component(role=PolyglotModelManager.class)
public class PolyglotModelManager
    implements ModelLocator
{
    @Requirement
    protected Logger log;
    
    @Requirement(role=Mapping.class)
    private List<Mapping> mappings;

    public void addMapping(final Mapping mapping) {
        assert mapping != null;
        mappings.add(mapping);
    }

    public ModelReader getReaderFor(final Map<String, ?> options) {
        for (Mapping mapping : mappings) {
            if (mapping.accept(options)) {
                return mapping.getReader();
            }
        }

        throw new RuntimeException("Unable determine model input format; options=" + options);
    }

    public ModelWriter getWriterFor(final Map<String, ?> options) {
        for (Mapping mapping : mappings) {
            if (mapping.accept(options)) {
                return mapping.getWriter();
            }
        }

        throw new RuntimeException("Unable determine model output format; options=" + options);
    }

    public File locatePom(final File dir) {
        assert dir != null;

        File pomFile = null;
        float mappingPriority = Float.MIN_VALUE;
        for ( Mapping mapping : mappings )
        {
            File file = mapping.locatePom( dir );
            if ( file != null && ( pomFile == null || mappingPriority < mapping.getPriority() ) )
            {
                pomFile = file;
                mappingPriority = mapping.getPriority();
            }
        }

        return pomFile;
    }
}
