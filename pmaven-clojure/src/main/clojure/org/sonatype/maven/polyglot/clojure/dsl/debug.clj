(ns org.sonatype.maven.polyglot.clojure.dsl.debug
 (:require org.sonatype.maven.polyglot.clojure.dsl.reader)
 (:require org.sonatype.maven.polyglot.clojure.dsl.writer)
 (:require org.sonatype.maven.polyglot.clojure.dsl.pretty-printer))

(org.sonatype.maven.polyglot.clojure.dsl.reader/defmaven
  'org.clojure/clojure "1.2.0-master-SNAPSHOT"
  :model-version "4.0.0"
  :name "Lamdras Website"
  :description "Acumen / LRMDS Integration"
  :properties { :project.build.sourceEncoding "UTF-8" }
  :packaging "war"
  :dependencies ['ring/ring-servlet
                 'ring/ring-devel
                 'clj-routing/clj-routing
                 'clout/clout
                 'compojure/compojure
                 'hiccup/hiccup
                 'org.clojure/clojure
                 'org.clojure/clojure-contrib
                 'congomongo/congomongo]
  :provided-dependencies [['org.mortbay.jetty/servlet-api-2.5 "6.1.14"]]
  :build [:final-name "website"
          :plugins [['org.apache.maven.plugins/maven-compiler-plugin "2.1" :source "1.6" :target "1.6"]
                    ['com.theoryinpractise/clojure-maven-plugin "1.3.1"
                     :sourceDirectories ["src/main/java"]
                     :executions [[:id "compile-clojure" :phase "compile" :goals ["compile"]]]]
                    ['org.mortbay.jetty/maven-jetty-plugin "6.1.10"
                     :configuration { :scanIntervalSeconds 10 :stopKey "foo" :stopPort 9999 }]]])

(defn test-pp []
  (org.sonatype.maven.polyglot.clojure.dsl.pretty-printer/print-object-to-writer
    (list*
      'defmaven
      (org.sonatype.maven.polyglot.clojure.dsl.writer/reify-field-value
        @org.sonatype.maven.polyglot.clojure.dsl.reader/*MODEL*))
    *out*)
  (.write *out* "\n"))
