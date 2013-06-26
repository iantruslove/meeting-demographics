(defproject demographics "0.1.0-SNAPSHOT"
`  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src/clj"]

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/data.json "0.2.2"]
                 [compojure "1.1.5"]
                 [ring "1.1.8"]
                 [com.cemerick/piggieback "0.0.4"]
                 [ring-middleware-format "0.3.0"]
                 [ring/ring-json "0.2.0"]
                 [crypto-random "1.1.0"]
                 [prismatic/dommy "0.1.1"]

                 ;; CLJS:
                 [cljsbuild "0.3.2"]
                 [domina "1.0.2-SNAPSHOT"]
                 [enfocus "1.0.1"]]

  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
  
  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/tools.namespace "0.2.3"]]}}

  :ring {:handler demographics.app/app
         :port 8000}

  :plugins [[lein-ring "0.8.5"]
            [lein-cljsbuild "0.3.2"]]

  :cljsbuild {:builds [ {:source-paths ["src/cljs/demographics"]
                         :compiler {:optimizations :whitespace
                                    :pretty-print true
                                    :output-dir "resources/public/js/files"
                                    :output-to "resources/public/js/app.js" }}]}
  )
