(ns clojurewerkz.money.json
  (:require cheshire.generate)
  (:import [org.joda.money Money CurrencyUnit]))

(defn ^String format-money
  [^Money m]
  ;; some currencies don't have minor units, e.g. JPY
  (if (> (.getDecimalPlaces (.getCurrencyUnit m)) 0)
    (format "%s %d.%d"
            (.. m getCurrencyUnit getCode)
            (.getAmountMajorLong m)
            (.getMinorPart m))
    (format "%s %d"
            (.. m getCurrencyUnit getCode)
            (.getAmountMajorLong m))))


(cheshire.generate/add-encoder CurrencyUnit
                               (fn [^CurrencyUnit cu ^com.fasterxml.jackson.core.json.WriterBasedJsonGenerator generator]
                                 (.writeString generator (.getCode cu))))

(cheshire.generate/add-encoder Money
                               (fn [^Money m ^com.fasterxml.jackson.core.json.WriterBasedJsonGenerator generator]
                                 (.writeString generator (format-money m))))
