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

import org.apache.maven.model.Model;
import org.apache.maven.model.building.ModelProcessor;
import org.apache.maven.model.io.ModelParseException;
import org.apache.maven.model.io.ModelReader;
import org.apache.maven.model.io.ModelWriter;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Translates between polyglot model formats.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 0.7
 */
@Component(role=PolyglotModelTranslator.class)
public class PolyglotModelTranslator
{
    @Requirement
    protected Logger log;
    
    @Requirement
    private PolyglotModelManager manager;

    public void translate(final File input, final File output) throws IOException, ModelParseException {
        assert input != null;
        assert output != null;

        Map<String,Object> inputOptions = new HashMap<String,Object>();
        inputOptions.put(ModelProcessor.SOURCE, input);

        Map<String,Object> outputOptions = new HashMap<String,Object>();
        outputOptions.put(ModelProcessor.SOURCE, output);

        translate(input, inputOptions, output, outputOptions);
    }

    @SuppressWarnings({"unchecked"})
    public void translate(final File input, final Map<String,?> inputOptions, final File output, final Map<String,?> outputOptions) throws IOException, ModelParseException {
        assert input != null;
        assert output != null;

        ModelReader reader = manager.getReaderFor(inputOptions);
        Model model = reader.read(input, inputOptions);

        ModelWriter writer = manager.getWriterFor(outputOptions);
        writer.write(output, (Map<String, Object>) outputOptions, model);
    }

    public void translate(final URL input, final URL output) throws IOException, ModelParseException {
        assert input != null;
        assert output != null;

        Map<String,Object> inputOptions = new HashMap<String,Object>();
        inputOptions.put(ModelProcessor.SOURCE, input);

        Map<String,Object> outputOptions = new HashMap<String,Object>();
        outputOptions.put(ModelProcessor.SOURCE, output);

        translate(input, inputOptions, output, outputOptions);
    }

    public void translate(final URL input, final Map<String,?> inputOptions, final URL output, final Map<String,?> outputOptions) throws IOException, ModelParseException {
        assert input != null;
        assert output != null;

        OutputStream out;
        if (output.getProtocol().equals("file")) {
            File file = new File(output.getPath());
            out = new BufferedOutputStream(new FileOutputStream(file));
        }
        else {
            out = output.openConnection().getOutputStream();
        }

        try {
            translate(input.openStream(), inputOptions, out, outputOptions);
        }
        finally {
            out.flush();
        }
    }

    @SuppressWarnings({"unchecked"})
    public void translate(final InputStream input, final Map<String,?> inputOptions, final OutputStream output, final Map<String,?> outputOptions) throws IOException, ModelParseException {
        assert input != null;
        assert output != null;

        ModelReader reader = manager.getReaderFor(inputOptions);
        Model model = reader.read(input, inputOptions);

        ModelWriter writer = manager.getWriterFor(outputOptions);
        writer.write(output, (Map<String, Object>) outputOptions, model);
    }

    @SuppressWarnings({"unchecked"})
    public void translate(final Reader input, final Map<String,?> inputOptions, final Writer output, final Map<String,?> outputOptions) throws IOException, ModelParseException {
        assert input != null;
        assert output != null;

        ModelReader reader = manager.getReaderFor(inputOptions);
        Model model = reader.read(input, inputOptions);

        ModelWriter writer = manager.getWriterFor(outputOptions);
        writer.write(output, (Map<String, Object>) outputOptions, model);
    }
}