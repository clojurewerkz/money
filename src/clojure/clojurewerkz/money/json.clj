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
