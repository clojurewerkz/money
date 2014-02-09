;; Copyright (c) 2012-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns clojurewerkz.money.json
  "Provides Cheshire integration.

   org.joda.money.Money instances are serialized to strings with human-readable
   amounts following ISO-4217 currency codes.

   Currency units are serialized to strings by taking their ISO-4217 codes."
  (:require cheshire.generate
            [clojurewerkz.money.format :as fmt])
  (:import [org.joda.money Money CurrencyUnit]))



(cheshire.generate/add-encoder CurrencyUnit
                               (fn [^CurrencyUnit cu ^com.fasterxml.jackson.core.json.WriterBasedJsonGenerator generator]
                                 (.writeString generator (.getCode cu))))

(cheshire.generate/add-encoder Money
                               (fn [^Money m ^com.fasterxml.jackson.core.json.WriterBasedJsonGenerator generator]
                                 (.writeString generator (fmt/format m))))
