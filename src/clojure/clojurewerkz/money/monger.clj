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
