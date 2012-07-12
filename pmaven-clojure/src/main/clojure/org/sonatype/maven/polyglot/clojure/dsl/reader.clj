(ns org.sonatype.maven.polyglot.clojure.dsl.reader
 (:use org.sonatype.maven.polyglot.clojure.dsl.util))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Code to support the macro
;

; Non-gensymed symbols are used because a) different fragments are combined
; from different sources; and b) there's a bug with auto gensym that sometimes
; gives a binding different names at different points, especially when
; multiple fragments are combined into one syntax-quote

(def *uncoerced-classes* #{ String Boolean/TYPE Integer/TYPE })

(defn- scalar-field-handler [name kw type]
  `(~kw
     (~(symbol (str ".set" name))
       ~'object
       ~(if (contains? *uncoerced-classes* type)
          'value
          `(coerce-to-maven ~'value ~type)))))

(defn- list-field-handler
  ([name kw type]
    `(~kw
       (doseq [~'v ~'value]
         (~(symbol (str ".add" (singular-form-of-name name)))
           ~'object
           (coerce-to-maven ~'v ~type)))))
  ([name kw type mutator]
    `(~kw
       (doseq [~'v ~'value]
         (let [~'target (coerce-to-maven ~'v ~type)]
           ~mutator
           (~(symbol (str ".add" (singular-form-of-name name)))
             ~'object
             ~'target))))))

(defn- dependency-field-handler []
  (let [name "Dependencies"
        type org.apache.maven.model.Dependency]
    [(list-field-handler name :test-dependencies     type `(.setScope ~'target "test"))
     (list-field-handler name :provided-dependencies type `(.setScope ~'target "provided"))
     (list-field-handler name :runtime-dependencies  type `(.setScope ~'target "runtime"))
     (list-field-handler name :system-dependencies   type `(.setScope ~'target "system"))
     (list-field-handler name :dependencies          type)]))

(defn- configuration-field-handler []
  [`(:configuration (parse-configuration-node! ~'config (vec ~'value)))])

(defn- field-handlers [klass]
  (let [introspector (java.beans.Introspector/getBeanInfo klass)
        descriptors (.getPropertyDescriptors introspector)
        writable-descriptors (filter #(.getWriteMethod %) descriptors)]
    (for [descriptor writable-descriptors]
      (let [name (capitalize-first-letter (.getName descriptor))
            kw (keyword (from-camel-case name))
            type (.getPropertyType descriptor)
            list-type (when (= type java.util.List) (list-type klass name))]
        (cond

          (= name "Dependencies")
          (dependency-field-handler)

          (= name "Configuration")
          (configuration-field-handler)

          (= type java.util.List)
          [(list-field-handler name kw list-type)]

          :else
          [(scalar-field-handler name kw type)])))))

(defmacro defmodel-reader [className]
  (let [klass (Class/forName (str "org.apache.maven.model." className))
        name (symbol (.getSimpleName klass))
        has-coordinates (has-method? klass "setGroupId")
        has-configuration (has-method? klass "setConfiguration")]
    `(do
       (defn ~name [& ~'args]
         (let [~(with-meta 'object {:tag (symbol (.getName klass))}) (new ~(symbol (.getName klass)))
               ~@(when has-configuration
                   `(~'config (org.codehaus.plexus.util.xml.Xpp3Dom. "configuration")))]
           (loop [[~'key ~'value & rest#]
                  ~(if has-coordinates
                     `(parse-coordinates ~'args ~'object)
                     'args)]
             (when ~'key
               (condp = ~'key
                 ~@(apply concat (apply concat (field-handlers klass)))
                 ~(if has-configuration
                    `(parse-configuration-node! ~'config [~'key ~'value])
                    `(throw (IllegalArgumentException.
                              (str ~(str name " doesn't have an attribute named ") ~'key)))))
               (recur rest#)))
           ~@(when has-configuration
               [`(.setConfiguration ~'object ~'config)])
           ~'object))
       (defmethod coerce-to-maven [clojure.lang.IPersistentVector ~klass] [value# ~'_]
         (apply ~name value#))
       ~(if has-coordinates
          `(defmethod coerce-to-maven [clojure.lang.Symbol ~klass] [value# ~'_]
             (~name value#))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Code to support the runtime execution
;

(defmulti parse-configuration-node! (fn [parent value] (class value)))

(defmethod parse-configuration-node! clojure.lang.IPersistentVector [parent [head & tail :as children]]
  (cond
    ; TODO: handle namespaced names :a/b -> "a:b" for Xpp3Dom
    ; TODO: handle attributes i.e. a map as the second element of the list
    (instance? clojure.lang.Keyword head) (let [node (org.codehaus.plexus.util.xml.Xpp3Dom. (name head))]
                                            (doseq [x tail] (parse-configuration-node! node x))
                                            (.addChild parent node))
    (instance? String head) (let [wrapper-name (singular-form-of-name (.getName parent))]
                              (doseq [x children]
                                (let [node (org.codehaus.plexus.util.xml.Xpp3Dom. wrapper-name)]
                                  (parse-configuration-node! node x)
                                  (.addChild parent node))))
    :else (doseq [x children]
            (parse-configuration-node! parent x))))

(defmethod parse-configuration-node! :default [parent value]
  (.setValue parent (str value)))

(defmulti coerce-to-maven (fn [value dest-class] [(class value) dest-class]))

(defmethod coerce-to-maven [clojure.lang.Associative java.util.Properties] [value _]
  (let [properties (java.util.Properties.)]
    (doseq [[k v] value]
      (.setProperty properties (name k) v))
    properties))

(defmethod coerce-to-maven :default [value _] value)

(defmodel-reader Activation)
(defmodel-reader ActivationFile)
(defmodel-reader ActivationOS)
(defmodel-reader ActivationProperty)
(defmodel-reader Build)
(defmodel-reader BuildBase)
(defmodel-reader CiManagement)
(defmodel-reader Contributor)
(defmodel-reader Dependency)
(defmodel-reader DependencyManagement)
(defmodel-reader DeploymentRepository)
(defmodel-reader Developer)
(defmodel-reader DistributionManagement)
(defmodel-reader Exclusion)
(defmodel-reader Extension)
(defmodel-reader IssueManagement)
(defmodel-reader License)
(defmodel-reader MailingList)
(defmodel-reader Model)
(defmodel-reader Notifier)
(defmodel-reader Organization)
(defmodel-reader Parent)
(defmodel-reader Plugin)
(defmodel-reader PluginExecution)
(defmodel-reader PluginManagement)
(defmodel-reader Prerequisites)
(defmodel-reader Profile)
(defmodel-reader Relocation)
(defmodel-reader ReportPlugin)
(defmodel-reader ReportSet)
(defmodel-reader Reporting)
(defmodel-reader Repository)
(defmodel-reader RepositoryPolicy)
(defmodel-reader Resource)
(defmodel-reader Scm)
(defmodel-reader Site)

(def *MODEL* (atom nil))

(defn defmaven [& args]
  (reset! *MODEL* (apply Model args)))
