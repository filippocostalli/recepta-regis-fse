(ns recepta-regis-fse.services.database
  (:require
    [recepta-regis-fse.configuration :as conf]
    [clojure.java.jdbc :as jdbc]))

(def regis-db (:regis-db conf/configuration))

(extend-protocol jdbc/IResultSetReadColumn
  java.sql.Date
  (result-set-read-column [v _ _]
    (java.util.Date. (.getTime v))))

(def query-cartelle
  "SELECT * FROM cartella WHERE cartella_presidio_id IN (1,2,3,5) ORDER BY cartella_id")

(defn get-cartelle []
  (jdbc/query regis-db [query-cartelle]))
