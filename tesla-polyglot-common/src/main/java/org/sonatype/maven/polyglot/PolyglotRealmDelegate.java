/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot;

import org.apache.maven.classrealm.ClassRealmManagerDelegate;
import org.apache.maven.classrealm.ClassRealmRequest;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.component.annotations.Component;

/**
 * Additional class realm setup required for using {@link org.sonatype.maven.polyglot.execute} bits from plugins.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 0.7
 */
@Component(role=ClassRealmManagerDelegate.class, hint="tesla-polyglot")
public class PolyglotRealmDelegate
    implements ClassRealmManagerDelegate
{
    public void setupRealm(final ClassRealm realm, final ClassRealmRequest request) {
        assert realm != null;
        if (!ClassRealmRequest.RealmType.Project.equals(request.getType())) {
            try {
                realm.importFrom("plexus.core", "org.sonatype.maven.polyglot.execute");
                // the plugin also need PolyglotModelManager
                realm.importFrom("plexus.core", "org.sonatype.maven.polyglot");
            } catch (Exception e) {
                throw new IllegalStateException("Could not import Polyglot extensions", e);
            }
        }
    }
}
