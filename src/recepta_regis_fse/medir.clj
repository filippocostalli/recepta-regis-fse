(ns recepta-regis-fse.medir
  (:require
    [recepta-regis-fse.services.database :as database]
    [recepta-regis-fse.services.hl7cda :as hl7cda]
    [recepta-regis-fse.services.xades-mul :as xades-mul]
    [cambium.core :as log]))

(defn cartella->cdasigned
  [cartella]
  (let [presidio-id (:cartella_presidio_id cartella)]
    (-> cartella
        (hl7cda/cartella->cda)
        (xades-mul/sign presidio-id))))

(defn cartellain->cartellaout
   [cartella]
   (try
     {:cartella_id (:cartella_id cartella)
      :cartella_hl7cda (cartella->cdasigned cartella)
      :cartella_statoinviomedir 1}
     (catch Exception e
       (log/error (str "Errore creazione-forma hl7 cda. cartella=" (:cartella_id cartella) ", messaggio=" (.getMessage e)))
       {:cartella_id (:cartella_id cartella)
        :cartella_statoinviomedir 2
        :cartella_messaggiomedir (.getMessage e)})))

(defn processa-cartelle
  [n]
  (->> (database/get-cartelle)
       (take n)
       (map cartellain->cartellaout)
       (database/update-cartelle!)))


(defn processa-cartella [cartella-id]
  (->> (database/get-cartella cartella-id)
      first
      ;;(hl7cda/cartella->cda)
      (cartella->cdasigned)
      (spit (str "E:/tmp/Recepta/Regis/Fse/test-cda/v2/" cartella-id ".xml"))))
