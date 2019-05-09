package org.sonatype.maven.polyglot.kotlin.support

import org.codehaus.plexus.classworlds.realm.ClassRealm
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.net.URI
import java.net.URLClassLoader

internal fun classpathFromClassLoader(classLoader: ClassLoader): List<File> {
    return when (classLoader) {
        is ClassRealm -> return classpathFromClassRealm(classLoader)
        is URLClassLoader -> return classpathFromURLClassLoader(classLoader)
        else -> emptyList()
    }
}

private fun classpathFromURLClassLoader(classLoader: URLClassLoader): List<File> {
    return classLoader.urLs
            .map { url -> url.toURI() }
            .map { uri -> File(uri) }
}

private fun classpathFromClassRealm(classRealm: ClassRealm): List<File> {
    val classpath: MutableList<File> = ArrayList()
    val bytes = ByteArrayOutputStream()
    val out = PrintStream(bytes)
    classRealm.display(out)
    classpath.addAll(
            kotlin.text.Regex("""urls\[\d+]\s*=\s*(file:.*)""")
                    .findAll(bytes.toString())
                    .map { matchResult -> matchResult.groupValues[1] }
                    .map { value -> URI(value) }
                    .map { uri -> File(uri) }
    )
    return classpath
}
