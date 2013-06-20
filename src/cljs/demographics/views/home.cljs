(ns demographics.views.home
  (:require [domina :as dom]
            [domina.css :as css]
            [domina.events :as ev]
            [goog.net.XhrIo]))

(def last-click-event (atom nil))

(defn bind-new-buttons! [buttons handler]
  (ev/listen! buttons :click handler))

(defn ^:export find-new-buttons []
  (css/sel ".button.demo-create-new"))

(defn nav-to! [url]
  (set! (.-href (.-location js/window)) url))

(def last-response (atom nil))

(defn handle-new-meeting-id [response]
  (reset! last-response response)
  (let [data (js->clj (.getResponseJson (.-target response)))]
    (nav-to! (str "http://localhost:8080/" ("meeting-id" data)))
))

(defn request-new-meeting-id []
  (.send goog.net.XhrIo
         "/api/meeting"
         handle-new-meeting-id
         "POST"))

(defn on-click-new-button [ev]
  (reset! last-click-event ev)
  (ev/prevent-default ev)
  (request-new-meeting-id))

(defn bind-events []
  (bind-new-buttons! (find-new-buttons) on-click-new-button))

(defn ^:export unbind-events []
  (ev/unlisten! (find-new-buttons) :click))

(defn ^:export init []
  (bind-events))
