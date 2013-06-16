;(ns demographics.webserver)
(ns demographics.brepl
  (:require [cljs.repl.browser]))

(defn connect []
  (cemerick.piggieback/cljs-repl
   :repl-env (doto (cljs.repl.browser/repl-env :port 9000)
               cljs.repl/-setup)))
