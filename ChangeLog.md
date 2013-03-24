## Changes between 1.0.0-beta2 and 1.0.0-beta2

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
