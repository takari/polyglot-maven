(defmaven 'a/b "c"
  :dependencies [['org.clojure/clojure "1.1.0-alpha-SNAPSHOT"]
                 ['org.clojure/clojure-contrib "1.0-SNAPSHOT"]])

; Use the provided API to add a new dependency
(.addDependency @*MODEL* (Dependency 'org.testng/testng "5.10"))
