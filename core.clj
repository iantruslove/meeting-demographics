(ns ring-experiment.core
  (use compojure.core)
  (:require [ring.middleware.params :refer [wrap-params]]
                                        ;[ring.middleware.json :as json]
            [ring.middleware.format :refer [wrap-restful-format]]
            [compojure.route :as route]
            [compojure.handler :as handler]))

(defroutes api-routes
  (GET "/:resource" [_resource] {:body { :message "this is the api" :code 1234}})
  (route/not-found "API 404"))

(defroutes docs-routes
  (GET "/" [] "this is a doc"))

(defroutes root-routes
  (context "/api" []  api-routes)
  (context "/docs" []  docs-routes)
  (route/not-found "<h1>Page not found</h1>"))

(def app
  (-> (handler/api root-routes)
      (wrap-restful-format)))
