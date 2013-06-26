(ns demographics.views.collection-form
  (:require-macros [enfocus.macros :as em])
    
  (:require [domina :as dom]
            [domina.css :as css]
            [domina.events :as ev] 
            [enfocus.core :as ef]
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

(em/deftemplate table-cell :compiled "resources/templates/form_table_cell.html" [primary-attr secondary-attr]
  [".plus-one"] (em/chain
                 (em/content (str "+1 " primary-attr " " secondary-attr))
                 (em/set-attr :data-primary-val primary-attr :data-secondary-val secondary-attr)))

(em/deftemplate table-row :compiled "resources/templates/form_table_row.html" [primary-attrs secondary-attr]
  ["th"] (em/content secondary-attr)
  ["td.data"] (em/clone-for [primary-attr primary-attrs]
                            (em/content (table-cell primary-attr secondary-attr))))

(em/deftemplate table-outline :compiled "resources/templates/form_table_outline.html" [primary-attrs secondary-attrs]
  ["tr.header > th.column"] (em/clone-for [primary-attr primary-attrs]
                                   (em/content primary-attr))
  ["tr.data"] (em/clone-for [secondary-attr secondary-attrs]
                            (em/content (table-row primary-attrs secondary-attr))))

(defn ^:export create-buttons! [data]
  (.log js/console (str "Creating buttons: " (pr-str data)))
  (let [pri-attrs (vec (keys data))
        sec-attrs (vec (keys ((first pri-attrs) data)))]
    (.log js/console (str pri-attrs ", " sec-attrs))
    (em/at js/document
           ["#demographics-form"] (em/content (table-outline pri-attrs sec-attrs)))))

(defn set-button-text! [data]
  (.log js/console (str "Setting button vals: " (pr-str data)))
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

(defn get-attendee-data [response]
  (js->clj (.getResponseJson (.-target response))))

(defn on-plus-one-response [response]
  (let [data (get-attendee-data response)]
    (set-button-text! data)))

(defn send-plus-one
  [ev]

  (.send goog.net.XhrIo
         @api-href
         on-plus-one-response
         "POST"
         (data-attrs ev)
         (clj->js {:Content-Type "application/json"})))

(defn plus-one-handler [ev]
  (send-plus-one ev))

(defn on-click-plus-button [ev]
    (.log js/console "Send!")
  (plus-one-handler ev))

(defn get-data [callback]
  (.send goog.net.XhrIo
         @api-href
         #(callback (get-attendee-data %))
         "GET"))

(defn register-click-handlers! []
    (ev/listen! (css/sel "button") :click on-click-plus-button)
  )

(defn ^:export init []
  (reset! api-href (str "/api/meeting" (.-pathname (.-location js/window))))
  (get-data (fn [data]
              (create-buttons! data)
              (set-button-text! data)
              (register-click-handlers!)))
;  (set-button-text! initial-data)
  )
