(ns recepta-regis-fse.services.hl7cda
  (:require
    [recepta-regis-fse.configuration :as conf]
    [selmer.parser :as parser]))

(def presidio-1 {:id 1 :codice "200098" :descrizione "P.O. SIRAI"
                 :citta "Carbonia" :indirizzo "Via dell'Ospedale" :civico "" :cap "09013" :asl "ASSL CARBONIA" :id-marno-asl "SMARNO07A07B745H"})
(def presidio-5 {:id 5 :codice "200034" :descrizione "P.O. N.S. DI BONARIA"
                 :citta "San Gavino Monreale (SU)" :indirizzo "Via Roma" :civico "" :cap "09037" :asl "ASSL SANLURI" :id-marno-asl "SMARNO06A06H974N"})
(def presidio-2 {:id 2 :codice "200029" :descrizione "P.O. SANTA BARBARA"
                 :citta "Iglesias (SU)" :indirizzo "Via S. Leonardo" :civico "1" :cap "09016" :asl "ASSL CARBONIA" :id-marno-asl "SMARNO07A07B745H"})
(def presidio-3 {:id 3 :codice "200030" :descrizione "P.O. CTO"
                 :citta "Iglesias (SU)" :indirizzo "Via R. Cattaneo" :civico "52" :cap "09016" :asl "ASSL CARBONIA" :id-marno-asl "SMARNO07A07B745H"})

(def presidi {:1 presidio-1, :2 presidio-2, :3 presidio-3 :5 presidio-5})

(defn date->cdatime [date]
  (str
    (.format (java.text.SimpleDateFormat. "yyyyMMddHHmmss") date)
    (.format (java.text.SimpleDateFormat. "Z") date)))

(defn date->time-nooffset [date]
    (.format (java.text.SimpleDateFormat. "yyyyMMddHHmmss") date))

(defn cartella->cda
  [cartella]
  (let [presidio ((keyword (str (:cartella_presidio_id cartella))) presidi)]
    (parser/render-file
       (:cco-cda-template conf/configuration)
       (-> cartella
         (assoc :presidio presidio)
         (assoc :data-compilazione (date->cdatime (java.util.Date.)))
         (assoc :data-produzione (date->cdatime (:cartella_datascansione cartella)))
         (assoc :data-firma (date->cdatime (java.util.Date.)))
         (assoc :data-ricovero (date->time-nooffset (:cartella_dataricovero cartella)))
         (assoc :cartella-endpoint "regis/consultaCartella")))))
