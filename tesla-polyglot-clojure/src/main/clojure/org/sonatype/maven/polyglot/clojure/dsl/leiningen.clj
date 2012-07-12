;;
;; Copyright (C) 2010 the original author or authors.
;;
;; Licensed under the Apache License, Version 2.0 (the "License");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at
;;
;; http://www.apache.org/licenses/LICENSE-2.0
;;
;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.
;;

(ns org.sonatype.maven.polyglot.clojure.dsl.leiningen
  (:use org.sonatype.maven.polyglot.clojure.dsl.reader))

; TODO: does the assembly plugin belong in here, given the lein uberjar target?
(def *leiningen-defaults*
  '[:model-version "4.0.0"
    :repositories [[:id "central"           :url "http://repo1.maven.org/maven2"]
                   [:id "clojure"           :url "http://build.clojure.org/releases"]
                   [:id "clojure-snapshots" :url "http://build.clojure.org/snapshots"]
                   [:id "clojars"           :url "http://clojars.org/repo/"]]
    :build [:plugins [[com.theoryinpractise/clojure-maven-plugin "1.3.2"
                       :testScript "src/test/clojure/test.clj"
                       :executions [[:id "compile"      :phase "compile"      :goals ["testCompile"]]
                                    [:id "test-compile" :phase "test-compile" :goals ["compile"]]
                                    [:id "test"         :phase "test"         :goals ["test"]]]]]]])

(def *leiningen-valid-properties* [ :dependencies :name :description ])

; I anticipate this becoming more sophisticated over time.
(defmacro defproject [& args]
  (let [cleaned-args (apply concat (vec (select-keys (apply hash-map (drop 2 args)) *leiningen-valid-properties*)))
        quoted-args (map #(list 'quote %) (concat (take 2 args) cleaned-args *leiningen-defaults*))]
  `(defmaven ~@quoted-args)))
