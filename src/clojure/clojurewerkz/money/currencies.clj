(ns clojurewerkz.money.currencies
  "Provides var aliases for org.joda.money.CurrencyUnit constants"
  (:import org.joda.money.CurrencyUnit))


;;
;; API
;;

(defn ^CurrencyUnit of
  "Returns currency unit for the specified ISO-4217 three letter currency code"
  [^String code]
  (CurrencyUnit/of code))

(defn ^CurrencyUnit for-code
  "Returns currency unit for the specified ISO-4217 three letter currency code"
  [^String code]
  (CurrencyUnit/of code))

(defn ^CurrencyUnit of-numeric-code
  "Returns currency unit for the specified numeric currency code"
  [^long code]
  (CurrencyUnit/ofNumericCode code))

(defn ^CurrencyUnit for-numeric-code
  "Returns currency unit for the specified numeric currency code"
  [^long code]
  (CurrencyUnit/ofNumericCode code))

(defn ^CurrencyUnit of-country
  "Returns currency unit for the specified country code. This
   function is case sensitive."
  [^String code]
  (CurrencyUnit/ofCountry code))

(defn ^CurrencyUnit for-country
  "Returns currency unit for the specified country code. This
   function is case sensitive."
  [^String code]
  (CurrencyUnit/ofCountry code))

(defn pseudo-currency?
  "Returns true if this currency is a pseudo currency"
  [^CurrencyUnit cu]
  (.isPseudoCurrency cu))

(defprotocol ToCurrencyUnit
  (to-currency-unit [input] "Converts input to a CurrencyUnit instance"))

(extend-protocol ToCurrencyUnit
  String
  (to-currency-unit [^String s]
    (of s))

  CurrencyUnit
  (to-currency-unit [^CurrencyUnit cu]
    cu))




(defmacro defalias
  [^String s]
  `(def ~(symbol s) (CurrencyUnit/of (String/valueOf ~s))))

;; Code generated via reflection.
;; This can be macro-defined using reflection, too.
(defalias "AED")
(defalias "AFN")
(defalias "ALL")
(defalias "AMD")
(defalias "ANG")
(defalias "AOA")
(defalias "ARS")
(defalias "AUD")
(defalias "AWG")
(defalias "AZN")
(defalias "BAM")
(defalias "BBD")
(defalias "BDT")
(defalias "BGN")
(defalias "BHD")
(defalias "BIF")
(defalias "BMD")
(defalias "BND")
(defalias "BOB")
(defalias "BRL")
(defalias "BSD")
(defalias "BTN")
(defalias "BWP")
(defalias "BYR")
(defalias "BZD")
(defalias "CAD")
(defalias "CDF")
(defalias "CHF")
(defalias "CLP")
(defalias "CNY")
(defalias "COP")
(defalias "CRC")
(defalias "CUP")
(defalias "CVE")
(defalias "CZK")
(defalias "DJF")
(defalias "DKK")
(defalias "DOP")
(defalias "DZD")
(defalias "EGP")
(defalias "ERN")
(defalias "ETB")
(defalias "EUR")
(defalias "FJD")
(defalias "FKP")
(defalias "GBP")
(defalias "GEL")
(defalias "GHS")
(defalias "GIP")
(defalias "GMD")
(defalias "GNF")
(defalias "GTQ")
(defalias "GYD")
(defalias "HKD")
(defalias "HNL")
(defalias "HRK")
(defalias "HTG")
(defalias "HUF")
(defalias "IDR")
(defalias "ILS")
(defalias "INR")
(defalias "IQD")
(defalias "IRR")
(defalias "ISK")
(defalias "JMD")
(defalias "JOD")
(defalias "JPY")
(defalias "KES")
(defalias "KGS")
(defalias "KHR")
(defalias "KMF")
(defalias "KPW")
(defalias "KRW")
(defalias "KWD")
(defalias "KYD")
(defalias "KZT")
(defalias "LAK")
(defalias "LBP")
(defalias "LKR")
(defalias "LRD")
(defalias "LSL")
(defalias "LTL")
(defalias "LVL")
(defalias "LYD")
(defalias "MAD")
(defalias "MDL")
(defalias "MGA")
(defalias "MKD")
(defalias "MMK")
(defalias "MNT")
(defalias "MOP")
(defalias "MRO")
(defalias "MUR")
(defalias "MVR")
(defalias "MWK")
(defalias "MXN")
(defalias "MYR")
(defalias "MZN")
(defalias "NAD")
(defalias "NGN")
(defalias "NIO")
(defalias "NOK")
(defalias "NPR")
(defalias "NZD")
(defalias "OMR")
(defalias "PAB")
(defalias "PEN")
(defalias "PGK")
(defalias "PHP")
(defalias "PKR")
(defalias "PLN")
(defalias "PYG")
(defalias "QAR")
(defalias "RON")
(defalias "RSD")
(defalias "RUB")
(defalias "RUR")
(defalias "RWF")
(defalias "SAR")
(defalias "SBD")
(defalias "SCR")
(defalias "SDG")
(defalias "SEK")
(defalias "SGD")
(defalias "SHP")
(defalias "SLL")
(defalias "SOS")
(defalias "SRD")
(defalias "STD")
(defalias "SYP")
(defalias "SZL")
(defalias "THB")
(defalias "TJS")
(defalias "TMT")
(defalias "TND")
(defalias "TOP")
(defalias "TRY")
(defalias "TTD")
(defalias "TWD")
(defalias "TZS")
(defalias "UAH")
(defalias "UGX")
(defalias "USD")
(defalias "UYU")
(defalias "UZS")
(defalias "VEF")
(defalias "VND")
(defalias "VUV")
(defalias "WST")
(defalias "XAF")
(defalias "XAG")
(defalias "XAU")
(defalias "XBA")
(defalias "XBB")
(defalias "XBC")
(defalias "XBD")
(defalias "XCD")
(defalias "XDR")
(defalias "XFU")
(defalias "XOF")
(defalias "XPD")
(defalias "XPF")
(defalias "XPT")
(defalias "XTS")
(defalias "XXX")
(defalias "YER")
(defalias "ZAR")
(defalias "ZMK")
(defalias "ZWL")
