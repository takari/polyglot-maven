;;
;; Copyright (c) 2015 to original author or authors
;; All rights reserved. This program and the accompanying materials
;; are made available under the terms of the Eclipse Public License v1.0
;; which accompanies this distribution, and is available at
;; http://www.eclipse.org/legal/epl-v10.html
;;

(defproject org.clojars.the-kenny/clojure-couchdb "0.2"
  :description "Simple Clojure interface to Apache CouchDB, fork of the original project with function arguments instead of *server* and some other changes."
  :dependencies [[org.clojure/clojure "1.1.0-master-SNAPSHOT"]
                 [org.clojure/clojure-contrib "1.0-SNAPSHOT"]
                 [org.clojure/clojure-http-client "1.0.0-SNAPSHOT"]]
  :dev-dependencies [[lein-clojars "0.5.0-SNAPSHOT"]
                     [org.clojure/swank-clojure "1.0"]])
