(ns clj-exchange.ews
  (:import [javax.xml.namespace QName]
           [javax.xml.ws Service BindingProvider]
           [com.microsoft.schemas.exchange.services._2006.messages ExchangeServicePortType]))

(def wsdl-location (.getResource ExchangeServicePortType "/services.wsdl"))
(def svc-name  (QName/valueOf "{http://schemas.microsoft.com/exchange/services/2006/messages}ExchangeWebService"))
(def port-name (QName/valueOf "{http://schemas.microsoft.com/exchange/services/2006/messages}ExchangeWebPort") )

(defn ews [endpoint username password]
  (let [port (-> (Service/create wsdl-location svc-name)
                 (.getPort port-name ExchangeServicePortType))]
    (doto (.getRequestContext port)
      (.put BindingProvider/ENDPOINT_ADDRESS_PROPERTY endpoint)
      (.put BindingProvider/USERNAME_PROPERTY username)
      (.put BindingProvider/PASSWORD_PROPERTY password))
    port))
