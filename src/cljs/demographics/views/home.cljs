(ns demographics.views.home
  (:require [domina :as dom]
            [domina.css :as css]
            [domina.events :as ev]
            [clojure.browser.net :as net]
            [clojure.browser.event :as event]))

(defn bind-new-buttons! [buttons handler]
  (ev/listen! buttons :click handler))

(defn ^:export find-new-buttons []
  (css/sel ".button.demo-create-new"))

(defn nav-to! [url]
  (set! (.-href (.-location js/window)) url))

(defn handle-new-meeting-id [response]
  (let [data (js->clj (.getResponseJson (.-target response)))
        loc (.-location js/window)]
    (nav-to! (str (.-protocol loc)
                  "//"
                  (.-host loc)
                  "/"
                  ("meeting-id" data)))))

(defn request-new-meeting-id []
  (let [xhr (net/xhr-connection.)]
    (event/listen xhr :success handle-new-meeting-id)
    (net/transmit xhr "/api/meeting" "POST")))

(defn on-click-new-button [ev]
  (ev/prevent-default ev)
  (request-new-meeting-id))

(defn bind-events []
  (bind-new-buttons! (find-new-buttons) on-click-new-button))

(defn ^:export unbind-events []
  (ev/unlisten! (find-new-buttons) :click))

(defn ^:export init []
  (bind-events))
