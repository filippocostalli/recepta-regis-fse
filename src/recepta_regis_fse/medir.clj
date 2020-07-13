(ns recepta-regis-fse.medir
  (:require
    [recepta-regis-fse.services.database :as database]
    [recepta-regis-fse.services.hl7cda :as hl7cda]
    [recepta-regis-fse.services.xades-mul :as xades-mul]))


(defn cartella->cdasigned
  [cartella]
  (let presidio-id (:cartella_presidio_id cartella)
    (-> cartella
        (hl7cda/cartella->cda)
        (partial (xades-mul/sign presidio-id)))))


(defn cartellain->cartellaout
   [cartella]
   (try
     ({:cartella_id (:cartella_id cartella)
       :cartella_hl7cda (cartella->cdasigned cartella)
       :cartella_statoinviomedir 1})
     (catch Exception e
       ({:cartella_id (:cartella_id cartella)
         :cartella_statoinviomedir 2
         :cartella_messaggiomedir (.getMessage e)}))))


(defn medir-job []
  (-> (database/get-cartelle)
      (take 100)
      (cartellain->cartellaout)
      (database/update-cartelle)))
