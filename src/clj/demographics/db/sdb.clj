(ns demographics.db.sdb
  (:require [cemerick.rummage :as db]
            [cemerick.rummage.encoding :as enc]))

(defn init []
  (let [endpoint "http://localhost:8080"
        creds (com.amazonaws.auth.BasicAWSCredentials. "key" "123")
        client (doto (com.amazonaws.services.simpledb.AmazonSimpleDBClient. creds) (.setEndpoint endpoint))
        ]
    {:endpoint endpoint
     :creds creds 
     :client client
     }))
