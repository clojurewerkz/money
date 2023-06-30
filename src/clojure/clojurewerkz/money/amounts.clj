;; This source code is dual-licensed under the Apache License, version
;; 2.0, and the Eclipse Public License, version 1.0.
;;
;; The APL v2.0:
;;
;; ----------------------------------------------------------------------------------
;; Copyright (c) 2012-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; Licensed under the Apache License, Version 2.0 (the "License");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at
;;
;;     http://www.apache.org/licenses/LICENSE-2.0
;;
;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.
;; ----------------------------------------------------------------------------------
;;
;; The EPL v1.0:
;;
;; ----------------------------------------------------------------------------------
;; Copyright (c) 2012-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team.
;; All rights reserved.
;;
;; This program and the accompanying materials are made available under the terms of
;; the Eclipse Public License Version 1.0,
;; which accompanies this distribution and is available at
;; http://www.eclipse.org/legal/epl-v10.html.
;; ----------------------------------------------------------------------------------

(ns clojurewerkz.money.amounts
  "Operations on monetary amounts, including conversion, parsing, and predicates"
  (:refer-clojure :exclude [zero? max min > >= < <= abs])
  (:require [clojurewerkz.money.conversion :as cnv])
  (:import [org.joda.money Money BigMoney CurrencyUnit MoneyUtils]
           [java.math RoundingMode]))

;;
;; API
;;

(defn ^Money amount-of
  "Instantiates a monetary amount of given currency unit and amount as double.
   N.B. this function should not be used for currencies without minor units (e.g. JPY)."
  ([^CurrencyUnit unit ^double amount]
     (Money/of unit amount))
  ([^CurrencyUnit unit ^double amount ^RoundingMode rm]
     (Money/of unit amount rm)))

(defn ^Money of-major
  "Instantiates a monetary amount of given currency unit and major unit amount.
   This function should be used for currencies without minor units (e.g. JPY)."
  [^CurrencyUnit unit ^long amount]
  (Money/ofMajor unit amount))

(defn ^Money of-minor
  "Instantiates a monetary amount of given currency unit and minor unit amount.
   This function can be used for currencies without minor units (e.g. JPY),
   in which case it's going to work just as of-major."
  [^CurrencyUnit unit ^long amount]
  (Money/ofMinor unit amount))

(defn ^{:tag 'long} major-of
  "Returns the amount in major units as a long"
  [^Money money]
  (.getAmountMajorLong money))

(defn minor-of
  "Returns the amount in minor units as a BigInt"
  [^Money money]
  (bigint (.getAmountMinor money)))

(defn ^CurrencyUnit currency-of
  "Returns the currency of a monetary amount"
  [^Money money]
  (.getCurrencyUnit money))

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

(defprotocol AmountOps
  (op-plus [other money rounding-mode])
  (op-minus [other money rounding-mode])
  (op-multiply [multiplier money rounding-mode])
  (op-divide [multiplier money rounding-mode]))

(extend-protocol AmountOps
  Double
  (op-plus [other ^Money money ^RoundingMode rounding-mode]
    (.plus money other rounding-mode))
  (op-minus [ other ^Money money ^RoundingMode rounding-mode]
    (.minus money other rounding-mode))
  (op-multiply [ multiplier ^Money money ^RoundingMode rounding-mode]
    (.multipliedBy money multiplier rounding-mode))
  (op-divide [multiplier ^Money money ^RoundingMode rounding-mode]
    (.dividedBy money multiplier rounding-mode))
  java.math.BigDecimal
  (op-plus [other ^Money money ^RoundingMode rounding-mode]
    (.plus money other rounding-mode))
  (op-minus [ other ^Money money ^RoundingMode rounding-mode]
    (.minus money other rounding-mode))
  (op-multiply [ multiplier ^Money money ^RoundingMode rounding-mode]
    (.multipliedBy money multiplier rounding-mode))
  (op-divide [multiplier ^Money money ^RoundingMode rounding-mode]
    (.dividedBy money multiplier rounding-mode))
  Long
  (op-multiply [ multiplier ^Money money ^RoundingMode rounding-mode]
    (.multipliedBy money multiplier))
  (op-divide [multiplier ^Money money ^RoundingMode rounding-mode]
    (.dividedBy money multiplier rounding-mode))
  Money
  (op-plus [^Money other ^Money money ^RoundingMode rounding-mode]
    (.plus money other))
  (op-minus [^Money other ^Money money ^RoundingMode rounding-mode]
    (.minus money other))
  Iterable
  (op-plus [other ^Money money ^RoundingMode rounding-mode]
    (.plus money other))
  (op-minus [other ^Money money ^RoundingMode rounding-mode]
    (.minus money other)))

(defn ^Money plus
  "Adds two monetary amounts together"
  [^Money money other]
  (op-plus other money (cnv/to-rounding-mode nil)))

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
  (op-minus other money (cnv/to-rounding-mode nil)))

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
   * :floor, :ceiling, :up, :down, :half-up, :half-down, :half-even that correspond to
     java.math.RoundingMode constants with the same names
   * nil for no rounding"
  ([^Money money ^double multiplier]
     (op-multiply multiplier money (cnv/to-rounding-mode nil)))
  ([^Money money ^double multiplier rounding-mode]
     (op-multiply multiplier money (cnv/to-rounding-mode rounding-mode))))

(defn ^Money divide
  "Divides monetary amount by the given number. Takes an optional arounding
   mode which is one of:

   * java.math.RoundingMode instances
   * :floor, :ceiling, :up, :down, :half-up, :half-down, :half-even that correspond to
     java.math.RoundingMode constants with the same names
   * nil for no rounding"
  ([^Money money multiplier]
     (op-divide multiplier money (cnv/to-rounding-mode nil)))
  ([^Money money multiplier rounding-mode]
     (op-divide multiplier money (cnv/to-rounding-mode rounding-mode))))

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

(defn >
  "Returns true if the given money amounts are in monotonically decreasing order,
  otherwise false."
  ([a] true)
  ([^Money a ^Money b]
     (.isGreaterThan a b))
  ([a b & more]
     (if (> a b)
       (if (next more)
         (recur a (first more) (next more))
         (> b (first more)))
       false)))

(defn >=
  "Returns true if the given money amounts are in monotonically non-increasing order,
  otherwise false."
  ([a] true)
  ([^Money a ^Money b]
     (or (.isGreaterThan a b) (= a b)))
  ([a b & more]
     (if (>= a b)
       (if (next more)
         (recur b (first more) (next more))
         (>= b (first more)))
       false)))

(defn <
  "Returns true if the given money amounts are in monotonically decreasing order,
  otherwise false."
  ([a] true)
  ([^Money a ^Money b]
     (.isLessThan a b))
  ([a b & more]
     (if (< a b)
       (if (next more)
         (recur a (first more) (next more))
         (< b (first more)))
       false)))

(defn <=
  "Returns true if the given money amounts are in monotonically non-decreasing order,
  otherwise false."
  ([a] true)
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
   * :floor, :ceiling, :up, :down, :half-up, :half-down, :half-even that correspond to
     java.math.RoundingMode constants with the same names
   * nil for no rounding"
  [^Money money ^long scale rounding-mode]
  (.rounded money scale (cnv/to-rounding-mode rounding-mode)))

(defn ^Money convert-to
  "Converts monetary amount in one currency to monetary amount in a different
   currency using the provided multiplier (exchange rate) and rounding mode.

   Multiplier should be either a java.math.BigDecimal or a double.

   Rounding mode should be one of:

   * java.math.RoundingMode instances
   * :floor, :ceiling, :up, :down, :half-up, :half-down, :half-even that correspond to
     java.math.RoundingMode constants with the same names
   * nil for no rounding"
  [^Money money ^CurrencyUnit currency multiplier rounding-mode]
  (.convertedTo money currency (bigdec multiplier) (cnv/to-rounding-mode rounding-mode)))
