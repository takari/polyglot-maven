project {
    reports = """
          <properties>
            <long.max>9223372036854775807</long.max>
            <long.min>-9223372036854775808</long.min>
            <double.max>1.7976931348623157E308</double.max>
            <double.min>4.9E-324</double.min>
            <one>1</one>
            <two>002</two>
            <three>3.0</three>
            <four>4.0.0</four>
            <true>true</true>
            <false>false</false>
            <javaVersion>${"$"}{java.version}</javaVersion>
            <kotlinVersion>${"$"}{kotlin.version}</kotlinVersion>
          </properties>
        """
}