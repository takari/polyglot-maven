package org.sonatype.maven.polyglot.kotlin.engine

import org.apache.maven.model.Model
import org.apache.maven.project.MavenProject
import org.codehaus.plexus.util.xml.Xpp3DomBuilder
import org.sonatype.maven.polyglot.execute.ExecuteContext
import org.sonatype.maven.polyglot.kotlin.dsl.DSL
import kotlin.script.experimental.api.ScriptAcceptedLocation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.acceptedLocations
import kotlin.script.experimental.api.ide
import kotlin.script.experimental.api.implicitReceivers
import kotlin.script.experimental.jvm.dependenciesFromClassContext
import kotlin.script.experimental.jvm.jvm

object PomKtsScriptDefinition : ScriptCompilationConfiguration(
        {
            implicitReceivers(DSL::class)
            jvm {
                dependenciesFromClassContext(DSL::class, "polyglot-kotlin") // needed for DSL
                dependenciesFromClassContext(ExecuteContext::class, "polyglot-common") // needed for executing tasks
                dependenciesFromClassContext(Model::class, "maven-model") // needed for maven model
                dependenciesFromClassContext(MavenProject::class, "maven-core") // needed for maven project/session
                dependenciesFromClassContext(Xpp3DomBuilder::class, "plexus-utils") // needed for Xpp3DomBuilder
            }
            ide {
                acceptedLocations(ScriptAcceptedLocation.Everywhere)
            }
        })
