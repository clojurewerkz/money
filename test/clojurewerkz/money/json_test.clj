(ns clojurewerkz.money.json-test
  (:require [clojurewerkz.money.amounts :as ma]
            [clojurewerkz.money.currencies :as cu]
            clojurewerkz.money.json
            [clojure.test :refer :all]
            [cheshire.core :as json]))

(deftest test-currency-encoder
  (testing "Currency units are encoded in JSON as ISO-4217 codes."
    (is (= "\"GBP\"" (json/generate-string (cu/for-code "GBP"))))))

(deftest test-money-encoder
  (testing "USD is encoded in JSON with an ISO-4217 code followed by the amount."
    (is (= "\"USD 110.00\"" (json/generate-string (ma/parse "USD 110"))))
    (is (= "\"JPY 100\"" (json/generate-string (ma/parse "JPY 100"))))))
