;;
;; Copyright (c) 2015 to original author or authors
;; All rights reserved. This program and the accompanying materials
;; are made available under the terms of the Eclipse Public License v1.0
;; which accompanies this distribution, and is available at
;; http://www.eclipse.org/legal/epl-v10.html
;;

(defmaven 'a/b "c"
  :dependencies [['org.clojure/clojure "1.1.0-alpha-SNAPSHOT"]
                 ['org.clojure/clojure-contrib "1.0-SNAPSHOT"]])

; Use the provided API to add a new dependency
(.addDependency @*MODEL* (Dependency 'org.testng/testng "5.10"))
