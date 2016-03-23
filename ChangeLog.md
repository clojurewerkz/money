## Changes Between 1.9.0 and 1.10.0

### Fix JSON serialization format

`clojurewerkz.money.json` previously used a Cheshire encoder that serialized a
`Money` object to a locale-dependent amount which could not be parsed by
`clojurewerkz.money.amounts/parse`, and did not agree with the `c.m.j` namespace
documentation. It has been updated to produce a `[CODE] [AMOUNT]` string which
can be parsed by `clojurewerkz.money.amounts/parse`.

References:

* [Reported in #9](https://github.com/clojurewerkz/money/issues/9)
* [Fixed in #18](https://github.com/clojurewerkz/money/pull/18)

Contributed by [Gordon Stratton](https://github.com/gws)

### Fix Primitive Return Type Hints

`clojurewerkz.money.amounts/major-of` and `clojurewerkz.money.amounts/minor-of`
had incorrect primitive return type hints, which have been fixed.

See [#14](https://github.com/clojurewerkz/money/pull/14)

Contributed by [Gordon Stratton](https://github.com/gws)

## Changes Between 1.8.0 and 1.9.0

### New Amount Functions

`clojurewerkz.money.amounts/major-of`, `clojurewerkz.money.amounts/minor-of`,
and `clojurewerkz.money.amounts/currency-of` return major units (e.g. dollars,
minor units (e.g. cents), and currency unit from a provided monetary amount
(`Money` instance).

### Clojure 1.7 By Default

The project now depends on `org.clojure/clojure` version `1.7.0`. It is
still compatible with Clojure 1.6 and if your `project.clj` depends on
a different version, it will be used, but 1.7 is the default now.

We encourage all users to upgrade to 1.7, it is a drop-in replacement
for the majority of projects out there.


## Changes between 1.7.0 and 1.8.0

### Conversion Supports BigDecimals

`clojurewerkz.money.amounts/convert-to` now accepts `BigDecimal`s.

Contributed by TJ Gabbour.


## Changes between 1.6.0 and 1.7.0

### ZMK Currency Removed

In accordance with a Joda Money `0.10.0` change, ZMK is no longer
on the list of known currencies (use ZMW instead).

### Joda Money 0.10

Joda Money was upgraded to 0.10.0.



## Changes between 1.5.0 and 1.6.0

### Clojure 1.6 By Default

The project now depends on `org.clojure/clojure` version `1.6.0`. It is
still compatible with Clojure 1.4 and if your `project.clj` depends on
a different version, it will be used, but 1.6 is the default now.

We encourage all users to upgrade to 1.6, it is a drop-in replacement
for the majority of projects out there.


## Changes between 1.4.0 and 1.5.0

### Comparison Function Correctness

`<`, `>`, `<=`, `>=` now return a boolean on the 1-arg arity, as per docstring.

Contributed by Nicola Mometto.


## Changes between 1.3.0 and 1.4.0

### Roudning Multiplication

`clojuremowerkz.money.amount/multiply` now provides another
arity that allows for multiplication by a double, just like
with `divide`:

``` clojure
(ams/multiply (ams/amount-of cu/USD 45) 10.1 :floor)
;= USD 454.50
```


## Changes between 1.2.0 and 1.3.0

### Hiccup Integration

`clojurewerkz.money.hiccup`, when compiled, will extend Hiccup's HTML
rendering protocol for monetary amounts and currency units. Internally
it uses default formatter used by `clojurewerkz.money.format`:
`clojurewerkz.money.format/*default-formatter*`, which can be rebound.

### Formatting of Monetary Amounts

`clojurewerkz.money.format/format` is a new function that formats
monetary amounts using default or provided locale:

``` clojure
(require '[clojurewerkz.money.currencies :as cu)
(require '[clojurewerkz.money.amounts :refer [amount-of])
(require '[clojurewerkz.money.format :refer :all])

(import java.util.Locale)

;; format using default system locale
(format (amount-of cu/GBP 20.0) Locale/UK) ;= GBP20,00
;; format using UK locale
(format (amount-of cu/GBP 20.0) Locale/UK) ;= £10.00
```

Custom Joda Money formatters are also supported.


### Addition and Subtraction of Monetary Amounts

`clojurewerkz.money.amounts/plus` and `clojurewerkz.money.amounts/minus` now accept
monetary amounts as well as doubles as 2nd argument.

Contributed by Ryan Neufeld.


## Changes between 1.1.0 and 1.2.0

ClojureWerkz Money now provides a list of additional currencies
on top of the default Joda Money currency provider. Right now
the only additional currency is BTC (Bitcoin).



## Changes between 1.0.0 and 1.1.0

It is possible to compare monetary amounts using >, >=, < and <=.

```clojure
(require '[clojurewerkz.money.amounts    :as ma])
(require '[clojurewerkz.money.currencies :as mc])

(ma/< (ma/amount-of mc/USD 100) (ma/amount-of mc/USD 100))
;= false

(ma/<= (ma/amount-of mc/USD 100) (ma/amount-of mc/USD 100) (ma/amount-of mc/USD 120))
;= true

(ma/>= (ma/amount-of mc/USD 100) (ma/amount-of mc/USD 100) (ma/amount-of mc/USD 120))
;= false

(ma/> (ma/amount-of mc/USD 200) (ma/amount-of mc/USD 100))
;= true
```


## Changes between 1.0.0-beta2 and 1.0.0

### clojurewerkz.money.amounts/convert-to

`clojurewerkz.money.amounts/convert-to` converts a monetary value in one currency
to another using provided exchange rate and rounding mode:

``` clojure
(require '[clojurewerkz.money.amounts :as ams])

(ams/convert-to (ams/amount-of cu/GBP 65.65) cu/USD 1.523 :down)
;= USD 99.98
```


### clojurewerkz.money.amounts/round

`clojurewerkz.money.amounts/round` is a new function that performs rounding of
monetary values using one of the rounding modes:

``` clojure
(require '[clojurewerkz.money.amounts :as ams])

(ams/round (ams/amount-of cu/USD 40.01) -1 :floor)
;= USD 40

(ams/round (ams/amount-of cu/USD 40.01) -1 :up)
;= USD 50

(ams/round (ams/amount-of cu/USD 45.24) 0 :floor)
;= USD 45

(ams/round (ams/amount-of cu/USD 45.24) 0 :up)
;= USD 46

(ams/round (ams/amount-of cu/USD 45.24) 1 :floor)
;= USD 45.20

(ams/round (ams/amount-of cu/USD 45.24) 1 :up)
;= USD 45.30
```


## Changes between 1.0.0-beta1 and 1.0.0-beta2

### Monger Integration

`clojurewerkz.money.monger`, when loaded, registers BSON serializers for `org.joda.money.Money`
and `org.joda.money.CurrencyUnit`. Serialization conventions used are straightforward and
produce human readable values:

 * `(clojurewerkz.money.currencies/USD)` => `"USD"`
 * `(clojurewerkz.money.amounts/amount-of (clojurewerkz.money.currencies/USD) 20.5)` => `{"currency-unit" "USD" "amount-in-minor-units" 2050}`

Note that serialization is one-way: loaded documents are returned as maps because there is no way to tell
them from regular BSON documents. `clojurewerkz.money.monger/from-stored-map` can be used to produce `Money` instances
from maps following the serialization convention described above.


### Cheshire Integration

`clojurewerkz.money.json`, when loaded, registers serializers for `org.joda.money.Money`
and `org.joda.money.CurrencyUnit`. Serialization conventions used are straightforward and
produce human readable values:

 * `(clojurewerkz.money.currencies/USD)` => `"USD"`
 * `(clojurewerkz.money.amounts/amount-of (clojurewerkz.money.currencies/USD) 20.5)` => `"USD 20.50"`


This requires Cheshire `5.0.x` or later. `clojure.data.json` is not supported.


### clojurewerkz.money.amounts/max and min

`clojurewerkz.money.amounts/max` and `clojurewerkz.money.amounts/min` are
new functions that return the greater and the lesser of two monetary amounts.

### clojurewerkz.money.currencies

`clojurewerkz.money.currencies` is a new namespace that defines vars with currency
units. For example, instead of `(org.joda.money.CurrencyUnit/valueOf "NOK")` it is now
possible to use `clojurewerkz.money.currencies/NOK`.


### clojurewerkz.money.amounts/positive? and Related

`clojurewerkz.money.amounts/positive?`, `clojurewerkz.money.amounts/negative?`,
`clojurewerkz.money.amounts/positive-or-zero?`, and `clojurewerkz.money.amounts/negative-or-zero?` are
new predicate functions that work with monetary amounts.


## Changes between 1.0.0-SNAPSHOT and 1.0.0-beta1

### Joda Money 0.8

Joda Money was upgraded to 0.8.


### Clojure 1.5 By Default

Money now depends on Clojure 1.5. It still supports 1.3+
but the default version is now 1.5.
