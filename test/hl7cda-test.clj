(ns recepta-regis-fse.hl7cda-test
  (:use clojure.test)
  (:require
     [recepta-regis-fse.services.database :as database]
     [recepta-regis-fse.services.hl7cda :as hl7cda]
     [recepta-regis-fse.services.xades :as xades]
     [clojure.string :as s]
     [clojure.java.io :as io]))

(def not-nil? (complement nil?))


(deftest get-presidio-inesistente
   (let [presidio (:1000 hl7cda/presidi)]
     (is (nil? presidio))))


(deftest get-presidio-esistente
    (let [presidio (:1 hl7cda/presidi)]
      (is (= (:id presidio) 1))
      (is (= (:codice presidio) "200098"))
      (is (= (:descrizione presidio) "P.O. SIRAI"))
      (is (= (:citta presidio) "Carbonia"))
      (is (= (:indirizzo presidio) "Via dell'Ospedale"))
      (is (= (:cap presidio) "09013"))
      (is (= (:asl presidio) "ASSL CARBONIA"))))


(deftest get-cdatime
  (let [cdatime (hl7cda/date->cdatime (java.util.Date.))]
    (println cdatime)))


(deftest get-cartella
   (let [cartelle (database/get-cartelle)
         cartella (first cartelle)]
     (is (= "Y0W000WM7" (:cartella_barcode cartella)))
     (is (= 128796 (:cartella_id cartella)))))


(deftest cda-composition
   (let [cartelle (database/get-cartelle)
         cartella (first cartelle)
         mycda (hl7cda/cartella->cda cartella)
         filename (str "E:/tmp/Recepta/Regis/Fse/test-cda/test_" (:cartella_id cartella) "_" (:cartella_barcode cartella) ".xml")]
     (spit filename mycda)
     (is (= 128796 (:cartella_id cartella)))
     (is (.exists (io/as-file filename)))))
     ;;(is (s/include? mycda))))

(deftest cda-sign
  (let [cartelle (database/get-cartelle)
        cartella (first cartelle)
        mycda (hl7cda/cartella->cda cartella)
        mycda_signed (xades/sign mycda)
        filename (str "E:/tmp/Recepta/Regis/Fse/test-cda/test_" (:cartella_id cartella) "_" (:cartella_barcode cartella) "_signed.xml")]
     (spit filename mycda_signed)
     (is (.exists (io/as-file filename)))))