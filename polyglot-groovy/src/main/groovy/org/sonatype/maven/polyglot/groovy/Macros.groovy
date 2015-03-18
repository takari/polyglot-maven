/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.groovy

/**
 * Default pom macros.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 0.7
 */

def vars = binding.properties.variables

parseArtifact = {String... items ->
    assert items != null && items.size() != 0

    if (items.size() == 1 && items[0].contains(':')) {
        return parseArtifact(items[0].split(':'))
    }

    def map = [:]

    switch (items.size()) {
        case 5:
            map.groupId = items[0]
            map.artifactId = items[1]
            map.type = items[2]
            map.classifier = items[3]
            map.version = items[4]
            break

        case 4:
            map.groupId = items[0]
            map.artifactId = items[1]
            map.classifier = items[2]
            map.version = items[3]
            break

        case 3:
            map.groupId = items[0]
            map.artifactId = items[1]
            map.version = items[2]
            break

        case 2:
            map.groupId = items[0]
            map.artifactId = items[1]
            break

        default:
            throw new IllegalArgumentException("Unable to parse artifact for: $items")
    }

    return map
}


vars.'$artifact' = {builder, String... items ->
    def map = parseArtifact(items)

    map.each {
        builder."${it.key}" it.value
    }
}

vars.'$configuration' = {builder, Map items ->
    builder.configuration {
        for (item in items) {
            "${item.key}"(item.value)
        }
    }
}