(defproject clj-exchange "1.0.0-SNAPSHOT"
  :description "Utility for Exchange Web Service Access"
  :url "https://github.com/cmbntr/clj-exchange"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/data.json "0.2.4"]
                 [org.codehaus.jackson/jackson-xc "1.9.11"]
                 [joda-time/joda-time "2.3"]]

  :plugins [[midje "1.6.0"]
            [lein-marginalia "0.7.1"]]

  :source-paths [ "src/clojure" ]
  :resource-paths [ "src/resource" ]
  :java-source-paths [ "src/java" ]

  :aot [clj-exchange.core clj-exchange.main clj-exchange.ews clj-exchange.prefs]
  :main clj-exchange.main)

