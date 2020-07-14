(ns recepta-regis-fse.core
  (:require
     [recepta-regis-fse.medir :as medir]
     [chime.core :as chime]
     [cambium.core :as log])
  (:import
     (java.time LocalTime ZonedDateTime ZoneId Period)))


(def period
     (chime/periodic-seq (-> (LocalTime/of 13 0 0)
                             (.adjustInto (ZonedDateTime/now (ZoneId/of "Europe/Rome")))
                             .toInstant)
                         (Period/ofDays 1)))

(defn -main
  [& args]
  (log/info "Application started")
  (chime/chime-at period
                  (fn [time]
                    (log/info "Fava"))))
