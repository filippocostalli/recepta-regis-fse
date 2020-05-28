(ns recepta-regis-fse.configuration
  (:require
    [cprop.core :refer [load-config]]
    [cprop.source :as source]))

(def configuration (load-config))
