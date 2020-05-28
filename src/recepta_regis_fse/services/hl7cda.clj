(ns recepta-regis-fse.services.hl7cda
  (:require
    [recepta-regis-fse.configuration :as conf]
    [selmer.parser :as parser]
    [clj-time.core :as tcore]
    [clj-time.format :as tformat]
    [clj-time.local :as tlocal]))

(def presidio-sirai {:id 1 :codice "200098" :descrizione "P.O. SIRAI"})
(def presidio-nsbn {:id 5 :codice "200034" :descrizione "P.O. N.S. DI BONARIA"})
(def presidio-sbarbara {:id 2 :codice "200029" :descrizione "P.O. SANTA BARBARA"})

(defn clean-codice-reparto [codice]
  (case codice
    9.01 "0901"
    8.01 "0801"
    801 "0801"
    901 "0901"
    codice))

(defn string->date [s] (tformat/parse s))

(def cda-format (tformat/formatter "yyyyMMdd"))

(def custom-time-formatter
  (tformat/with-zone (tformat/formatter "yyyyMMddHHmmssZz")
                     (tcore/time-zone-for-id "Europe/Rome")))

(defn get-cdatime [s]
  (tformat/unparse
    cda-format
    (string->date s)))

(def mock-cco-map
  {:id "a125"
   :setid "a125"
   :version 2
   :effectivetime 12
   :patient
      {:fiscalcode "CSTFPP70E29G702H"
       :given "FILIPPO"
       :family "COSTALLI"}})




(defn generate
  [data-map]
  (parser/render-file
     (:cco-cda-template conf/configuration)
     data-map))
