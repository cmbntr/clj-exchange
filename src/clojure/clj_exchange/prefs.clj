(ns clj-exchange.prefs
  (:gen-class))

(defn- user-prefs
  ([] (user-prefs "/"))
  ([p](doto
          (if (instance? java.util.prefs.Preferences p) p
              (-> (java.util.prefs.Preferences/userRoot) (.node p)))
        (.sync))))

(defn- get-prefs [path]
  (let [p (user-prefs path)]
    (into {} (for [k (.keys p)] [(read-string k)
                                 (if-let [v (.get p k nil)]
                                   (read-string v))]))))

(defn- set-prefs! [p m]
  (let [s (user-prefs p)
        coded (into {} (for [[k v] m] [(pr-str k) (pr-str v)]))]
    (doseq [c (.keys s)]
      (if-not (get (keys coded) c) (.remove s c)))
    (doseq [[k v] coded]
      (.put s k v))
    (.flush s)
    s))

(defn- assoc-prefs! [p & kvs]
  (let [s (user-prefs p)
        o (get-prefs s)]
    (if-let [n (and kvs (apply assoc (cons o kvs)))]
      (do (set-prefs! s n) n)
      o)))

;; exchange prefs
(defn get-exchange-prefs [] (get-prefs "clj-exchange"))
(def assoc-exchange-prefs! (partial assoc-prefs! "clj-exchange"))

(defn rot13 [c]
  (if (string? c)
    (apply str (map rot13 c))
    (let [i (int c)]
      (cond
       (or (and (>= i (int \a)) (<= i (int \m)))
           (and (>= i (int \A)) (<= i (int \M))))
       (char (+ i 13))
       (or (and (>= i (int \n)) (<= i (int \z)))
           (and (>= i (int \N)) (<= i (int \Z))))
       (char (- i 13))
       :else c))))

(defn -main [& [ews-port username pwd]]
  (assoc-exchange-prefs!
   :ews-port ews-port
   :username username
   :password (rot13 pwd))
  (println "preferences stored."))

(comment
  (-main "https://example.com/EWS/exchange.asmx" "username" "password"))

