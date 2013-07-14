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

import org.apache.maven.model.building.ModelProcessor;
import org.apache.maven.model.building.ModelSource;

import java.io.File;
import java.net.URL;
import java.util.Map;

/**
 * Support for models.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 0.7
 */
public class PolyglotModelUtil
{
    /**
     * Gets the location, as configured via the {@link ModelProcessor#SOURCE} key.
     *
     * Supports values of:
     * <ul>
     * <li>String
     * <li>File
     * <li>URL
     * <li>ModelSource
     * </ul>
     *
     * @return  The model location; or null.
     */
    public static String getLocation(final Map<?, ?> options) {
        if (options != null) {
            Object tmp = options.get(ModelProcessor.SOURCE);
            if (tmp instanceof String) {
                return (String)tmp;
            }
            else if (tmp instanceof URL) {
                return ((URL)tmp).toExternalForm();
            }
            else if (tmp instanceof File) {
                return ((File)tmp).getAbsolutePath();
            }
            else if (tmp instanceof ModelSource) {
                return ((ModelSource)tmp).getLocation();
            }
        }
        return null;
    }
}