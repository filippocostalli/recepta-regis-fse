(ns recepta-regis-fse.medir-test
  (:use clojure.test)
  (:require
     [recepta-regis-fse.services.database :as database]
     [recepta-regis-fse.medir :as medir]
     [clojure.string :as s]
     [clojure.java.io :as io]))

(def not-nil? (complement nil?))

(deftest cartella->cdasigned-test
  (let [cartelle (database/get-cartelle-sanluri)
        cartella-in (first cartelle)
        cartella-out (medir/cartella->cdasigned cartella-in)
        filename (str "E:/tmp/Recepta/Regis/Fse/test-cda/test_" (:cartella_id cartella-in) "_" (:cartella_barcode cartella-in) "_signed_medir.xml")]
      (is (not-nil? cartella-out))
      (spit filename cartella-out)
      (is (.exists (io/as-file filename)))))

(deftest cartellain->cartellaout-test
   (let [cartelle (database/get-cartelle-sanluri)
         cartella-in (first cartelle)
         cartella-out (medir/cartellain->cartellaout cartella-in)
         filename (str "E:/tmp/Recepta/Regis/Fse/test-cda/test_" (:cartella_id cartella-in) "_" (:cartella_barcode cartella-in) "_signed_medir2.xml")]
     (is (not-nil? cartella-out))
     (println (str "***********" (:cartella_messaggiomedir cartella-out)))
     (is (= (:cartella_id cartella-out) (:cartella_id cartella-in)))
     (is (= (:cartella_statoinviomedir cartella-out) 1))
     (spit filename (:cartella_hl7cda cartella-out))
     (is (.exists (io/as-file filename)))))


(deftest medir-job-test_
  (medir/job 3)
  (is true))
