(ns demographics.core
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.handler :refer [site]]
            [compojure.route :refer [resources not-found]]
            [ring.util.response :as resp]))

(defroutes app-routes
  (resources "/")
  (GET "/" [] (resp/resource-response "index.html" {:root "public"}))
  (not-found "<p>404 - page not found</p>\n"))

(def app
  (site app-routes))
