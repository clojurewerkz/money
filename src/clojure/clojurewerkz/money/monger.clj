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
