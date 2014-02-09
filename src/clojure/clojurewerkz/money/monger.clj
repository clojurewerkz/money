;; Copyright (c) 2012-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns clojurewerkz.money.monger
  "Provides Monger integration by extending Monger's BSON serialization protocol
   for org.joda.money.CurrencyUnit and Money.

   Note that while dumping (serialization) of monetary amounts is automatic, they are
   stored in regular BSON documents. Monger and Money do not automatically instantiate
   monetary amounts when documents are loaded.

   That can be done manually using the from-stored-map function."
  (:require [monger.conversion :as cnv]
            [clojurewerkz.money.amounts    :as mc]
            [clojurewerkz.money.currencies :as cc])
  (:import [org.joda.money Money CurrencyUnit]
           com.mongodb.BasicDBObject))


(extend-protocol cnv/ConvertToDBObject
  CurrencyUnit
  (to-db-object [^CurrencyUnit cu]
    (.getCode cu))

  Money
  (to-db-object [^Money m]
    (doto (BasicDBObject.)
      (.put "amount-in-minor-units" (.getAmountMinorLong m))
      (.put "currency-unit"         (.. m getCurrencyUnit getCode)))))

(defn from-stored-map
  "Instantiates a Money data structure from a map loaded from
   MongoDB. Assumes the serialization convention used by the monger.conversion/ConvertToDBObject
   extension in this namespace."
  [m]
  (mc/of-minor (cc/for-code (get m "currency-unit"))
               (get m "amount-in-minor-units")))
