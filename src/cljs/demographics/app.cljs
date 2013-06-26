(ns demographics.app
  (:require [domina :refer [by-id]]
            [demographics.views.collection-form :as form]
            [demographics.views.home :as home]
            [clojure.browser.repl :as repl]))

(repl/connect "http://localhost:9000/repl")

(defn ^:export form_init []
  (form/init))

(defn ^:export home_init []
  (home/init))
