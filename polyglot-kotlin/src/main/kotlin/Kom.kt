typealias project = Project
data class Project(val artifactId: String,
                   val name: String = artifactId,
                   val parent: String,
                   val packaging: String = jar,
                   val properties: Any,
                   val dependencies: Any
)
val compile = fun (list: ()-> Any ){}
val test = fun (list: ()-> Any ){}
val provided = fun (list: ()-> Any ){}
val jar = "jar"