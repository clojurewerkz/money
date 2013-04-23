(ns clojurewerkz.money.json-test
  (:require [clojurewerkz.money.amounts    :as ams]
            [clojurewerkz.money.currencies :as cu]
            clojurewerkz.money.json
            [cheshire.core                 :as json])
  (:use clojure.test)
  (:import java.util.Locale))


(deftest test-json-serialization
  (are [formatted money] (is (= formatted (json/generate-string money)))
       "\"€10,00\""   (ams/amount-of cu/EUR 10.00)
       "\"GBP10,00\"" (ams/amount-of cu/GBP 10.00)
       "\"USD10,00\"" (ams/amount-of cu/USD 10.00)))
