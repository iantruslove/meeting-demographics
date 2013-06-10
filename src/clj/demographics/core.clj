(ns demographics.core
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.handler :refer [site]]
            [compojure.route :refer [resources not-found]]))

(defroutes app-routes
  (GET "/" [] "<p>Hello from compojure!</p>")
  (resources "/")
  (not-found "<p>404 - page not found</p>\n"))

(def app
  (site app-routes))
