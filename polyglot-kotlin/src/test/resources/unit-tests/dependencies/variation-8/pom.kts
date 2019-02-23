project {
    profiles {
        profile {
            id("other")
            activation {
                activeByDefault()
            }
            dependencies {
                compile("org.springframework.boot:spring-boot-starter:2.1.2.RELEASE")
                runtime("org.springframework.boot:spring-boot-devtools:2.1.2.RELEASE").optional(true)
                provided("org.springframework.boot:spring-boot-configuration-processor:2.1.2.RELEASE").optional(true)
                system("org.example:custom-lib:1.0::jdk11", "lib/custom-lib-1.0.jar")
                test("org.springframework.boot:spring-boot-starter-test").excluding("junit:junit", "org.hamcrest:hamcrest")
            }
        }
    }
}