(defproject clj-exchange "1.0.0-SNAPSHOT"
  :description "Utility for Exchange Web Service Access"
  :url "https://github.com/cmbntr/clj-exchange"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.clojure/data.json "0.1.2"]
                 [org.codehaus.jackson/jackson-xc "1.9.5"]
                 [joda-time/joda-time "2.1"]]

  :dev-dependencies [[midje "1.3.1"]
                     [lein-marginalia "0.7.0"]]

  :source-path "src/clojure"
  :resources-path "src/resource"
  :java-source-path "src/java"

  :aot [clj-exchange.core clj-exchange.main clj-exchange.ews clj-exchange.prefs]
  :main clj-exchange.main)

