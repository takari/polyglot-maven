import org.codehaus.plexus.component.annotations.Component
import org.codehaus.plexus.component.annotations.Requirement
import org.codehaus.plexus.logging.Logger
import org.sonatype.maven.polyglot.mapping.Mapping
import org.sonatype.maven.polyglot.mapping.MappingSupport

@Component(role = Mapping::class, hint = "kotlin")
class KotlinMapping: MappingSupport("kotlin") {

    @Requirement protected var log: Logger? = null

    init {
        setPomNames("pom.kts")
        setAcceptLocationExtensions(".kts")
        setAcceptOptionKeys("kts:4.0.0")
        priority = 1f
    }
}