(ns clj-exchange.core
  (:gen-class)
  (:use [clj-exchange ews mapping time])

  (:import [com.microsoft.schemas.exchange.services._2006.messages
            FindFolderType
            FindItemType
            ResolveNamesType]))

;;Service Description:
;;http://msdn.microsoft.com/en-us/library/bb409286(v=exchg.140).aspx

(defn parse-response-flat [r]
  (into {} (mapcat #(-> % :JAXBElement :value)
                   (-> r
                       parse-response
                       :ResponseMessages
                       :createItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage))))

(defn resolve-names [svc n]
  (let [r (-> (build ResolveNamesType {:UnresolvedEntry n :ReturnFullContactData true})
              (ex-resolve-names svc)
              parse-response-flat)]
    (condp =  (:ResponseCode r)
      "NoError" (-> r :ResolutionSet :Resolution first)
      "ErrorNameResolutionNoResults" nil

      (if-let [possibilities (-> r :ResolutionSet :Resolution)]
        (if (= 1 (count possibilities))
          (first possibilities)
          possibilities)))))

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

(defn calendar-items
  ([svc start end]
     (calendar-items svc start end nil))
  ([svc start end mbox]
     (let [f {:DistinguishedFolderId (into {:Id "calendar"} (if mbox {:Mailbox {:EmailAddress  mbox}}))}]
       (-> (build FindItemType
                  {:Traversal "Shallow"
                   :ItemShape {:BaseShape "AllProperties"}
                   :CalendarView {:StartDate (time-millis start) :EndDate (time-millis end)}
                   :ParentFolderIds {:folderIdOrDistinguishedFolderId [f]}})
           (ex-find-item svc)))))


