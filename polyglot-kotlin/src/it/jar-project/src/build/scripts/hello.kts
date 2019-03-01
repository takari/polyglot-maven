BuildContext(bindings).apply {
    val keys = bindings.keys as Collection<String>
    """
        ------------------------------------------------------------------------

        HELLO ${project.name}!!!

        Folder:   ${basedir}
        Script:   .${script.path.substringAfter(basedir.path)}
        Bindings: ${keys}

        ------------------------------------------------------------------------
    """.trimIndent().lines().forEach { log.info(it) }
}