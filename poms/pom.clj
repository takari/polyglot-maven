(defmaven 'io.tesla.polyglot/tesla-polyglot "0.0.1-SNAPSHOT"
 :model-version "4.0.0"
 :parent ['io.tesla/tesla "4" :relative-path "../pom.xml"]
 :name "Polyglot Tesla :: Aggregator"
 :packaging "pom"
 :properties {:teslaVersion "3.1.0" :sisuInjectVersion "0.0.0.M2a"}
 
 :modules ["tesla-polyglot-common" 
           "tesla-polyglot-atom" 
           "tesla-polyglot-ruby" 
           "tesla-polyglot-groovy" 
           "tesla-polyglot-yaml" 
           "tesla-polyglot-clojure" 
           "tesla-polyglot-scala" 
           "tesla-polyglot-cli" 
           "tesla-polyglot-maven-plugin"]
           
 :dependency-management [
   :dependencies [
     ['org.eclipse.sisu/org.eclipse.sisu.inject "${sisuInjectVersion}"]
     ['org.eclipse.sisu/org.eclipse.sisu.plexus "${sisuInjectVersion}"]
     ['org.apache.maven/maven-model-builder "3.1.0"]
     ['org.apache.maven/maven-embedder "3.1.0"]]
   :test-dependencies [
     ['junit "4.11"]
   ]
 ]
 
 :build [
   :plugins [
     ['org.codehaus.plexus/plexus-component-metadata "1.5.4"
       :executions [
         [:id "default" :goals ["generate-metadata" "generate-test-metadata"]]]]]])