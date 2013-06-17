(ns demographics.app
  (:require [clojure.data.json :as json]
            [compojure.core :refer [defroutes context GET POST]]
            [compojure.handler :as handler]
            [compojure.route :refer [resources not-found]]
            [ring.util.response :as resp]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]))

(def meeting-attendees (atom {:male {:frog 0 :toad 0}
                              :female {:frog 0 :toad 0}}))

(defn add-participant [attendees gender type]
        (update-in attendees [gender type] inc)) 

(defn register-participant! [gender type]
  (swap! meeting-attendees add-participant gender type))

(defroutes api-routes
  (GET "/meeting" [] (resp/response @meeting-attendees))
  (POST "/meeting" []
        (fn [request]
          (let [gender (keyword (get-in request [:body :data-primary-val]))
                type (keyword (get-in request [:body :data-secondary-val]))]
            (resp/response (register-participant! gender type))))))

(defroutes app-routes
  (context "/api" [] (-> api-routes
                         (wrap-json-response)
                         (wrap-json-body {:keywords? true})))
  (GET "/" [] (resp/resource-response "index.html" {:root "public"}))
  (GET ["/:id" :id #"[a-zA-Z0-9]{6,6}"] [id]
       (resp/resource-response "form.html" {:root "templates"}))
  (resources "/")
  (not-found "<p>404 - page not found</p>\n"))

(def app
  (handler/site app-routes))
