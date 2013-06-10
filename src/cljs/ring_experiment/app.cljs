(ns ring-experiment.app)

(defn ^:export main []
  (.write js/document "<p>Hello from cljs</p>"))
