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

(ns clojurewerkz.money.format
  "Formatting of monetary amounts"
  (:refer-clojure :exclude [format])
  (:import [org.joda.money Money]
           [org.joda.money.format MoneyFormatterBuilder MoneyFormatter MoneyAmountStyle]
           java.util.Locale))


;;
;; Implementation
;;

(defn new-builder
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
