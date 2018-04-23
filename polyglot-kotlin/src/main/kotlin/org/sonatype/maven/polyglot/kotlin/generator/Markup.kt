package org.sonatype.maven.polyglot.kotlin.generator

val nextLine = System.lineSeparator()

val tab = "    "
fun tab(text: String = "", count: Int = 1): String = tab.repeat(count) + text

fun String.quoted(): String = "\"$this\""

