/**
 * Copyright (c) 2012 to original author or authors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.sonatype.maven.polyglot.scala.model

import org.sonatype.maven.polyglot.scala.ScalaPrettyPrinter._

class PrettiedProperties(p: Map[String, String]) {
  def asDoc: Doc = mapStrings(p)
}
