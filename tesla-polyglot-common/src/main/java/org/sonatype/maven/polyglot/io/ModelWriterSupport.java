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
import org.apache.maven.model.io.ModelWriter;
import org.codehaus.plexus.util.IOUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

/**
 * Support for {@link ModelWriter} implementations.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 0.7
 */
public abstract class ModelWriterSupport
    implements ModelWriter
{
    public void write(final File file, final Map<String,Object> options, final Model model) throws IOException {
        assert file != null;
        assert model != null;

        OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        try {
            write(out, options, model);
            out.flush();
        }
        finally {
            IOUtil.close(out);
        }
    }

    public void write(final OutputStream output, final Map<String,Object> options, final Model model) throws IOException {
        assert output != null;
        assert model != null;

        write(new OutputStreamWriter(output), options, model);
    }    
}