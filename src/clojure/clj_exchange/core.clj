(ns clj-exchange.core
  (:gen-class)
  (:use [clj-exchange ews mapping time])
  (:import [com.microsoft.schemas.exchange.services._2006.messages FindFolderType FindItemType]))

;;Service Description:
;;http://msdn.microsoft.com/en-us/library/bb409286(v=exchg.140).aspx

(defn find-folder [svc]
  (-> (build FindFolderType
             {:Traversal "Shallow"
              :FolderShape {:BaseShape "Default"}
              :ParentFolderIds {:folderIdOrDistinguishedFolderId
                                [{:DistinguishedFolderId {:Id "inbox"}}]}})
      (ex-find-folder svc)))

(defn task-items [svc]
  (-> (build FindItemType
             {:Traversal "Shallow"
              :ItemShape {:BaseShape "AllProperties"}
              :ParentFolderIds {:folderIdOrDistinguishedFolderId
                                [{:DistinguishedFolderId {:Id "tasks"}}]}})
      (ex-find-item svc)))

(defn calendar-items [svc start end]
  (-> (build FindItemType
             {:Traversal "Shallow"
              :ItemShape {:BaseShape "AllProperties"}
              :CalendarView {:StartDate (time-millis start)
                             :EndDate (time-millis end)}
              :ParentFolderIds {:folderIdOrDistinguishedFolderId
                                [{:DistinguishedFolderId {:Id "calendar"}}]}})
      (ex-find-item svc)))

;; examples

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

(defn -main [& [from to]]
  (let [c (calendar-items svc from to)]
    (doseq [i (-> (parse-response c)
                  :ResponseMessages
                  :createItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage
                  first
                  :JAXBElement :value
                  :RootFolder :Items :itemOrMessageOrCalendarItem
                  )]
      (println i))))


