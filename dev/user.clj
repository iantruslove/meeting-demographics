(ns user
  (:require [clojure.pprint :refer (pprint)]
            [clojure.repl :refer :all]
            [clojure.tools.namespace.repl :refer (refresh refresh-all)]
            [ring-experiment.system :as system]))

;; The global Var to hold the whole system
(def system nil)

(defn init
  "Constructs the current development system"
  []
  (alter-var-root #'system
                  (constantly (system/system))
                  ))

(defn start
  "Starts the current development system"
  []
  (alter-var-root #'system
                  system/start))

(defn stop
  "Shuts down and cleans up the current development system"
  []
  (alter-var-root #'system
                  (fn [s] (when s (system/stop s)))))

(defn go
  "Initializes the current development system and starts it running."
  []
  (init)
  (start))

(defn restart []
  (stop)
  (refresh :after 'user/go))
