(ns demographics.app
  (:require [domina :refer [by-id]]
            [demographics.views.collection-form :as form])
  )

(defn ^:export init []
  (form/init (by-id "demographics-form")))
