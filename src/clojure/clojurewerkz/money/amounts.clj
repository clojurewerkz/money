(ns clojurewerkz.money.amounts
  "Operations on monetary amounts, including predicates and parsing"
  (:refer-clojure :exclude [zero? max min])
  (:require [clojurewerkz.money.conversion :as cnv])
  (:import [org.joda.money Money BigMoney CurrencyUnit MoneyUtils]
           [java.math RoundingMode BigDecimal]))

;;
;; API
;;

(defn ^Money amount-of
  ([^CurrencyUnit unit ^double amount]
     (Money/of unit amount))
  ([^CurrencyUnit unit ^double amount ^RoundingMode rm]
     (Money/of unit amount rm)))

(defn ^Money of-major
  [^CurrencyUnit unit ^long amount]
  (Money/ofMajor unit amount))


(defn ^Money of-minor
  [^CurrencyUnit unit ^long amount]
  (Money/ofMinor unit amount))

(defn ^Money zero
  [^CurrencyUnit unit]
  (Money/zero unit))

(defn zero?
  [^Money money]
  (.isZero money))

(defn positive?
  [^Money money]
  (.isPositive money))

(defn negative?
  [^Money money]
  (.isNegative money))

(defn positive-or-zero?
  [^Money money]
  (.isPositiveOrZero money))

(defn negative-or-zero?
  [^Money money]
  (.isNegativeOrZero money))

(defn ^Money total
  [^Iterable monies]
  (Money/total monies))

(defn ^Money plus
  [^Money money ^double other]
  (.plus money other))

(defn ^Money plus-major
  [^Money money ^long amount]
  (.plusMajor money amount))

(defn ^Money plus-minor
  [^Money money ^long amount]
  (.plusMinor money amount))

(defn ^Money minus
  [^Money money ^double other]
  (.minus money other))

(defn ^Money minus-major
  [^Money money ^long amount]
  (.minusMajor money amount))

(defn ^Money minus-minor
  [^Money money ^long amount]
  (.minusMinor money amount))

(defn ^Money multiply
  [^Money money ^double multiplier]
  (.multipliedBy money multiplier))

(defn ^Money divide
  ([^Money money ^double multiplier]
     (.dividedBy money multiplier (cnv/to-rounding-mode nil)))
  ([^Money money ^double multiplier rounding-mode]
     (.dividedBy money multiplier (cnv/to-rounding-mode rounding-mode))))

(defn ^Money parse
  [^String s]
  (Money/parse s))


(defn ^Money negated
  [^Money money]
  (.negated money))

(defn ^Money abs
  [^Money money]
  (.abs money))

(defn ^Money max
  "Returns the greater of the two money amounts"
  [^Money a ^Money b]
  (MoneyUtils/max a b))

(defn ^Money min
  "Returns the lesser of the two money amounts"
  [^Money a ^Money b]
  (MoneyUtils/min a b))

(defn ^Money round
  [^Money money ^long scale rounding-mode]
  (.rounded money scale (cnv/to-rounding-mode rounding-mode)))

(defn ^Money convert-to
  [^Money money ^CurrencyUnit currency multiplier rounding-mode]
  (.convertedTo money currency (BigDecimal/valueOf multiplier) (cnv/to-rounding-mode rounding-mode)))
