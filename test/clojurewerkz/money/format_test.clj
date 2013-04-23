(ns clojurewerkz.money.format-test
  (:require [clojurewerkz.money.amounts    :as ams]
            [clojurewerkz.money.currencies :as cu]
            [clojurewerkz.money.format     :as fmt])
  (:use clojure.test)
  (:import java.util.Locale))


(deftest test-formatting-with-default-formatter-and-provided-locale
  (are [formatted money] (is (= formatted (fmt/format money (Locale/UK))))
       "€10.00"   (ams/amount-of cu/EUR 10.00)
       "£10.00"   (ams/amount-of cu/GBP 10.00)
       "USD10.00" (ams/amount-of cu/USD 10.00)))
