(ns demographics.views.collection-form
  (:require [domina :as dom]
            [domina.css :as css]
            [domina.events :as ev]
            [goog.net.XhrIo :as xhr]))

(def api-href (atom nil))
(def initial-data {:male {:frog 0 :toad 0}
                   :female {:frog 0 :toad 0}})

(defn css-data-attr-selector [data-name val]
  (str "[data-" (name data-name) "='" (name val) "']"))

(defn find-button [gender type]
  (css/sel (str "button"
                (css-data-attr-selector "primary-val" gender)
                (css-data-attr-selector "secondary-val" type))))

(defn label-button! [gender type current-count]
  (dom/set-text! (find-button gender type) (str "+1 " (name gender) " " (name  type) " [" current-count "]")))

(defn get-count-from [data gender type]
  (get-in data [gender type]))

(defn set-button-text! [data]
  (doseq [[gender data-per-gender] data]
    (doseq [[type number] data-per-gender]
      (label-button! gender type number))))

(defn data-attrs [ev]
  (-> ev
      (:target)
      (domina/attrs)
      (select-keys [:data-primary-val :data-secondary-val])
      (clj->js)
      (js/JSON.stringify)))

(defn handle-ajax-response [response]
  (let [data (js->clj (.getResponseJson (.-target response)))]
    (set-button-text! data)))

(defn submit-ajax-request
  [ev]
  (.send goog.net.XhrIo
         @api-href
         handle-ajax-response
         "POST"
         (data-attrs ev)
         (clj->js {:Content-Type "application/json"})))

(defn plus-one-handler [ev]
  (submit-ajax-request ev))

(defn on-click-plus-button [ev]
  (plus-one-handler ev))

(defn ^:export init [root-el]
  (reset! api-href (str "/api/meeting" (.-pathname (.-location js/window))))
  (ev/listen! (css/sel root-el "button") :click on-click-plus-button)
  (set-button-text! initial-data))
