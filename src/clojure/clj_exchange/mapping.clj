(ns clj-exchange.mapping
  (:use clojure.data.json))

(defn- jackson-mapper []
  (let [m (org.codehaus.jackson.map.ObjectMapper.)
        i (org.codehaus.jackson.xc.JaxbAnnotationIntrospector.)]
    (-> m .getDeserializationConfig (.setAnnotationIntrospector i))
    (-> m .getSerializationConfig (.setAnnotationIntrospector i))
    m))

(defn to-json-with-jackson [o]
  (.writeValueAsString (jackson-mapper) o))

(defn parse-response [r]
  (-> r to-json-with-jackson read-json))


