(ns recepta-regis-fse.medir-test
  (:use clojure.test)
  (:require
     [recepta-regis-fse.services.database :as database]
     [recepta-regis-fse.services.hl7cda :as hl7cda]
     [recepta-regis-fse.services.xades :as xades]
     [recepta-regis-fse.services.xades-mul :as xades-mul]
     [clojure.string :as s]
     [clojure.java.io :as io]))

(def not-nil? (complement nil?))
