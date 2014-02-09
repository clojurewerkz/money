;; Copyright (c) 2012-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns clojurewerkz.money.hiccup
  "Provides Hiccup integration: HTML rendering of monetary amounts"
  (:require [clojurewerkz.money.format :as fmt]
            [hiccup.compiler           :as hup])
  (:import [org.joda.money Money CurrencyUnit]))


(extend-protocol hup/HtmlRenderer
  Money
  (render-html [^Money money]
    (fmt/format money))

  CurrencyUnit
  (render-html [^CurrencyUnit cu]
    (.getCode cu)))
