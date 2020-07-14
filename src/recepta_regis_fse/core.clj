(ns recepta-regis-fse.core
  (:require
     [recepta-regis-fse.medir :as medir]
     [chime.core :as chime]
     [cambium.core :as log])
  (:import
     (java.time LocalTime ZonedDateTime ZoneId Period))
  (:gen-class))


(def period
     (chime/periodic-seq (-> (LocalTime/of 13 20 0)
                             (.adjustInto (ZonedDateTime/now (ZoneId/of "Europe/Rome")))
                             .toInstant)
                         (Period/ofDays 1)))

(defn -main
  [& args]
  (log/info "Application started 13 20")
  (chime/chime-at period
                  (fn [time]
                    (log/info (medir/processa-cartelle 100)))))
