(ns ring-experiment.system
  (:use ring.adapter.jetty)
  (:require ring-experiment.core)
  (:require cljs.closure))

(defn system
  "Returns a new instance of the whole application"
  ([port]
     (let [app #'ring-experiment.core/app
           webserver (run-jetty app {:port (Integer. port)
                                     :join? false })]
       (.stop webserver)
       {:state "initialized"
        :webserver webserver}))
  ([]      ;; default "constructor" on port 8080 
     (system 8080)))

(defn- start-webserver [system]
  (.start (:webserver system))
  system)

(defn- stop-webserver [system]
  (.stop (:webserver system))
  system)

(defn- compile-clojurescript [_]
  (cljs.closure/build "src/cljs/ring_experiment/"
                      {:optimizations :whitespace
                       :pretty-print true
                       :output-dir "resources/public/js/files"
                       :output-to "resources/public/js/app.js" }))

(defn start
  "Performs side effects to initialize a system, acquire resources, and start it running.
   Returns an updated instance of the app."
  [system]
  (-> system
      (compile-clojurescript)
      (start-webserver)
      (assoc :state "started")))

(defn stop
  "Performs side effects to shut down system, and release its resources.
   Returns an updated instance of the system."
  [system]
  (-> system
      (stop-webserver)
      (assoc :state "stopped")))

(defn -main [port]
  (start (system port)))

