(ns clojurewerkz.money.amounts
  "Operations on monetary amounts, including conversion, parsing, and predicates"
  (:refer-clojure :exclude [zero? max min > >= < <=])
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
  "Returns zero monetary amount for the given currency unit"
  [^CurrencyUnit unit]
  (Money/zero unit))

(defn zero?
  "Returns true if the given monetary amount is zero"
  [^Money money]
  (.isZero money))

(defn positive?
  "Returns true if the given monetary amount is positive"
  [^Money money]
  (.isPositive money))

(defn negative?
  "Returns true if the given monetary amount is negative"
  [^Money money]
  (.isNegative money))

(defn positive-or-zero?
  "Returns true if the given monetary amount is positive or zero"
  [^Money money]
  (.isPositiveOrZero money))

(defn negative-or-zero?
  "Returns true if the given monetary amount is negative or zero"
  [^Money money]
  (.isNegativeOrZero money))

(defn ^Money total
  "Sums up multiple monetary amounts"
  [^Iterable monies]
  (Money/total monies))

(defn ^Money plus
  "Adds two monetary amounts together"
  [^Money money other]
  (.plus money other))

(defn ^Money plus-major
  "Adds two monetary amounts together, taking one of them in
   major units (e.g. dollars)"
  [^Money money ^long amount]
  (.plusMajor money amount))

(defn ^Money plus-minor
  "Adds two monetary amounts together, taking one of them in
   minor units (e.g. cents)"
  [^Money money ^long amount]
  (.plusMinor money amount))

(defn ^Money minus
  "Subtracts one monetary amount from another, taking one of them in
   major units (e.g. dollars)"
  [^Money money other]
  (.minus money other))

(defn ^Money minus-major
  "Subtracts one monetary amount from another, taking one of them in
   minor units (e.g. cents)"
  [^Money money ^long amount]
  (.minusMajor money amount))

(defn ^Money minus-minor
  "Subtracts one monetary amount from another"
  [^Money money ^long amount]
  (.minusMinor money amount))

(defn ^Money multiply
  "Multiplies monetary amount by the given number. Takes an optional arounding
   mode which is one of:

   * java.math.RoundingMode instances
   * :floor, :ceiling, :up, :down, :half-up, :half-down, :hald-even that correspond to
     java.math.RoundingMode constants with the same names
   * nil for no rounding"
  ([^Money money ^double multiplier]
     (.multipliedBy money multiplier (cnv/to-rounding-mode nil)))
  ([^Money money ^double multiplier rounding-mode]
     (.multipliedBy money multiplier (cnv/to-rounding-mode rounding-mode))))

(defn ^Money divide
  "Divides monetary amount by the given number. Takes an optional arounding
   mode which is one of:

   * java.math.RoundingMode instances
   * :floor, :ceiling, :up, :down, :half-up, :half-down, :hald-even that correspond to
     java.math.RoundingMode constants with the same names
   * nil for no rounding"
  ([^Money money ^double multiplier]
     (.dividedBy money multiplier (cnv/to-rounding-mode nil)))
  ([^Money money ^double multiplier rounding-mode]
     (.dividedBy money multiplier (cnv/to-rounding-mode rounding-mode))))

(defn ^Money parse
  "Parses a string in the format of [currency code] [amount as double]
   (e.g. GBP 20.00) and returns a monetary amount."
  [^String s]
  (Money/parse s))

(defn ^Money negated
  "Negates the given monetary amount"
  [^Money money]
  (.negated money))

(defn ^Money abs
  "Takes absolute value of the given monetary amount"
  [^Money money]
  (.abs money))

(defn ^Money max
  "Returns the greatest of the given money amounts"
  ([a] a)
  ([^Money a ^Money b]
     (MoneyUtils/max a b))
  ([a b & more]
     (reduce max (max a b) more)))

(defn ^Money min
  "Returns the least of the given money amounts"
  ([a] a)
  ([^Money a ^Money b]
     (MoneyUtils/min a b))
  ([a b & more]
     (reduce min (min a b) more)))

(defn ^boolean >
  "Returns true if the given money amounts are in monotonically decreasing order,
  otherwise false."
  ([a] a)
  ([^Money a ^Money b]
     (.isGreaterThan a b))
  ([a b & more]
     (if (> a b)
       (if (next more)
         (recur a (first more) (next more))
         (> b (first more)))
       false)))

(defn ^boolean >=
  "Returns true if the given money amounts are in monotonically non-increasing order,
  otherwise false."
  ([a] a)
  ([^Money a ^Money b]
     (or (.isGreaterThan a b) (= a b)))
  ([a b & more]
     (if (>= a b)
       (if (next more)
         (recur b (first more) (next more))
         (>= b (first more)))
       false)))

(defn ^boolean <
  "Returns true if the given money amounts are in monotonically decreasing order,
  otherwise false."
  ([a] a)
  ([^Money a ^Money b]
     (.isLessThan a b))
  ([a b & more]
     (if (< a b)
       (if (next more)
         (recur a (first more) (next more))
         (< b (first more)))
       false)))

(defn ^boolean <=
  "Returns true if the given money amounts are in monotonically non-decreasing order,
  otherwise false."
  ([a] a)
  ([^Money a ^Money b]
     (or (.isLessThan a b) (= a b)))
  ([a b & more]
     (if (<= a b)
       (if (next more)
         (recur a (first more) (next more))
         (<= b (first more)))
       false)))

(defn ^Money round
  "Rounds monetary amount using the given scale and rounding mode.

   Scale is one of -1, 0, 1, 2, 3 but no greater than the currency's scale.

   Rounding mode should be one of:

   * java.math.RoundingMode instances
   * :floor, :ceiling, :up, :down, :half-up, :half-down, :hald-even that correspond to
     java.math.RoundingMode constants with the same names
   * nil for no rounding"
  [^Money money ^long scale rounding-mode]
  (.rounded money scale (cnv/to-rounding-mode rounding-mode)))

(defn ^Money convert-to
  "Converts monetary amount in one currency to monetary amount in a different
   currency using the provided multiplier (exchange rate) and rounding mode.

   Multipler should be either a java.math.BigDecimal or a double.

   Rounding mode should be one of:

   * java.math.RoundingMode instances
   * :floor, :ceiling, :up, :down, :half-up, :half-down, :hald-even that correspond to
     java.math.RoundingMode constants with the same names
   * nil for no rounding"
  [^Money money ^CurrencyUnit currency multiplier rounding-mode]
  (.convertedTo money currency (BigDecimal/valueOf multiplier) (cnv/to-rounding-mode rounding-mode)))
