(ns clj-exchange.mapping
  (:use clojure.data.json))

(defn- jackson-mapper []
  (let [m (org.codehaus.jackson.map.ObjectMapper.)
        i (org.codehaus.jackson.xc.JaxbAnnotationIntrospector.)]
    (-> m .getDeserializationConfig (.setAnnotationIntrospector i))
    (-> m .getSerializationConfig (.setAnnotationIntrospector i))
    m))

(defn- to-json-with-jackson [o]
  (.writeValueAsString (jackson-mapper) o))

(defn- to-value-with-jackson [s target]
  (.readValue (jackson-mapper) s target))

(defn parse-response [r]
  (-> r to-json-with-jackson read-json))

(defn build [target x]
  (-> x json-str (to-value-with-jackson target)))


