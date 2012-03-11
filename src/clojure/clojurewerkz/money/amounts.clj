(ns clojurewerkz.money.amounts
  (:import [org.joda.money Money BigMoney CurrencyUnit]
           [java.math RoundingMode]))

;;
;; API
;;

(defn amount-of
  ([^CurrencyUnit unit ^double amount]
     (Money/of unit amount))
  ([^CurrencyUnit unit ^double amount ^RoundingMode rm]
     (Money/of unit amount rm)))

(defn of-major
  [^CurrencyUnit unit ^long amount]
  (Money/ofMajor unit amount))


(defn of-minor
  [^CurrencyUnit unit ^long amount]
  (Money/ofMinor unit amount))

(defn zero
  [^CurrencyUnit unit]
  (Money/zero unit))

(defn total
  [monies]
  (Money/total monies))
