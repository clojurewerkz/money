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
