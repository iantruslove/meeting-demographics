(ns demographics.app
  (:require [demographics.data-model.data-model :as model]
            [clojure.data.json :as json]
            [compojure.core :refer [defroutes context GET POST]]
            [compojure.handler :as handler]
            [compojure.route :refer [resources not-found]]
            [ring.util.response :as resp]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [clojure.string :as str]
            [crypto.random]))

(def re-meeting-id #"[a-zA-Z0-9-_]{6,6}")

(defn gen-chars [n]
  (subs (str/replace (crypto.random/base64 (* 2 n))
                     #"[=+/]"
                     "")
        0 n))

;; TODO: Split into 2 parts: one to create the meeting, one to
;; formulate the response
(defn create-new-meeting [base-uri]
  (let [new-id (gen-chars 6)
        new-uri (str base-uri "/" new-id)]
    (model/create-new-meeting! (keyword new-id))
    (-> (resp/response {:message "created"
                        :_links {:self {:href  new-uri
                                        :type "application/json"}}
                        :meeting-id new-id})
        (resp/header "Location" new-uri)
        (resp/status 202))))


(defn add-meeting-participant
  "Adds a participant to a meeting"
  [meeting-id primary-attr-val secondary-attr-val]
  (apply model/register-participant!
         (map keyword [meeting-id primary-attr-val secondary-attr-val])))

(defn show-meeting-page [meeting-id]
  (if (model/meeting-exists? meeting-id) 
    (resp/resource-response "form.html" {:root "templates"})
    (-> 
     (resp/resource-response "no-such-meeting.html" {:root "public"})
     (resp/status 404))))

(defroutes api-routes
  (GET ["/meeting/:id" :id re-meeting-id] [id]
       (resp/response (model/get-meeting-info (keyword id))))
  (POST ["/meeting/:id", :id re-meeting-id]
        {{id :id} :params
         {primary-val :data-primary-val secondary-val :data-secondary-val} :body}
        (add-meeting-participant id primary-val secondary-val)
        (resp/response (model/get-meeting-info id)))
  (POST "/meeting" {uri :uri} (create-new-meeting uri)))

(defroutes app-routes
  (context "/api" [] (-> api-routes
                         (wrap-json-response)
                         (wrap-json-body {:keywords? true})))
  (GET "/" [] (resp/resource-response "index.html" {:root "public"}))
  (GET ["/:id" :id re-meeting-id] [id] (show-meeting-page id))
  (resources "/")
  (not-found "<p>404 - page not found</p>\n"))

(def app
  (handler/site app-routes))
