(ns demographics.brepl
  (:require [cljs.repl.browser :as brepl]))

(defn connect []
  (cemerick.piggieback/cljs-repl
   :repl-env (doto (cljs.repl.browser/repl-env :port 9000)
               cljs.repl/-setup)))
