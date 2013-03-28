(ns clojurewerkz.money.currencies-test
  (:require [clojurewerkz.money.currencies :as cu])
  (:use clojure.test)
  (:import [org.joda.money Money CurrencyUnit]))


(deftest test-currency-aliases
  (are [alias canonical] (is (= alias canonical))
    cu/USD CurrencyUnit/USD
    cu/GBP CurrencyUnit/GBP
    cu/JPY CurrencyUnit/JPY
    cu/NOK (CurrencyUnit/of "NOK")
    cu/RUB (CurrencyUnit/of "RUB")
    (cu/of "NOK") (CurrencyUnit/of "NOK")
    (cu/for-code "NOK") (CurrencyUnit/of "NOK")))


(deftest test-currency-numeric-codes
  (are [code unit] (is (= unit (cu/of-numeric-code code) (cu/of-numeric-code code)))
    756 cu/CHF
    643 cu/RUB))

(deftest test-currency-countries
  (are [code unit] (is (= unit (cu/of-country code) (cu/for-country code)))
    "CH" cu/CHF
    "RU" cu/RUB
    "LV" cu/LVL))

(deftest test-pseudo-currency
  (is (cu/pseudo-currency? (cu/of "XXX")))
  (is (not (cu/pseudo-currency? (cu/of "JPY")))))
