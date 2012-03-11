(ns clojurewerkz.money.amounts-test
  (:use clojure.test
        clojurewerkz.money.amounts)
  (:import [org.joda.money CurrencyUnit Money]
           [java.math RoundingMode]))


(println (str "Using Clojure version " *clojure-version*))

(deftest test-amount-of-with-currency-unit-and-double
  (let [^Money money (amount-of CurrencyUnit/EUR 10.00)]
    (is (= (.getCurrencyUnit money) CurrencyUnit/EUR))
    (is (= 10.00M (.getAmount money)))))

(deftest test-amount-of-with-currency-unit-and-double-and-rounding-mode
  (let [^Money money (amount-of CurrencyUnit/EUR 10.0333333 RoundingMode/DOWN)]
    (is (= (.getCurrencyUnit money) CurrencyUnit/EUR))
    (is (= 10.03M (.getAmount money)))))
