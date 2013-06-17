(ns demographics.system
  (:use ring.adapter.jetty)
  (:require demographics.webserver)
  (:require demographics.app)
  (:require cljs.closure))

;;TODO: extract the jetty webserver out to a webserver object
(defn init
  "Returns a new instance of the whole application"
  ([port]
     (let [app #'demographics.app/app
           webserver (demographics.webserver/init (Integer. port) app)]
       {:state :initialized
        :webserver webserver}))
  ([]      ;; default "constructor" on port 8080 
     (init 8080)))

(defn start-webserver [system]
  (assoc system :webserver 
         (demographics.webserver/start (:webserver system))))

(defn stop-webserver [system]
  (assoc system :webserver 
         (demographics.webserver/stop (:webserver system))))

(defn compile-clojurescript [system]
  (cljs.closure/build "src/cljs/demographics/"
                      {:optimizations :whitespace
                       :pretty-print true
                       :output-dir "resources/public/js/files"
                       :output-to "resources/public/js/app.js" })
  system)

(defn start
  "Performs side effects to initialize a system, acquire resources, and start it running.
   Returns an updated instance of the app."
  [system]
  (assoc
      (compile-clojurescript (start-webserver system))
    :state :started))

(defn stop
  "Performs side effects to shut down system, and release its resources.
   Returns an updated instance of the system."
  [system]
  (assoc
      (compile-clojurescript (stop-webserver system))
    :state :stopped))

(defn -main [port]
  (start (init port)))

