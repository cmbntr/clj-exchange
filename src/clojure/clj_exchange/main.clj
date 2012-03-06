(ns clj-exchange.main
  (:gen-class)
  (:use (clj-exchange [core :only [calendar-items parse-response-flat]]
                      [ews  :only [ews]])))

(defn -main [& [from to & [mbox]]]
  (doseq [i (-> (ews)
                (calendar-items from to mbox)
                parse-response-flat
                :RootFolder :Items :itemOrMessageOrCalendarItem)]
    (println i \newline)))
