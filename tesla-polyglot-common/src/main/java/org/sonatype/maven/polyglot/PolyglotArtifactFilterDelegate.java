/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot;

import org.apache.maven.ArtifactFilterManagerDelegate;
import org.codehaus.plexus.component.annotations.Component;

import java.util.Set;

/**
 * Additional filter required for using {@link org.sonatype.maven.polyglot.execute} bits from plugins.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 0.7
 */
@Component(role=ArtifactFilterManagerDelegate.class, hint="tesla-polyglot")
public class PolyglotArtifactFilterDelegate
    implements ArtifactFilterManagerDelegate
{
    public void addExcludes(final Set<String> strings) {
        assert strings != null;
        strings.add("tesla-polyglot-common");
    }

    public void addCoreExcludes(final Set<String> strings) {
        assert strings != null;
        strings.add("tesla-polyglot-common");
    }
}
