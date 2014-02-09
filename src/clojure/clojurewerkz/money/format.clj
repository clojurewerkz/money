;; Copyright (c) 2012-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns clojurewerkz.money.format
  "Formatting of monetary amounts"
  (:refer-clojure :exclude [format])
  (:import [org.joda.money Money]
           [org.joda.money.format MoneyFormatterBuilder MoneyFormatter MoneyAmountStyle]
           java.util.Locale))


;;
;; Implementation
;;

(defn ^:private new-builder
  []
  (MoneyFormatterBuilder.))

(defn ^MoneyFormatterBuilder append-amount
  ([^MoneyFormatterBuilder b]
     (.appendAmount b))
  ([^MoneyFormatterBuilder b ^MoneyAmountStyle style]
     (.appendAmount b style)))

(defn ^MoneyFormatterBuilder append-amount-localized
  [^MoneyFormatterBuilder b]
  (.appendAmountLocalized b))

(defn ^MoneyFormatterBuilder append-currency-symbol-localized
  [^MoneyFormatterBuilder b]
  (.appendCurrencySymbolLocalized b))

(defn ^MoneyFormatterBuilder append-currency-code
  [^MoneyFormatterBuilder b]
  (.appendCurrencyCode b))

(defn ^MoneyFormatter to-formatter
  ([^MoneyFormatterBuilder b]
     (.toFormatter b))
  ([^MoneyFormatterBuilder b ^Locale locale]
     (.toFormatter b locale)))

;;
;; API
;;

(defmacro ^MoneyFormatter build-formatter
  "Builds a money formatter"
  [& body]
  `(let [bldr# (-> (new-builder) ~@body)]
     (.toFormatter bldr#)))

(def ^MoneyFormatter ^:dynamic *default-builder*
  (-> (new-builder)
      append-currency-symbol-localized
      append-amount-localized))

(def ^MoneyFormatter ^:dynamic *default-formatter*
  (build-formatter append-currency-symbol-localized
                   append-amount-localized))

(defn ^String format
  "Formats monetary amount.

   When invoked with 1 argument, uses default formatter and system locale.
   When invoked with 2 arguments, uses default formatter and provided locale.
   When invoked with 3 arguments, uses provided formatter and locale."
  ([^Money money]
     (.print *default-formatter* money))
  ([^Money money ^Locale locale]
     (let [formatter (to-formatter *default-builder* locale)]
       (.print formatter money)))
  ([^Money money ^MoneyFormatter formatter ^Locale locale]
     (let [formatter (.withLocale formatter locale)]
       (.print formatter money))))
