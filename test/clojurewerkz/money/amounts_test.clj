(ns clojurewerkz.money.amounts-test
  (:require [clojurewerkz.money.amounts    :as ams]
            [clojurewerkz.money.currencies :as cu])
  (:use clojure.test)
  (:import [org.joda.money CurrencyUnit Money]
           java.math.RoundingMode))


(println (str "Using Clojure version " *clojure-version*))

(deftest test-amount-of-with-currency-unit-and-double
  (let [^Money money (ams/amount-of CurrencyUnit/EUR 10.00)]
    (is (= (.getCurrencyUnit money) CurrencyUnit/EUR))
    (is (= 10.00M (.getAmount money)))))

(deftest test-amount-of-with-currency-unit-and-double-and-rounding-mode
  (let [^Money money (ams/amount-of CurrencyUnit/EUR 10.0333333 RoundingMode/DOWN)]
    (is (= (.getCurrencyUnit money) CurrencyUnit/EUR))
    (is (= 10.03M (.getAmount money)))))



(deftest test-amount-of-with-currency-unit-and-amount-in-major-units
  (are [cu amount bdec] (let [^Money money (ams/of-major cu amount)]
                          (is (= (.getCurrencyUnit money) cu))
                          (is (= bdec (.getAmount money))))
       CurrencyUnit/USD 30 30.00M
       CurrencyUnit/GBP 40 40.00M
       CurrencyUnit/JPY 50 50.M))


(deftest test-amount-of-with-currency-unit-and-amount-in-minor-units
  (are [cu amount bdec] (let [^Money money (ams/of-minor cu amount)]
                          (is (= (ams/currency-of money) cu))
                          (is (= bdec (.getAmount money))))
       CurrencyUnit/USD 2595 25.95M
       CurrencyUnit/GBP 4012 40.12M
       CurrencyUnit/JPY 5000 5000M))

(deftest test-major-units-of
  (are [cu amount expected] (let [^Money money (ams/of-minor cu amount)]
                              (is (= (ams/currency-of money) cu))
                              (is (= expected (ams/major-of money))))
       CurrencyUnit/USD    2595 25
       CurrencyUnit/GBP    4012 40
       (cu/for-code "VND") 401  401
       CurrencyUnit/JPY    5000 5000))

(deftest test-minor-units-of
  (are [cu amount expected] (let [^Money money (ams/of-minor cu amount)]
                              (is (= (ams/currency-of money) cu))
                              (is (= expected (ams/minor-of money))))
       CurrencyUnit/USD    2595 2595
       (cu/for-code "VND") 401  401
       CurrencyUnit/JPY    5000 5000))


(deftest test-zero-amount
  (are [cu bdec] (let [^Money money (ams/zero cu)]
                   (is (= (.getCurrencyUnit money) cu))
                   (is (= bdec (.getAmount money)))
                   (is (ams/zero? money))
                   (is (not (ams/positive? money))))
       CurrencyUnit/USD 0.00M
       CurrencyUnit/GBP 0.00M
       CurrencyUnit/JPY 0M))

(deftest test-positive?
  (are [cu bdec] (let [^Money money (ams/of-minor cu bdec)]
                   (is (ams/positive? money)))
       CurrencyUnit/USD 100
       CurrencyUnit/GBP 200
       CurrencyUnit/JPY 500))

(deftest test-positive-or-zero?
  (are [cu bdec] (let [^Money money (ams/of-minor cu bdec)]
                   (is (ams/positive-or-zero? money)))
       CurrencyUnit/USD 100
       CurrencyUnit/GBP 200
       CurrencyUnit/JPY 500
       CurrencyUnit/EUR 0))

(deftest test-negative?
  (are [cu bdec] (let [^Money money (ams/of-minor cu bdec)]
                   (is (ams/negative? money)))
       CurrencyUnit/USD -100
       CurrencyUnit/GBP -200
       CurrencyUnit/JPY -500))

(deftest test-negative-or-zero?
  (are [cu bdec] (let [^Money money (ams/of-minor cu bdec)]
                   (is (ams/negative-or-zero? money)))
       CurrencyUnit/USD -100
       CurrencyUnit/GBP -200
       CurrencyUnit/JPY -500
       CurrencyUnit/EUR 0))

(deftest test-money-total
  (let [cu CurrencyUnit/EUR
        a  (ams/amount-of cu 15.00)
        b  (ams/of-major  cu 10)
        c  (ams/of-minor  cu 1300)
        d  (ams/zero      cu)
        ^Money t  (ams/total [a b c d])]
    (is (= (.getCurrencyUnit t) cu))
    (is (= 38.00M (.getAmount t)))))


(deftest test-parsing-of-monetary-amounts
  (are [s money] (is (= (ams/parse s) money))
       "USD 0" (ams/zero CurrencyUnit/USD)
       "EUR 0" (ams/zero CurrencyUnit/EUR)
       "JPY 0" (ams/zero CurrencyUnit/JPY)
       "GBP 0" (ams/zero CurrencyUnit/GBP)
       "USD 10" (ams/of-major CurrencyUnit/USD 10)
       "EUR 11" (ams/of-major CurrencyUnit/EUR 11)
       "JPY 12" (ams/of-major CurrencyUnit/JPY 12)
       "GBP 13" (ams/of-major CurrencyUnit/GBP 13)
       "USD +10" (ams/of-major CurrencyUnit/USD 10)
       "EUR +11" (ams/of-major CurrencyUnit/EUR 11)
       "JPY +12" (ams/of-major CurrencyUnit/JPY 12)
       "GBP +13" (ams/of-major CurrencyUnit/GBP 13)
       "USD 20.05"  (ams/of-minor CurrencyUnit/USD 2005)
       "EUR 21.13"  (ams/of-minor CurrencyUnit/EUR 2113)
       "JPY 323"    (ams/of-minor CurrencyUnit/JPY 323)
       "JPY 323.00" (ams/of-minor CurrencyUnit/JPY 323)
       "GBP 33.78"  (ams/of-minor CurrencyUnit/GBP 3378)
       "USD +20.05"  (ams/of-minor CurrencyUnit/USD 2005)
       "EUR +21.13"  (ams/of-minor CurrencyUnit/EUR 2113)
       "JPY +323"    (ams/of-minor CurrencyUnit/JPY 323)
       "JPY +323.00" (ams/of-minor CurrencyUnit/JPY 323)
       "GBP +33.78"  (ams/of-minor CurrencyUnit/GBP 3378)
       "USD -20.05"  (ams/of-minor CurrencyUnit/USD -2005)
       "EUR -21.13"  (ams/of-minor CurrencyUnit/EUR -2113)
       "JPY -323"    (ams/of-minor CurrencyUnit/JPY -323)
       "JPY -323.00" (ams/of-minor CurrencyUnit/JPY -323)
       "GBP -33.78"  (ams/of-minor CurrencyUnit/GBP -3378)))

(deftest test-addition-of-monetary-values
  (let [cu CurrencyUnit/EUR
        a  (ams/amount-of cu 15.00)
        b  10.00M
        c  (ams/of-minor  cu 1300)
        d  0.00M
        ^Money t1  (ams/plus a b)
        ^Money t2  (ams/plus c d)]

    (is (= (.getCurrencyUnit t1) cu))
    (is (= (.getCurrencyUnit t2) cu))
    (is (= 25.00M (.getAmount t1)))
    (is (= 13.00M (.getAmount t2)))
    (is (= 38.00M (.getAmount (ams/plus t1 t2))) "add two Money values together")
    (is (= 39.00M (.getAmount (ams/plus t2 [t2 t2]))) "add a collection of Money values")))

(deftest test-addition-with-major-units
  (let [cu CurrencyUnit/EUR
        a  (ams/amount-of cu 15.00)
        b  10
        c  (ams/of-minor  cu 1300)
        d  0
        ^Money t1  (ams/plus-major a b)
        ^Money t2  (ams/plus-major c d)]
    (is (= (.getCurrencyUnit t1) cu))
    (is (= (.getCurrencyUnit t2) cu))
    (is (= 25.00M (.getAmount t1)))
    (is (= 13.00M (.getAmount t2)))))

(deftest test-addition-with-minor-units
  (let [cu CurrencyUnit/EUR
        a  (ams/amount-of cu 15.00)
        b  1000
        c  (ams/of-minor  cu 1300)
        d  0
        ^Money t1  (ams/plus-minor a b)
        ^Money t2  (ams/plus-minor c d)]
    (is (= (.getCurrencyUnit t1) cu))
    (is (= (.getCurrencyUnit t2) cu))
    (is (= 25.00M (.getAmount t1)))
    (is (= 13.00M (.getAmount t2)))))

(deftest test-substraction-of-monetary-values
  (let [cu CurrencyUnit/EUR
        a  (ams/amount-of cu 15.00)
        b  10.00M
        c  (ams/of-minor  cu 1300)
        d  0.00M
        ^Money t1  (ams/minus a b)
        ^Money t2  (ams/minus c d)]
    (is (= (.getCurrencyUnit t1) cu))
    (is (= (.getCurrencyUnit t2) cu))
    (is (= 5.00M  (.getAmount t1)))
    (is (= 13.00M (.getAmount t2)))
    (is (= 8.00M (.getAmount (ams/minus t2 t1))) "subtract one Money value from another")
    (is (= 3.00M (.getAmount (ams/minus t2 [t1 t1]))) "subtract multiple Money values from another")))

(deftest test-substraction-with-major-units
  (let [cu CurrencyUnit/EUR
        a  (ams/amount-of cu 15.00)
        b  10
        c  (ams/of-minor  cu 1300)
        d  0
        ^Money t1  (ams/minus-major a b)
        ^Money t2  (ams/minus-major c d)]
    (is (= (.getCurrencyUnit t1) cu))
    (is (= (.getCurrencyUnit t2) cu))
    (is (= 5.00M  (.getAmount t1)))
    (is (= 13.00M (.getAmount t2)))))

(deftest test-substraction-with-minor-units
  (let [cu CurrencyUnit/EUR
        a  (ams/amount-of cu 15.00)
        b  1000
        c  (ams/of-minor  cu 1300)
        d  0
        ^Money t1  (ams/minus-minor a b)
        ^Money t2  (ams/minus-minor c d)]
    (is (= (.getCurrencyUnit t1) cu))
    (is (= (.getCurrencyUnit t2) cu))
    (is (= 5.00M  (.getAmount t1)))
    (is (= 13.00M (.getAmount t2)))))


(deftest test-negation
  (let [cu           CurrencyUnit/USD
        ^Money money (ams/negated (ams/amount-of cu 10.00M))]
    (is (= cu (.getCurrencyUnit money)))
    (is (= -10.00M (.getAmount money)))))


(deftest test-taking-an-absolute-value
  (let [cu            CurrencyUnit/USD
        ^Money money1 (ams/amount-of cu 10.00M)
        ^Money money2 (ams/negated money1)]
    (is (= cu (.getCurrencyUnit money1)))
    (is (= cu (.getCurrencyUnit money2)))
    (is (= 10.00M (.getAmount ^Money (ams/abs money1))))
    (is (= 10.00M (.getAmount ^Money (ams/abs money2))))))


(deftest test-max
  (is (= (ams/amount-of cu/USD 10) (ams/max (ams/amount-of cu/USD 10))))
  (is (= (ams/amount-of cu/GBP 3) (ams/max (ams/amount-of cu/GBP 3))))
  (are [a b c] (is (= c (ams/max a b)))
       (ams/amount-of cu/USD 10) (ams/amount-of cu/USD 20) (ams/amount-of cu/USD 20)
       (ams/amount-of cu/GBP 30) (ams/amount-of cu/GBP 10) (ams/amount-of cu/GBP 30))
  (are [a b c d] (is (= d (ams/max a b c)))
       (ams/amount-of cu/USD 5) (ams/amount-of cu/USD 20) (ams/amount-of cu/USD 10) (ams/amount-of cu/USD 20)
       (ams/amount-of cu/GBP 8) (ams/amount-of cu/GBP 3) (ams/amount-of cu/GBP 6) (ams/amount-of cu/GBP 8)))

(deftest test-min
  (is (= (ams/amount-of cu/USD 10) (ams/min (ams/amount-of cu/USD 10))))
  (is (= (ams/amount-of cu/GBP 3) (ams/min (ams/amount-of cu/GBP 3))))
  (are [a b c] (is (= c (ams/min a b)))
       (ams/amount-of cu/USD 10) (ams/amount-of cu/USD 20) (ams/amount-of cu/USD 10)
       (ams/amount-of cu/GBP 30) (ams/amount-of cu/GBP 10) (ams/amount-of cu/GBP 10))
  (are [a b c d] (is (= d (ams/min a b c)))
       (ams/amount-of cu/USD 5) (ams/amount-of cu/USD 20) (ams/amount-of cu/USD 10) (ams/amount-of cu/USD 5)
       (ams/amount-of cu/GBP 8) (ams/amount-of cu/GBP 10) (ams/amount-of cu/GBP 10) (ams/amount-of cu/GBP 8)))

(deftest test-gt
  (is (ams/> (ams/amount-of cu/USD 10)))
  (is (ams/> (ams/amount-of cu/GBP 4)))
  (is (ams/> (ams/amount-of cu/USD 10) (ams/amount-of cu/USD 8)))
  (is (ams/> (ams/amount-of cu/GBP 4) (ams/amount-of cu/GBP 2)))
  (is (not (ams/> (ams/amount-of cu/USD 8) (ams/amount-of cu/USD 8))))
  (is (ams/> (ams/amount-of cu/USD 8) (ams/amount-of cu/USD 7)))
  (is (ams/> (ams/amount-of cu/GBP 3) (ams/amount-of cu/GBP 2)))
  (is (ams/> (ams/amount-of cu/GBP 3) (ams/amount-of cu/GBP 2) (ams/amount-of cu/GBP 1))))

(deftest test-gte
  (is (ams/>= (ams/amount-of cu/USD 10)))
  (is (ams/>= (ams/amount-of cu/GBP 4)))
  (is (ams/>= (ams/amount-of cu/USD 10) (ams/amount-of cu/USD 10)))
  (is (ams/>= (ams/amount-of cu/USD 10) (ams/amount-of cu/USD 9)))
  (is (ams/>= (ams/amount-of cu/GBP 4) (ams/amount-of cu/GBP 2)))
  (is (ams/>= (ams/amount-of cu/USD 8) (ams/amount-of cu/USD 8)))
  (is (ams/>= (ams/amount-of cu/USD 8) (ams/amount-of cu/USD 7)))
  (is (ams/>= (ams/amount-of cu/GBP 3) (ams/amount-of cu/GBP 2)))
  (is (ams/>= (ams/amount-of cu/GBP 3) (ams/amount-of cu/GBP 2) (ams/amount-of cu/GBP 1) (ams/amount-of cu/GBP 1))))

(deftest test-lt
  (is (ams/< (ams/amount-of cu/USD 10)))
  (is (ams/< (ams/amount-of cu/GBP 4)))
  (is (ams/< (ams/amount-of cu/USD 8) (ams/amount-of cu/USD 10)))
  (is (ams/< (ams/amount-of cu/GBP 1) (ams/amount-of cu/GBP 4)))
  (is (not (ams/< (ams/amount-of cu/USD 8) (ams/amount-of cu/USD 8))))
  (is (ams/< (ams/amount-of cu/USD 3) (ams/amount-of cu/USD 4)))
  (is (ams/< (ams/amount-of cu/GBP 2) (ams/amount-of cu/GBP 4)))
  (is (ams/< (ams/amount-of cu/GBP 1) (ams/amount-of cu/GBP 2) (ams/amount-of cu/GBP 30))))

(deftest test-lte
  (is (ams/<= (ams/amount-of cu/USD 10)))
  (is (ams/<= (ams/amount-of cu/GBP 4)))
  (is (ams/<= (ams/amount-of cu/USD 8) (ams/amount-of cu/USD 10)))
  (is (ams/<= (ams/amount-of cu/GBP 1) (ams/amount-of cu/GBP 4)))
  (is (ams/<= (ams/amount-of cu/USD 8) (ams/amount-of cu/USD 8)))
  (is (ams/<= (ams/amount-of cu/USD 3) (ams/amount-of cu/USD 4)))
  (is (ams/<= (ams/amount-of cu/GBP 2) (ams/amount-of cu/GBP 4)))
  (is (ams/<= (ams/amount-of cu/GBP 1) (ams/amount-of cu/GBP 2) (ams/amount-of cu/GBP 30))))

(deftest test-multiplication
  (let [oa (ams/amount-of cu/USD 100)
        ma (ams/multiply oa 2)]
    (is (= ma (ams/amount-of cu/USD 200)))))

(deftest test-multiplication-with-floor-rounding-mode
  (let [oa (ams/amount-of cu/USD 45)
        ma (ams/multiply oa 10.1 :floor)]
    (is (= ma (ams/amount-of cu/USD 454.50)))))

(deftest test-division
  (let [oa (ams/amount-of cu/USD 100)
        ma (ams/divide oa 2)]
    (is (= ma (ams/amount-of cu/USD 50)))))

(deftest test-division-with-floor-rounding-mode
  (let [oa (ams/amount-of cu/USD 100.01)
        ma (ams/divide oa 2 :floor)]
    (is (= ma (ams/amount-of cu/USD 50)))))

(deftest test-round
  (testing "with scale -1 and flooring rounding mode"
    (let [oa (ams/amount-of cu/USD 40.01)
          ma (ams/round oa -1 :floor)]
      (is (= ma (ams/amount-of cu/USD 40))))
    (testing "with scale -1 and up rounding mode"
      (let [oa (ams/amount-of cu/USD 40.01)
            ma (ams/round oa -1 :up)]
        (is (= ma (ams/amount-of cu/USD 50)))))
    (testing "with scale 0 and flooring rounding mode"
      (let [oa (ams/amount-of cu/USD 45.24)
            ma (ams/round oa 0 :floor)]
        (is (= ma (ams/amount-of cu/USD 45)))))
    (testing "with scale 0 and up rounding mode"
      (let [oa (ams/amount-of cu/USD 45.24)
            ma (ams/round oa 0 :up)]
        (is (= ma (ams/amount-of cu/USD 46)))))
    (testing "with scale 1 and flooring rounding mode"
      (let [oa (ams/amount-of cu/USD 45.24)
            ma (ams/round oa 1 :floor)]
        (is (= ma (ams/amount-of cu/USD 45.20)))))
    (testing "with scale 1 and up rounding mode"
      (let [oa (ams/amount-of cu/USD 45.24)
            ma (ams/round oa 1 :up)]
        (is (= ma (ams/amount-of cu/USD 45.30)))))
    (testing "with scale 2 and flooring rounding mode"
      (let [oa (ams/amount-of cu/USD 45.24)
            ma (ams/round oa 2 :floor)]
        (is (= ma (ams/amount-of cu/USD 45.24)))))
    (testing "with scale 2 and up rounding mode"
      (let [oa (ams/amount-of cu/USD 45.24)
            ma (ams/round oa 1 :up)]
        (is (= ma (ams/amount-of cu/USD 45.30)))))))

(deftest test-convert-to
  (testing "with Double rate"
    (let [a    (ams/amount-of cu/USD 99.98)
          b    (ams/amount-of cu/GBP 65.65)
          rate 1.523]
      (is (= a (ams/convert-to b cu/USD rate :down)))))
  (testing "with BigDecimal rate"
    (let [a    (ams/amount-of cu/USD 99.98)
          b    (ams/amount-of cu/GBP 65.65)
          rate 1.523M]
      (is (= a (ams/convert-to b cu/USD rate :down))))))
