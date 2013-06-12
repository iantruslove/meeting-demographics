(ns demographics.webserver
  (:require [ring.adapter.jetty :as jetty]))

(defn init
  "Returns a new instance of the webserver"
  ([port app]
     {:state :initialized
      :server nil
      :port port
      :app app
      })
  ([]
     (init 8080 nil)))

(defn start
  "Start the webserver if it's not already running"
  [webserver]
  (if (not (= :started (:state webserver)))
    (let [port (:port webserver)
          app (:app webserver)
          server (jetty/run-jetty app {:port (Integer. port)
                                 :join? false })]
      (.start server)
      (assoc webserver
        :server server
        :state :started))
    webserver))

(defn stop
  "Stops a running webserver"
  [webserver]
  (if (= :started (:state webserver))
    (.stop (:server webserver)))
  (assoc webserver
    :state :stopped
    :server nil))

(defn restart [webserver]
  (start (stop webserver)))
