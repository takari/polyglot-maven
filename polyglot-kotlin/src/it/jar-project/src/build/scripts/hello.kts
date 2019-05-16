println("""
------------------------------------------------------------------------

HELLO ${project.name}!!!

Folder:   ${project.projectDirectory}
Script:   .${script.path.substringAfter(project.projectDirectory.path)}

------------------------------------------------------------------------
""")
