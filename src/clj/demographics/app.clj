(ns demographics.app
  (:require [clojure.data.json :as json]
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

(def meeting-template {:male {:frog 0 :toad 0}
                       :female {:frog 0 :toad 0}})

(defn add-new-meeting [meetings meeting-id]
  (assoc meetings meeting-id meeting-template))

(def meeting-attendees (atom {}))

(defn add-participant [attendees meeting-id gender type]
        (update-in attendees [meeting-id gender type] inc)) 

(defn register-participant! [meeting-id gender type]
  (swap! meeting-attendees add-participant meeting-id gender type)
  (meeting-id @meeting-attendees))

(defn get-meeting-info
  "Return the current info on a particular meeting"
  [id]
  (resp/response (id @meeting-attendees)))

(defn add-meeting-participant
  "Adds a participant to a meeting"
  [meeting-id primary-attr-val secondary-attr-val]
  (resp/response (register-participant! meeting-id primary-attr-val secondary-attr-val)))

;; TODO: Split into 2 parts: one to create the meeting, one to
;; formulate the response
(defn create-new-meeting [base-uri]
  (let [new-id (gen-chars 6)
        new-uri (str base-uri "/" new-id)]
    (swap! meeting-attendees add-new-meeting (keyword new-id))
    (-> (resp/response {:message "created"
                        :_links {:self {:href  new-uri
                                        :type "application/json"}}
                        :meeting-id new-id})
        (resp/header "Location" new-uri)
        (resp/status 202))))

(defroutes api-routes
  (GET ["/meeting/:id" :id re-meeting-id] [id] (get-meeting-info (keyword id)))
  (POST ["/meeting/:id", :id re-meeting-id]
        {{id :id} :params
         {primary-val :data-primary-val secondary-val :data-secondary-val} :body}
        (apply add-meeting-participant (map keyword [id primary-val secondary-val])))
  (POST "/meeting" {uri :uri} (create-new-meeting uri))
)

(defn meeting-exists? [meeting-id]
  (contains? @meeting-attendees (keyword meeting-id)))

(defn show-meeting-page [meeting-id]
  (if (meeting-exists? meeting-id) 
    (resp/resource-response "form.html" {:root "templates"})
    (-> 
     (resp/resource-response "no-such-meeting.html" {:root "public"})
     (resp/status 404))))

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
