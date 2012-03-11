(ns clojurewerkz.money.amounts
  (:refer-clojure :exclude [zero?])
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

(defn zero?
  [^Money money]
  (.isZero money))

(defn total
  [^Iterable monies]
  (Money/total monies))

(defn plus
  ([^Money money ^double other]
     (.plus money other)))

(defn plus-major
  ([^Money money ^long amount]
     (.plusMajor money amount)))

(defn plus-minor
  ([^Money money ^long amount]
     (.plusMinor money amount)))

(defn minus
  ([^Money money ^double other]
     (.minus money other)))

(defn minus-major
  ([^Money money ^long amount]
     (.minusMajor money amount)))

(defn minus-minor
  ([^Money money ^long amount]
     (.minusMinor money amount)))


(defn parse
  [^String s]
  (Money/parse s))


(defn negated
  [^Money money]
  (.negated money))

(defn abs
  [^Money money]
  (.abs money))
