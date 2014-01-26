(ns example
  (:gen-class)
  (:use [clj-exchange ews mapping time])
  (:import [com.microsoft.schemas.exchange.services._2006.messages FindItemType]))

(defn calendar-items [svc start end]
  (-> (build FindItemType
             {:Traversal "Shallow"
              :ItemShape {:BaseShape "AllProperties"}
              :CalendarView {:StartDate (time-millis start)
                             :EndDate (time-millis end)}
              :ParentFolderIds {:folderIdOrDistinguishedFolderId
                                [{:DistinguishedFolderId {:Id "calendar"}}]}})
      (ex-find-item svc)))

(defn -main [& [endpoint username pwd from to]]
  (let [svc (ews endpoint username pwd)
        c   (calendar-items svc from to)]
    (doseq [i (-> (parse-response c)
                  :ResponseMessages
                  :createItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage
                  first
                  :JAXBElement :value
                  :RootFolder :Items :itemOrMessageOrCalendarItem
                  )]
      (println i))))

;; examples

(comment

  (def svc (ews))

  (def f (find-folder svc))
  (map #(-> % .getValue bean)
       (-> f .getResponseMessages
           .getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage
           seq
           ))

  (def i (task-items svc))
  (map bean
       (mapcat #(-> % .getValue .getRootFolder .getItems .getItemOrMessageOrCalendarItem seq)
               (-> i .getResponseMessages
                   .getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage
                   seq
                   )))

  )
