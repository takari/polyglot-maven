(ns org.sonatype.maven.polyglot.clojure.dsl.pretty-printer)

(comment "
keyword           -> if not sol then new line at current indent
list, vector, map -> if not sol then new line at current indent
<open> sol = true <value*> <close>
scalar            -> <space><value> sol = false
")

(defn- write-string [state string]
  (.write (:writer state) string)
  (swap! (:position state) + (count string))
  (reset! (:start-of-line state) false))

(defn- write-quote [state]
  (.write (:writer state) "'")
  (swap! (:position state) + 1)
  (reset! (:start-of-line state) true))

(defn- write-start-of-line-space [state]
  (.write (:writer state) " ")
  (swap! (:position state) + 1)
  (reset! (:start-of-line state) true))

(defn- write-opening-delimiter [state string]
  (.write (:writer state) string)
  (swap! (:indent state) conj (+ @(:position state) (count string)))
  (swap! (:position state) + (count string))
  (reset! (:start-of-line state) true))

(defn- write-closing-delimiter [state string]
  (write-string state string)
  (reset! (:start-of-line state) false)
  (swap! (:indent state) pop))

(defn- write-space-if-not-start-of-line [state]
  (when-not @(:start-of-line state)
    (write-string state " ")))

(defn- indent-if-not-start-of-line [state]
  (when-not @(:start-of-line state)
    (.write (:writer state) "\n")
    (dotimes [_ (peek @(:indent state))]
      (.write (:writer state) " "))
    (reset! (:position state) (peek @(:indent state)))
    (reset! (:start-of-line state) true)))

(defmulti pretty-print (fn [object state] (class object)))

(defmethod pretty-print clojure.lang.Keyword [object state]
  (indent-if-not-start-of-line state)
  (write-string state (str object))
  (write-start-of-line-space state))

(defmethod pretty-print clojure.lang.IPersistentVector [object state]
  (indent-if-not-start-of-line state)
  (write-opening-delimiter state "[")
  (doseq [o object]
    (pretty-print o state))
  (write-closing-delimiter state "]"))

(defmethod pretty-print clojure.lang.ISeq [object state]
  (if (= (first object) 'quote)
    (do
      (write-space-if-not-start-of-line state)
      (write-quote state)
      (pretty-print (second object) state))
    (do
      (indent-if-not-start-of-line state)
      (write-opening-delimiter state "(")
      (doseq [o object]
        (pretty-print o state))
      (write-closing-delimiter state ")"))))

(defmethod pretty-print clojure.lang.IPersistentMap [object state]
  (indent-if-not-start-of-line state)
  (write-opening-delimiter state "{")
  (doseq [[k v] object]
    (pretty-print k state)
    (write-space-if-not-start-of-line state)
    (pretty-print v state))
  (write-closing-delimiter state "}"))

(defmethod pretty-print :default [object state]
  (write-space-if-not-start-of-line state)
  (write-string state (pr-str object)))

(defn print-object-to-writer [object writer]
  (pretty-print object { :writer writer :start-of-line (atom true) :indent (atom '()) :position (atom 0) }))