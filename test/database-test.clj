(ns recepta-regis-fse.database-test
  (:use clojure.test)
  (:require
     [recepta-regis-fse.services.database :as database]))

;; Mock test
(deftest addition-tests
  (is (= 5 (+ 3 2)))
  (is (= 10 (+ 5 5))))


(deftest test-create-table
   (let [cartelle (database/get-cartelle)
         cartella (first cartelle)]
     (is (= 128796 (:cartella_id cartella)))))
