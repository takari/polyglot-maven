package org.sonatype.maven.polyglot.kotlin.execute

import org.sonatype.maven.polyglot.execute.ExecuteContext
import java.io.File

class TaskContext(val script: File, private val delegate: ExecuteContext) : ExecuteContext by delegate
