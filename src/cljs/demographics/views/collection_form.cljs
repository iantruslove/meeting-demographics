(ns demographics.views.collection-form
  (:require [domina.css :refer [sel]]
            [domina.events :refer [listen!]]))

(defn plus-one-handler [ev]
  (js/debugger)
  (js/console.log ev))

(defn ^:export init [root-el]
  (js/console.log "collection-form init")
  (js/console.log root-el)
  (listen! (sel root-el "button") :click plus-one-handler))
