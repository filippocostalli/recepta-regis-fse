(ns recepta-regis-fse.database-test
  (:use clojure.test)
  (:require
     [recepta-regis-fse.services.database :as database]))


(deftest test-get-cartella
   (let [cartella (first (database/get-cartella 128796))]
     (is (= 128796 (:cartella_id cartella)))))


(deftest test-update-cartella
  (let [cartella {:cartella_id 128796
                  :cartella_statoinviomedir 1
                  :cartella_messaggiomedir "aloa"
                  :cartella_hl7cda "<pluto>"}]
    (database/update-cartella! cartella)
    (let [cartella2 (first(database/get-cartella 128796))]
       (is (= (:cartella_id cartella2)  128796))
       (is (= (:cartella_statoinviomedir cartella2)  1))
       (is (= (:cartella_messaggiomedir cartella2)  "aloa"))
       (is (= (:cartella_hl7cda cartella2)  "<pluto>")))))


(deftest test-update-cartella-ripristino
  (let [cartella {:cartella_id 128796
                  :cartella_statoinviomedir 0
                  :cartella_messaggiomedir nil
                  :cartella_hl7cda nil}]
    (database/update-cartella! cartella)
    (let [cartella2 (first(database/get-cartella 128796))]
       (is (= (:cartella_id cartella2)  128796))
       (is (= (:cartella_statoinviomedir cartella2)  0))
       (is (= (:cartella_messaggiomedir cartella2)  nil))
       (is (= (:cartella_hl7cda cartella2)  nil)))))

(deftest test-get-cartelle
   (let [cartelle (database/get-cartelle)
         cartella (first cartelle)]
     (is (= 128796 (:cartella_id cartella)))))


(deftest test-update-cartelle!
   (let [cartelle '({:cartella_id 128796, :cartella_statoinviomedir 1, :cartella_messaggiomedir "aaa", :cartella_hl7cda "<cda1>"}
                    {:cartella_id 128802, :cartella_statoinviomedir 1, :cartella_messaggiomedir "bbb", :cartella_hl7cda "<cda2>"})]
      (database/update-cartelle! cartelle)
      (let [cartella1 (first(database/get-cartella 128796))
            cartella2 (first(database/get-cartella 128802))]
        (is (= (:cartella_id cartella1)  128796))
        (is (= (:cartella_statoinviomedir cartella1)  1))
        (is (= (:cartella_messaggiomedir cartella1)  "aaa"))
        (is (= (:cartella_hl7cda cartella1)  "<cda1>"))
        (is (= (:cartella_id cartella2)  128802))
        (is (= (:cartella_statoinviomedir cartella2)  1))
        (is (= (:cartella_messaggiomedir cartella2)  "bbb"))
        (is (= (:cartella_hl7cda cartella2)  "<cda2>")))))

(deftest test-ripristino-update-cartelle!
   (let [cartelle '({:cartella_id 128796, :cartella_statoinviomedir 0, :cartella_messaggiomedir nil, :cartella_hl7cda nil}
                    {:cartella_id 128802, :cartella_statoinviomedir 0, :cartella_messaggiomedir nil, :cartella_hl7cda nil})]
      (database/update-cartelle! cartelle)
      (let [cartella1 (first(database/get-cartella 128796))
            cartella2 (first(database/get-cartella 128802))]
        (is (= (:cartella_id cartella1)  128796))
        (is (= (:cartella_statoinviomedir cartella1)  0))
        (is (= (:cartella_messaggiomedir cartella1)  nil))
        (is (= (:cartella_hl7cda cartella1)  nil))
        (is (= (:cartella_id cartella2)  128802))
        (is (= (:cartella_statoinviomedir cartella2)  0))
        (is (= (:cartella_messaggiomedir cartella2)  nil))
        (is (= (:cartella_hl7cda cartella2)  nil)))))
