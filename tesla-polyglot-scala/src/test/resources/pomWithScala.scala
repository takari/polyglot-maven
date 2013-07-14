
/**
 * The simple Scala POM example from the Polyglot Maven
 * Confluence documentation page.
 **/

project("myGroupId:myArtifactId:1.0-SNAPSHOT") { proj =>

    //...adds scala library dependency and configures Maven Scala compiler plugin...
    proj includesScalaSourceCode "2.7.7"

    //...adds normal Maven artifact dependency to Apache Commons HttpClient 3.0.1...
    proj dependency "apache-httpclient:commons-httpclient:3.0.1"

}
