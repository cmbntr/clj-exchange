(ns clj-exchange.time)

(defn parse-time [t]
  (org.joda.time.DateTime. t))

(defn time-millis [t]
  (if-let [x (parse-time t)]
    (.getMillis x)))