(ns recepta-regis-fse.services.database
  (:require
    [recepta-regis-fse.configuration :as conf]
    [clojure.java.jdbc :as jdbc]))

(def regis-db (:regis-db conf/configuration))

(extend-protocol jdbc/IResultSetReadColumn
  java.sql.Date
  (result-set-read-column [v _ _]
    (java.util.Date. (.getTime v))))

(defn import-cartelle []
  (jdbc/query regis-db "SELECT import_cartella_medir()"))

(defn get-cartelle []
  (jdbc/query regis-db ["SELECT * FROM cartella_medir WHERE cartella_presidio_id IN (1,2,3,5) AND  cartella_statoinviomedir = 0 ORDER BY cartella_id"]))

(defn get-cartella [id]
    (jdbc/query regis-db ["SELECT * FROM cartella_medir WHERE cartella_id = ?" id]))

(defn get-cartelle-sanluri []
  (jdbc/query regis-db ["SELECT * FROM cartella_medir WHERE cartella_presidio_id = 5 ORDER BY cartella_id"]))

(defn get-cartelle-sirai []
  (jdbc/query regis-db ["SELECT * FROM cartella_medir WHERE cartella_presidio_id = 1 ORDER BY cartella_id"]))

(defn update-cartella! [cartella]
  (jdbc/update! regis-db
                :cartella_medir
                {:cartella_hl7cda (:cartella_hl7cda cartella)
                 :cartella_statoinviomedir (:cartella_statoinviomedir cartella)
                 :cartella_messaggiomedir (:cartella_messaggiomedir cartella)}
                ["cartella_id = ?" (:cartella_id cartella)]))

(defn update-cartelle! [cartelle]
   (doseq [cartella cartelle] (update-cartella!  cartella)))
 
