(ns clojurewerkz.money.lib-integration-test
  (:require [monger.conversion :as cnv]
            [cheshire.core :as json]
            [clojurewerkz.money.monger  :as cmm]
            [clojurewerkz.money.amounts :as cm]
            [clojurewerkz.money.currencies :as cc])
  (:use clojure.test))


(deftest test-json-generation
  (testing "without minor units"
    (let [m (cm/amount-of cc/USD 20)]
      (is (= (json/encode m)
             "\"USD 20.0\""))))
  (testing "with a currency without minor units"
    (let [m (cm/of-major cc/JPY 2000)]
      (is (= (json/encode m)
             "\"JPY 2000\""))))
  (testing "with minor units"
    (let [m (cm/amount-of cc/USD 20.5)]
      (is (= (json/encode m)
             "\"USD 20.50\"")))))


(deftest test-monger-bson-generation
  (let [m   (cm/amount-of cc/USD 20)
        dbo (cnv/to-db-object m)]
    (is (= 2000  (get dbo "amount-in-minor-units")))
    (is (= "USD" (get dbo "currency-unit")))))

(deftest test-monger-bson-loading
  (let [m   (cm/amount-of cc/USD 20)
        dbo (cnv/to-db-object m)]
    (is (= m (cmm/from-stored-map dbo)))))
