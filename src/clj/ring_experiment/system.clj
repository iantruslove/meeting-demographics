(ns ring-experiment.system
  (:use ring.adapter.jetty)
  (:require ring-experiment.core))

(defn system
  "Returns a new instance of the whole application"
  []
  (let [app #'ring-experiment.core/app
        webserver (run-jetty app {:port 8080
                                  :join? false })]
    (.stop webserver)
    {:state "initialized"
     :webserver webserver}))

(defn- start-webserver [system]
  (.start (:webserver system))
  system)

(defn- stop-webserver [system]
  (.stop (:webserver system))
  system)

(defn start
  "Performs side effects to initialize a system, acquire resources, and start it running.
   Returns an updated instance of the app."
  [system]
  (-> system
      (start-webserver)
      (assoc :state "started")))

(defn stop
  "Performs side effects to shut down system, and release its resources.
   Returns an updated instance of the system."
  [system]
  (-> system
      (stop-webserver)
      (assoc :state "stopped")))
