(ns clj-exchange.mapping
  (:require [clojure.data.json :as json]))

(defn- jackson-mapper []
  (let [m (org.codehaus.jackson.map.ObjectMapper.)
        i (org.codehaus.jackson.xc.JaxbAnnotationIntrospector.)]
    (-> m .getDeserializationConfig (.setAnnotationIntrospector i))
    (-> m .getSerializationConfig (.setAnnotationIntrospector i))
    (-> m (.configure
           org.codehaus.jackson.map.SerializationConfig$Feature/WRITE_DATES_AS_TIMESTAMPS false))
    m))

(def mapper (jackson-mapper))

(defn- to-json-with-jackson [o]
  (.writeValueAsString mapper o))

(defn- to-value-with-jackson [s target]
  (.readValue mapper s target))

(defn parse-response [r]
  (-> r to-json-with-jackson json/read-json))

(defn build [target x]
  (-> x json/json-str (to-value-with-jackson target)))


