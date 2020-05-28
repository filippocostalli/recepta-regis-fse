(ns recepta-regis-fse.services.ws
  (:require
    [recepta-regis-fse.configuration :as conf]
    [clj-http.client :as client]
    [paos.service :as service]
    [paos.wsdl :as wsdl]))


(def medir-service (wsdl/parse  (:medir-wsdl-url conf/configuration)))

;; (get-in medir-service ["DocumentRepositoryBinding_IssuedTokenForUsernameSecureConversationMutualCertificate11_policy" :operations "ProvideDocument"])
;; (get-in medir-service ["DocumentRepositoryBinding_IssuedTokenForUsernameSecureConversationMutualCertificate11_policy" :url])


;; ancora da modellare
(defn send-message
  [message])
(let [medir-url (:url medir-service)
      srv (get-in soap-service ["SomeServiceBinding" :operations "operation1"])
      soap-headers (service/soap-headers srv)
      content-type (service/content-type srv)
      mapping (service/request-mapping srv)
      context (do-something-with-mapping mapping)
      body (service/wrap-body srv context)]
  (client/post soap-url
     {:content-type content-type
      :headers soap-headers
      :body body}))
