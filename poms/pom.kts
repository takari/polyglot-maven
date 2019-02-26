/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
project("Polyglot Tesla :: Aggregator") {

    id = "io.tesla.polyglot:tesla-polyglot:pom:0.0.1-SNAPSHOT"

    parent = "io.tesla:tesla:4"

    modules(
        "tesla-polyglot-common",
        "tesla-polyglot-atom",
        "tesla-polyglot-ruby",
        "tesla-polyglot-groovy",
        "tesla-polyglot-yaml",
        "tesla-polyglot-clojure",
        "tesla-polyglot-scala",
        "tesla-polyglot-cli",
        "tesla-polyglot-maven-plugin"
    )

    properties {
        "sisuInjectVersion" to "0.0.0.M2a"
        "teslaVersion" to "3.1.0"
    }

    dependencyManagement {
        dependencies {
            dependency("org.eclipse.sisu:org.eclipse.sisu.inject:${sisuInjectVersion}")
            dependency("org.eclipse.sisu:org.eclipse.sisu.plexus:${sisuInjectVersion}")
            dependency("org.apache.maven:maven-model-builder:3.5.0")
            dependency("org.apache.maven:maven-embedder:3.5.0")
            dependency("junit:junit:4.12:test")
        }
    }

    build {
        //
        // Arbitrary Groovy code can be executed in any phase in the form of a dynamic plugin
        //
        execute(id: "hello", phase: "validate") {
            println """
                    hello, I am Kotlin inside Maven.
                    """
        }

        plugins {
            plugin("org.codehaus.plexus:plexus-component-metadata:1.5.4") {
                executions {
                    execution(goals: listOf("generate-metadata", "generate-test-metadata"))
                }
            }
        }
    }
}