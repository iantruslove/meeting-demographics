(ns demographics.brepl
  (:require [clojure.browser.repl :as brepl]))

(defn connect []
  (brepl/connect "http://localhost:9000/repl")

  (cemerick.piggieback/cljs-repl :repl-env (doto (cljs.repl.browser/repl-env
                                                  :port 9000)
                                             cljs.repl/-setup))
)
