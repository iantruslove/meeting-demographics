(ns demographics.core
  (:require [compojure.core :refer [defroutes context GET POST]]
            [compojure.handler :as handler]
            [compojure.route :refer [resources not-found]]
            [ring.util.response :as resp]))

(defroutes api-routes
  (POST "/meeting" [] (fn [request] {:status 200})))

(defroutes app-routes
  (context "/api" [] api-routes)
  (resources "/")
  (GET "/" [] (resp/resource-response "index.html" {:root "public"}))
  (not-found "<p>404 - page not found</p>\n"))

(def app
  (handler/site app-routes))
