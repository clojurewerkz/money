(ns clojurewerkz.money.amounts-test
  (:use clojure.test
        [clojurewerkz.money.amounts :exclude [zero?] :as amounts])
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



(deftest test-amount-of-with-currency-unit-and-amount-in-major-units
  (are [cu amount bdec] (let [^Money money (of-major cu amount)]
                          (is (= (.getCurrencyUnit money) cu))
                          (is (= bdec (.getAmount money))))
       CurrencyUnit/USD 30 30.00M
       CurrencyUnit/GBP 40 40.00M
       CurrencyUnit/JPY 50 50.M))


(deftest test-amount-of-with-currency-unit-and-amount-in-minor-units
  (are [cu amount bdec] (let [^Money money (of-minor cu amount)]
                          (is (= (.getCurrencyUnit money) cu))
                          (is (= bdec (.getAmount money))))
       CurrencyUnit/USD 2595 25.95M
       CurrencyUnit/GBP 4012 40.12M
       CurrencyUnit/JPY 5000 5000M))


(deftest test-zero-amount
  (are [cu bdec] (let [^Money money (zero cu)]
                          (is (= (.getCurrencyUnit money) cu))
                          (is (= bdec (.getAmount money)))
                          (is (amounts/zero? money)))
       CurrencyUnit/USD 0.00M
       CurrencyUnit/GBP 0.00M
       CurrencyUnit/JPY 0M))

(deftest test-money-total
  (let [cu CurrencyUnit/EUR
        a  (amount-of cu 15.00)
        b  (of-major  cu 10)
        c  (of-minor  cu 1300)
        d  (zero      cu)
        ^Money t  (total [a b c d])]
    (is (= (.getCurrencyUnit t) cu))
    (is (= 38.00M (.getAmount t)))))


(deftest test-parsing-of-monetary-amounts
  (are [s money] (is (= (parse s) money))
       "USD 0" (zero CurrencyUnit/USD)
       "EUR 0" (zero CurrencyUnit/EUR)
       "JPY 0" (zero CurrencyUnit/JPY)
       "GBP 0" (zero CurrencyUnit/GBP)
       "USD 10" (of-major CurrencyUnit/USD 10)
       "EUR 11" (of-major CurrencyUnit/EUR 11)
       "JPY 12" (of-major CurrencyUnit/JPY 12)
       "GBP 13" (of-major CurrencyUnit/GBP 13)
       "USD +10" (of-major CurrencyUnit/USD 10)
       "EUR +11" (of-major CurrencyUnit/EUR 11)
       "JPY +12" (of-major CurrencyUnit/JPY 12)
       "GBP +13" (of-major CurrencyUnit/GBP 13)
       "USD 20.05"  (of-minor CurrencyUnit/USD 2005)
       "EUR 21.13"  (of-minor CurrencyUnit/EUR 2113)
       "JPY 323"    (of-minor CurrencyUnit/JPY 323)
       "JPY 323.00" (of-minor CurrencyUnit/JPY 323)
       "GBP 33.78"  (of-minor CurrencyUnit/GBP 3378)
       "USD +20.05"  (of-minor CurrencyUnit/USD 2005)
       "EUR +21.13"  (of-minor CurrencyUnit/EUR 2113)
       "JPY +323"    (of-minor CurrencyUnit/JPY 323)
       "JPY +323.00" (of-minor CurrencyUnit/JPY 323)
       "GBP +33.78"  (of-minor CurrencyUnit/GBP 3378)))

(deftest test-addition-of-two-monetary-values
  (let [cu CurrencyUnit/EUR
        a  (amount-of cu 15.00)
        b  10.00M
        c  (of-minor  cu 1300)
        d  0.00M
        ^Money t1  (amounts/plus a b)
        ^Money t2  (amounts/plus c d)]
    (is (= (.getCurrencyUnit t1) cu))
    (is (= (.getCurrencyUnit t2) cu))
    (is (= 25.00M (.getAmount t1)))
    (is (= 13.00M (.getAmount t2)))))

(deftest test-addition-with-major-units
  (let [cu CurrencyUnit/EUR
        a  (amount-of cu 15.00)
        b  10
        c  (of-minor  cu 1300)
        d  0
        ^Money t1  (amounts/plus-major a b)
        ^Money t2  (amounts/plus-major c d)]
    (is (= (.getCurrencyUnit t1) cu))
    (is (= (.getCurrencyUnit t2) cu))
    (is (= 25.00M (.getAmount t1)))
    (is (= 13.00M (.getAmount t2)))))

(deftest test-addition-with-minor-units
  (let [cu CurrencyUnit/EUR
        a  (amount-of cu 15.00)
        b  1000
        c  (of-minor  cu 1300)
        d  0
        ^Money t1  (amounts/plus-minor a b)
        ^Money t2  (amounts/plus-minor c d)]
    (is (= (.getCurrencyUnit t1) cu))
    (is (= (.getCurrencyUnit t2) cu))
    (is (= 25.00M (.getAmount t1)))
    (is (= 13.00M (.getAmount t2)))))

(deftest test-substraction-of-two-monetary-values
  (let [cu CurrencyUnit/EUR
        a  (amount-of cu 15.00)
        b  10.00M
        c  (of-minor  cu 1300)
        d  0.00M
        ^Money t1  (amounts/minus a b)
        ^Money t2  (amounts/minus c d)]
    (is (= (.getCurrencyUnit t1) cu))
    (is (= (.getCurrencyUnit t2) cu))
    (is (= 5.00M  (.getAmount t1)))
    (is (= 13.00M (.getAmount t2)))))

(deftest test-substraction-with-major-units
  (let [cu CurrencyUnit/EUR
        a  (amount-of cu 15.00)
        b  10
        c  (of-minor  cu 1300)
        d  0
        ^Money t1  (amounts/minus-major a b)
        ^Money t2  (amounts/minus-major c d)]
    (is (= (.getCurrencyUnit t1) cu))
    (is (= (.getCurrencyUnit t2) cu))
    (is (= 5.00M  (.getAmount t1)))
    (is (= 13.00M (.getAmount t2)))))

(deftest test-substraction-with-minor-units
  (let [cu CurrencyUnit/EUR
        a  (amount-of cu 15.00)
        b  1000
        c  (of-minor  cu 1300)
        d  0
        ^Money t1  (amounts/minus-minor a b)
        ^Money t2  (amounts/minus-minor c d)]
    (is (= (.getCurrencyUnit t1) cu))
    (is (= (.getCurrencyUnit t2) cu))
    (is (= 5.00M  (.getAmount t1)))
    (is (= 13.00M (.getAmount t2)))))
