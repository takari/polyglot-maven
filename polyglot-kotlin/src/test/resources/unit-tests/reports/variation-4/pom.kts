project {
    profiles {
        profile {
            id("other")
            activation {
                activeByDefault()
            }
            reports {
                "note" to """The "reports" section consists of arbitrary XML"""
                "coverage" to "62%"
                "scanSucceeded" to true
                "properties" {
                    "int.max" to 2147483647
                    "int.min" to -2147483647
                    "long.max" to 9223372036854775807
                    "long.min" to -9223372036854775807
                    "double.max" to 1.7976931348623157E308
                    "double.min" to 4.9E-324
                    "date.value" to java.time.LocalDate.parse("2019-02-01")
                    "datetime.value" to java.time.ZonedDateTime.parse("2019-02-01T11:42:19.416Z")
                    "one" to 1
                    "two" to "002"
                    "three" to 3.0
                    "four" to "4.0.0"
                    "true" to true
                    "false" to false
                    "javaVersion" to "\${java.version}"
                    "kotlinVersion" to "\${kotlin.version}"
                }
            }
        }
    }
}