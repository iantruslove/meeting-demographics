(ns demographics.data-model.meeting-test
  (:require [demographics.data-model.meeting :refer :all]
            [clojure.test :refer :all]))

(deftest data-model-meeting
  
  (let [meeting {:col-names [:c1 :c2]
                 :row-names [:r1 :r2]
                 :rows [[1 2] [3 4]]}]

    (testing "cells can be incremented"
      (is (= (:rows (increment meeting :c2 :r2)) [[1 2] [3 5]])))))
