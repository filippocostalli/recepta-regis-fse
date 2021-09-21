(ns recepta-regis-fse.services.uid-generator
  (:require
    [recepta-regis-fse.configuration :as conf])
  (:import (java.util TimeZone)
           (java.util Calendar)))

(def characters "123456789abcdefghijklmnopqrstuvwyxzABCDEFGHIJKLMNOPQRSTUVWYXZ")

(defn base10->base61 [^Long number]
  (loop [num number
         acc []]
    (if (zero? num)
      (clojure.string/join (reverse acc))
      (recur (long (/ num 61))
             (conj acc (nth characters (mod num 61)))))))

(defn codice-operatore-base61 []
  (-> conf/configuration
      :codice-operatore
      (Long/valueOf)
      (base10->base61)))

(defn timestamp-base61 []
  (-> (TimeZone/getTimeZone "GMT")
      (Calendar/getInstance)
      (.getTimeInMillis)   ;; base 10
      (base10->base61)))

(defn get-control-code [uid]
  (let [numeric-code (->> uid
                         (map #(get conversion-table %))
                         (apply +))]
     (nth characters (mod numeric-code 61))))

(defn get-uid []
  (let [codice-operatore (codice-operatore-base61)
        operatore-offset (apply str (take (- 7 (count codice-operatore)) (repeat "1")))
        timestamp (timestamp-base61)
        timestamp-offset (apply str (take (- 8 (count timestamp)) (repeat "1")))
        uid-no-control-code  (str operatore-offset codice-operatore timestamp-offset timestamp)]
   (str uid-no-control-code (get-control-code uid-no-control-code))))
