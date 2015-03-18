(ns org.sonatype.maven.polyglot.clojure.dsl.util)

(defn singular-form-of-name [#^String plural]
  "Returns the gramatically singular form of the argument, using a very
simple translation of (in order) *ies -> *y, *s -> * and * -> *"
  (cond
    (.endsWith plural "ies") (str (.substring plural 0 (- (count plural) 3)) "y")
    (.endsWith plural "s") (.substring plural 0 (- (count plural) 1))
    :else plural))

(defn capitalize-first-letter [s]
  (str (.toUpperCase (.substring s 0 1)) (.substring s 1)))

(defn to-camel-case [#^String name]
  (apply str (map capitalize-first-letter (.split name "-"))))

(defn from-camel-case [#^String name]
  (apply str (interpose "-" (map #(.toLowerCase %) (re-seq #"\p{Alpha}\p{Lower}*" name)))))

(defn list-type [klass property-name]
  (let [add-method-name (str "add" (singular-form-of-name property-name))
        add-methods (filter #(= (.getName %) add-method-name) (.getMethods klass))]
    (when (seq add-methods)
      (first (.getParameterTypes (first add-methods))))))

(defn has-method? [klass method-name]
  (some #(= (.getName %) method-name) (.getMethods klass)))

(defmulti parse-coordinates (fn [source _] (class source)))

(defmethod parse-coordinates clojure.lang.Symbol
  [#^clojure.lang.Symbol source destination]
  (let [artifact (name source)
        group (if-let [g (namespace source)] g artifact)]
    (.setGroupId destination group)
    (.setArtifactId destination artifact)))

(defmethod parse-coordinates clojure.lang.Sequential
  [#^clojure.lang.Sequential source destination]
  (if (and (> (count source) 0) (instance? clojure.lang.Symbol (first source)))
    (do
      (parse-coordinates (first source) destination)
      (if (and (> (count source) 1) (instance? String (second source)))
        (do
          (.setVersion destination (second source))
          (drop 2 source))
        (drop 1 source)))
    source))