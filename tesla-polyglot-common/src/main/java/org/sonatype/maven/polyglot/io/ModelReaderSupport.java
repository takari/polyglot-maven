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

package org.sonatype.maven.polyglot.io;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.ModelParseException;
import org.apache.maven.model.io.ModelReader;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.IOUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

/**
 * Support for {@link ModelReader} implementations.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 0.7
 */
public abstract class ModelReaderSupport
    implements ModelReader
{
    public Model read(final File input, final  Map<String,?> options) throws IOException {
        Model model;

        Reader reader = new BufferedReader(new FileReader(input));
        try {
            model = read(reader, options);
            model.setPomFile(input);
        }
        finally {
            IOUtil.close(reader);
        }
        return model;
    }

    public Model read(final InputStream input, final Map<String,?> options) throws IOException {
        return read(new InputStreamReader(input), options);
    }
}