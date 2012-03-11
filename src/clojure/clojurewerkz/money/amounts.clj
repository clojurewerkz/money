(ns clojurewerkz.money.amounts
  (:import [org.joda.money Money BigMoney CurrencyUnit]
           [java.math RoundingMode]))

;;
;; API
;;

(defn amount-of
  ([^CurrencyUnit unit ^double amount]
          (Money/of unit amount))
  ([^CurrencyUnit unit ^double amount ^RoundingMode rm]
          (Money/of unit amount rm)))
