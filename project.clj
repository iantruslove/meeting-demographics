(defproject demographics "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src/clj"]

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [ring "1.1.8"]
                 ;[ring/ring-json "0.2.0"]
                 [ring-middleware-format "0.3.0"]
                 [cljsbuild "0.3.2"]
                 ;; CLJS:
                 [cljsbuild "0.3.2"]
                 [domina "1.0.2-SNAPSHOT"]]

  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/tools.namespace "0.2.3"]]}}

  :plugins [[lein-ring "0.8.5"]]
  )
