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
