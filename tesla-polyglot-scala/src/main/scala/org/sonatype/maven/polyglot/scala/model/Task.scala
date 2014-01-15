/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.scala.model

import org.sonatype.maven.polyglot.execute.ExecuteContext

class Task(val id: String, val phase: String, val profileId: String, val block: ExecuteContext => Unit)

object Task {
  def apply(id: String, phase: String)(block: ExecuteContext => Unit) =
    new Task(id, phase, null, block)
  def apply(id: String, phase: String, profileId: String)(block: ExecuteContext => Unit) =
    new Task(id, phase, profileId, block)
}


import org.sonatype.maven.polyglot.scala.ScalaPrettyPrinter._

class PrettiedTask(t: Task) {
  def asDoc: Doc =
    "Task" <> lparen <>
      assignString("id", t.id) <> comma <+>
      assignString("phase", t.phase) <> rparen <+>
      "{compiled code}"
}
