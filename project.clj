(defproject clj-exchange "1.0.0-SNAPSHOT"
  :description "Utility for Exchange Web Service Access"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.clojure/data.json "0.1.1"]
                 [org.codehaus.jackson/jackson-xc "1.9.3"]
                 [joda-time/joda-time "2.0"]]

  :dev-dependencies [[midje "1.3.1"]
                     [lein-marginalia "0.6.1"]]
  
  :source-path "src/clojure"
  :resources-path "src/resource"
  :java-source-path [["src/java"]]
  
  :aot [clj-exchange.core clj-exchange.ews clj-exchange.prefs]
  :main clj-exchange.core)

