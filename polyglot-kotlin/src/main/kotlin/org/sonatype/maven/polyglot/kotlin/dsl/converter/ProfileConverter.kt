
import BuildConverter.buildOf
import DependencyConverter.dependenciesOf
import org.apache.maven.model.ActivationFile
import org.apache.maven.model.ActivationOS
import org.apache.maven.model.ActivationProperty
import org.apache.maven.model.Profile

object ProfileConverter {
    fun profilesOf(profiles: Profiles): List<Profile>? {
        val (profilesList) = profiles
        return profilesList.map {
            Profile().apply {
                id = it.id
                val (profileBuild, profileActivation, profileDeps) = it
                if (profileBuild != null) build = buildOf(profileBuild)

                if (profileActivation != null) activation = activationOf(profileActivation)
                if (profileDeps != null) dependencies = dependenciesOf(profileDeps.component1().map { MetaDependency(it) })
            }
        }
    }

    private fun activationOf(profileActivation: Activation): org.apache.maven.model.Activation {
        return org.apache.maven.model.Activation().apply {
            isActiveByDefault = profileActivation.default
            jdk = profileActivation.jdk
            val (thisFile, thisOs, prop) = profileActivation

            if (prop != null) property = ActivationProperty().apply {
                name = prop.first
                value = prop.second
            }
            if (thisFile != null) file = ActivationFile().apply {
                exists = thisFile.exists
                missing = thisFile.missing
            }
            if (thisOs != null) os = ActivationOS().apply {
                name = thisOs.name
                arch = thisOs.arch
                family = thisOs.family
                version = thisOs.version
            }
        }
    }
}