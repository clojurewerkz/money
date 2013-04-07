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
