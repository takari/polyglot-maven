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
@Component(role=ClassRealmManagerDelegate.class, hint="polyglot")
public class PolyglotRealmDelegate
    implements ClassRealmManagerDelegate
{
    public void setupRealm(final ClassRealm realm, final ClassRealmRequest request) {
        assert realm != null;
        if (!ClassRealmRequest.RealmType.Project.equals(request.getType())) {
            try {
                realm.importFrom("plexus.core", "org.sonatype.maven.polyglot.execute");
            } catch (Exception e) {
                throw new IllegalStateException("Could not import Polyglot extensions", e);
            }
        }
    }
}
