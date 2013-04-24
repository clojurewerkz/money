(ns clojurewerkz.money.lib-integration-test
  (:require [cheshire.core :as json]
            clojurewerkz.money.json
            [clojurewerkz.money.amounts :as cm]
            [clojurewerkz.money.currencies :as cc])
  (:use clojure.test))


(deftest test-json-generation
  (testing "without minor units"
    (let [m (cm/amount-of cc/USD 20)]
      (is (= (json/encode m)
             "\"USD20,00\""))))
  (testing "with a currency without minor units"
    (let [m (cm/of-major cc/JPY 2000)]
      (is (= (json/encode m)
             "\"JPY2.000\""))))
  (testing "with minor units"
    (let [m (cm/amount-of cc/USD 20.5)]
      (is (= (json/encode m)
             "\"USD20,50\"")))))
