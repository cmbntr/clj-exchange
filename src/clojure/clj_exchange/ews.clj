(ns clj-exchange.ews
  (:use clj-exchange.prefs)
  (:import [javax.xml.namespace QName]
           [javax.xml.ws Service Holder BindingProvider]
           [com.microsoft.schemas.exchange.services._2006.messages ExchangeServicePortType])
  (:require [clojure.string :as str]))

(def wsdl-location (.getResource ExchangeServicePortType "/services.wsdl"))
(def svc-name  (QName/valueOf "{http://schemas.microsoft.com/exchange/services/2006/messages}ExchangeWebService"))
(def port-name (QName/valueOf "{http://schemas.microsoft.com/exchange/services/2006/messages}ExchangeWebPort") )

(defn ews
  ([] (let [{:keys [ews-port username password]} (get-exchange-prefs)]
        (ews ews-port username (rot13 password))))
  
  ([endpoint username password]
      (let [port (-> (Service/create wsdl-location svc-name)
                     (.getPort port-name ExchangeServicePortType))]
        (doto (.getRequestContext port)
          (.put BindingProvider/ENDPOINT_ADDRESS_PROPERTY endpoint)
          (.put BindingProvider/USERNAME_PROPERTY username)
          (.put BindingProvider/PASSWORD_PROPERTY password))
        port)))

;; define stub functions

(defn- box
  ([]  (Holder.))
  ([v] (Holder. v)))

(defn- unbox [b]
  (if b (.value b)))

(defn- camel-to-dash [s]
  (str/replace s #"[A-Z]+" #(str \- (str/lower-case %))))

(defn- get-web-methods [c]
  (remove #(nil? (.getAnnotation % javax.jws.WebMethod)) (-> c .getMethods)))

(defn- def-ex-helper [m]
  (let [n (.getName m)
        s (symbol n)
        d (str "ex-" (camel-to-dash n))
        port (symbol "port")
        req  (symbol "req")]
    `(defn ~(symbol d) [~req ~port]
       (let [out# (box)]
         (. ~port ~s ~req out# (box))
         (unbox out#)))))

(defmacro def-ex-calls []
  (cons 'do (map def-ex-helper (get-web-methods ExchangeServicePortType))))

(def-ex-calls)




