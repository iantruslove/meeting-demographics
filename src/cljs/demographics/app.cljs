(ns demographics.app
  (:require [domina :refer [by-id]]
            [demographics.views.collection-form :as form]
            [clojure.browser.repl :as repl]))

(repl/connect "http://localhost:9000/repl")

(defn ^:export init []
  (form/init (by-id "demographics-form")))
