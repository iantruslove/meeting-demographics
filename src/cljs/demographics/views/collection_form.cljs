(ns demographics.views.collection-form
  (:require [domina.css :refer [sel]]
            [domina.events :refer [listen! unlisten!
                                   ]]
            [goog.net.XhrIo :as xhr]))

(def last-event (atom nil))

(defn ^:export set-last-event! [ev]
  (reset! last-event ev))

(defn ^:export get-last-event []
  @last-event)

(defn data-attrs [ev]
  (-> ev
      (:target)
      (domina/attrs)
      (select-keys [:data-primary-val :data-secondary-val])
      (clj->js)
      (js/JSON.stringify))
 )

(defn handle-ajax-response [response]
  (js/console.log (clj->js response))
  )

(defn submit-ajax-request
  [ev]
  (.send goog.net.XhrIo
         "/api/meeting"
         handle-ajax-response
         "POST"
         (data-attrs ev)
         (clj->js {:Content-Type "application/json"}))
  )

(defn plus-one-handler [ev]
  (js/console.log (data-attrs ev))
  (set-last-event! ev)
  (submit-ajax-request ev))

(defn ^:export init [root-el]
  (listen! (sel root-el "button") :click plus-one-handler))
