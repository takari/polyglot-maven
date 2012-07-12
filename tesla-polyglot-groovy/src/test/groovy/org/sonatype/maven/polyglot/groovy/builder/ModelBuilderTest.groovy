/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.groovy.builder

import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*
import org.sonatype.maven.polyglot.groovy.GroovyModelTestSupport

/**
 * Tests for {@link ModelBuilder}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class ModelBuilderTest
    extends GroovyModelTestSupport
{
    private ModelBuilder builder

    @Before
    void setUp() {
        builder = lookup(ModelBuilder.class)
    }

    @Test
    void testBuildWithElements() {
        def model = builder.project {
            parent {
                groupId 'a'
                artifactId 'b'
                version 'c'
            }

            dependencies {
                dependency {
                    groupId 'a'
                    artifactId 'b'
                    version 'c'
                }
            }
        }

        assertNotNull(model)

        def p = model.parent
        assertNotNull(p);

        assertEquals('a', p.groupId)
        assertEquals('b', p.artifactId)
        assertEquals('c', p.version)

        assertNotNull(model.dependencies)
        assertEquals(1, model.dependencies.size())

        def d = model.dependencies[0]
        assertEquals('a', d.groupId)
        assertEquals('b', d.artifactId)
        assertEquals('c', d.version)
    }

    @Test
    void testBuildWithAttributes() {
        def model = builder.project {
            parent(groupId: 'a', artifactId :'b', version: 'c')

            dependencies {
                dependency(groupId: 'a', artifactId: 'b', version: 'c')
            }
        }

        assertNotNull(model)

        def p = model.parent
        assertNotNull(p);

        assertEquals('a', p.groupId)
        assertEquals('b', p.artifactId)
        assertEquals('c', p.version)

        assertNotNull(model.dependencies)
        assertEquals(1, model.dependencies.size())

        def d = model.dependencies[0]
        assertEquals('a', d.groupId)
        assertEquals('b', d.artifactId)
        assertEquals('c', d.version)
    }

    @Test
    void testBuildWithConfiguration() {
        def model = builder.project {
            build {
                plugins {
                    plugin {
                        configuration {
                            foo 'a'
                            bar """
                                blah
                            """
                        }
                    }
                }
            }
        }

        assertNotNull(model)
    }

    @Test
    void testBuildWithProperties() {
        def model = builder.project {
            properties {
                foo {
                    bar {
                        a "b"
                    }
                }

                'foo.bar.c' "d"

                ick(poo: "blah") {
                    grr "barf"
                }

                v "x"

                x = "y"
            }
        }

        assertNotNull(model)
        def p = model?.properties

        assertNotNull(p)
        assertEquals("b", p.getProperty("foo.bar.a"))
        assertEquals("d", p.getProperty("foo.bar.c"))
        assertEquals("blah", p.getProperty("ick.poo"))
        assertEquals("barf", p.getProperty("ick.grr"))
        assertEquals("x", p.getProperty("v"))
        assertEquals("y", p.getProperty("x"))
    }

    @Test
    void testBuildParseParent() {
        def model = builder.project {
            parent("a:b:c")
        }

        assertNotNull(model)

        def p = model.parent
        assertNotNull(p);

        assertEquals('a', p.groupId)
        assertEquals('b', p.artifactId)
        assertEquals('c', p.version)
    }

    @Test
    void testBuildParseModules() {
        def model = builder.project {
            modules('a', 'b', 'c')
        }

        assertNotNull(model)

        def m = model?.modules
        assertNotNull(m)
        assertEquals(3, m.size())
        assertEquals([ 'a', 'b', 'c' ], m)
    }

    @Test
    void testBuildParseGoals1() {
        def model = builder.project {
            build {
                plugins {
                    plugin {
                        executions {
                            execution {
                                goals("foo")
                            }
                        }
                    }
                }
            }
        }

        assertNotNull(model)

        def g = model?.build?.plugins[0]?.executions[0].goals
        assertNotNull(g)
        assertEquals(1, g.size())
        assertEquals("foo", g[0])
    }

    @Test
    void testBuildParseGoals2() {
        def model = builder.project {
            build {
                plugins {
                    plugin {
                        executions {
                            execution {
                                goals("foo", "bar", "baz")
                            }
                        }
                    }
                }
            }
        }

        assertNotNull(model)

        def g = model?.build?.plugins[0]?.executions[0].goals
        assertNotNull(g)
        assertEquals(3, g.size())
        assertEquals(["foo", "bar", "baz"], g)
    }

    @Test
    void testBuildParseExclusions1() {
        def model = builder.project {
            dependencies {
                dependency {
                    exclusions("foo:bar")
                }
            }
        }

        assertNotNull(model)

        def e = model?.dependencies[0]?.exclusions
        assertNotNull(e)
        assertEquals(1, e.size())
        assertEquals("foo", e[0].groupId)
        assertEquals("bar", e[0].artifactId)
    }

    @Test
    void testBuildParseExclusions2() {
        def model = builder.project {
            dependencies {
                dependency {
                    exclusions("foo:bar", "a:b")
                }
            }
        }

        assertNotNull(model)

        def e = model?.dependencies[0]?.exclusions
        assertNotNull(e)
        assertEquals(2, e.size())
        assertEquals("foo", e[0].groupId)
        assertEquals("bar", e[0].artifactId)
        assertEquals("a", e[1].groupId)
        assertEquals("b", e[1].artifactId)
    }
}
