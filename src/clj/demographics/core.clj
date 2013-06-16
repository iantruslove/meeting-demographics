(ns demographics.core
  (:require [clojure.data.json :as json]
            [compojure.core :refer [defroutes context GET POST]]
            [compojure.handler :as handler]
            [compojure.route :refer [resources not-found]]
            [ring.util.response :as resp]
            [ring.middleware.json]))

(def meeting-attendees (atom {:male {:frog 0 :toad 0}
                              :female {:frog 0 :toad 0}}))

(defn add-participant [attendees gender type]
        (update-in attendees [gender type] inc)) 

(defn register-participant [gender type]
  (swap! meeting-attendees add-participant gender type))

(defroutes api-routes
  (POST "/meeting" [] (fn [request]

                        (let [gender (keyword (get-in request
                                                       [:body :data-primary-val]))
                              type (keyword  (get-in request 
                                                     [:body :data-secondary-val]))]
                          (register-participant gender type))
                        
                        {:status 200
                         :headers {"Content-type" "application/json"}
                         :body (json/write-str @meeting-attendees)})))

(defroutes app-routes
  (context "/api" [] (ring.middleware.json/wrap-json-body api-routes {:keywords? true}))
  (resources "/")
  (GET "/" [] (resp/resource-response "index.html" {:root "public"}))
  (not-found "<p>404 - page not found</p>\n"))

(def app
  (handler/site app-routes))
