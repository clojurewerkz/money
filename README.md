# ClojureWerkz Money, a Clojure Library to Work With Money

ClojureWerkz Money is a Clojure library that deals with monetary amounts.
It is built on top of [Joda Money](http://joda-money.sourceforge.net/).


## Project Goals

 * Expose most or all Joda Money features in an easy to use way
 * Be well documented and well tested
 * Integrate with popular libraries such as [Cheshire](https://github.com/dakrone/cheshire) and [Monger](http://clojuremongodb.info)
 * Don't introduce any significant amount of performance overhead


## Project Maturity

Money is past `1.0` and is considered to be a complete, stable library.



## Maven Artifacts

Money artifacts are [released to Clojars](https://clojars.org/clojurewerkz/money). If you are using Maven, add the following repository
definition to your `pom.xml`:

``` xml
<repository>
  <id>clojars.org</id>
  <url>http://clojars.org/repo</url>
</repository>
```

### Most Recent Release

With Leiningen:

    [clojurewerkz/money "1.10.0"]


With Maven:

    <dependency>
      <groupId>clojurewerkz</groupId>
      <artifactId>money</artifactId>
      <version>1.10.0</version>
    </dependency>


## Documentation

### Monetary Amounts

Monetary amounts are instantiated using `clojurewerkz.money.amounts` functions. They operate on
floating point amounts (doubles) or long values in major units (e.g. dollars) or minor units (e.g. cents).

Note that some currencies do not have minor units (most notably `JPY`). For those, use `clojurewerkz.money.amounts/of-major`.

``` clojure
(require '[clojurewerkz.money.amounts :as ma])
(require '[clojurewerkz.money.currencies :as mc])

;; USD 10.50
(ma/amount-of mc/USD 10.5)
;; USD 10
(ma/of-major mc/USD 10)
;; USD 10.50
(ma/of-minor mc/USD 1050)

;; JPY 1000
(ma/of-major mc/JPY 1000)
```

Note that not all currencies have minor units (most notably JPY does not).

It is possible to parse a string in the standard format `[currency unit] [amount]`, e.g. `JPY 1000`:

``` clojure
(require '[clojurewerkz.money.amounts :as ma])

(ma/parse "JPY 1000")
;= org.joda.money.Money instance for JPY 1000
```

Monetary amounts can be added, substracted and so on using `clojurewerkz.money.amounts/plus`,
`clojurewerkz.money.amounts/minus`, `clojurewerkz.money.amounts/multiply`, and
`clojurewerkz.money.amounts/divide` functions:

``` clojure
(require '[clojurewerkz.money.amounts    :as ma])
(require '[clojurewerkz.money.currencies :as mc])

(ma/plus (ma/amount-of mc/USD 10) (ma/amount-of mc/USD 100))
;= USD 110

(ma/minus (ma/amount-of mc/USD 100) (ma/amount-of mc/USD 10))
;= USD 90

(ma/multiply (ma/amount-of mc/USD 100) 10)
;= USD 1000

;; :floor for flooring round mode
(ma/divide (ma/amount-of mc/USD 100.1) 10 :floor)
;= USD 10
```

It is possible to add up all monies in a collection or sequence using `clojurewerkz.money.amounts/total`:

``` clojure
(require '[clojurewerkz.money.amounts    :as ma])
(require '[clojurewerkz.money.currencies :as mc])

(ma/total [(ma/amount-of mc/USD 10) (ma/amount-of mc/USD 100)])
;= USD 110
```

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

### Rounding

`clojurewerkz.money.amounts/round` is a function that performs rounding of
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


### Currencies

Currency units use their ISO-4217 codes and represented by `org.joda.money.CurrencyUnit` instances.
Usually the easiest way to use currency units is via `clojurewerkz.money.currencies` aliases:

``` clojure
(require '[clojurewerkz.money.currencies :as mc])

mc/USD ;= USD currency unit
mc/CAD ;= CAD currency unit
mc/GBP ;= GBP currency unit
mc/RUB ;= RUB currency unit
```

`clojurewerkz.money.currencies/for-code` and `clojurewerkz.money.currencies/of-country` can be used
to get currency units by their ISO-4217 code strings and country abbreviations:

``` clojure
(require '[clojurewerkz.money.currencies :as mc])

(mc/for-code "CHF")   ;= CHF currency unit
(mc/for-country "CH") ;= CHF currency unit
```

`clojurewerkz.money.currencies/pseudo-currency?` is a predicate function that takes a currency unit
and returns true if it is a pseudo-currency (e.g. [Bitcoin](http://bitcoin.org) or [IMF Special Drawing Rights](http://www.imf.org/external/np/exr/facts/sdr.htm)).


### Currency Conversion

`clojurewerkz.money.amounts/convert-to` converts a monetary value in one currency
to another using provided exchange rate and rounding mode:

``` clojure
(require '[clojurewerkz.money.amounts :as ams])

(ams/convert-to (ams/amount-of cu/GBP 65.65) cu/USD 1.523 :down)
;= USD 99.98
```


### Formatting

Money supports formatting of monetary amounts with the `clojurewerkz.money.format/format` function
which takes an amount and (optionally) a locale and a formatter:

``` clojure
(require '[clojurewerkz.money.currencies :as cu])
(require '[clojurewerkz.money.amounts :refer [amount-of]])
(require '[clojurewerkz.money.format :refer :all])

(import java.util.Locale)

;; format using default system locale
(format (amount-of cu/GBP 20.0)) ;= GBP20,00
;; format using UK locale
(format (amount-of cu/GBP 20.0) Locale/UK) ;= £20.00

;; format using Brazilian locale
(format (amount-of cu/BRL 20.0) (Locale. "pt" "BR")) ;= R$20,00
```

Default formatter uses localized currency symbol and amount and default locale which JVM infers from environment
settings.

### Cheshire Integration

`clojurewerkz.money.json`, when loaded, registers serializers for
`org.joda.money.Money` and `org.joda.money.CurrencyUnit` with
Cheshire. Serialization conventions used are straightforward and
produce human readable values:

 * `(clojurewerkz.money.currencies/USD)` => `"USD"`
 * `(clojurewerkz.money.amounts/amount-of (clojurewerkz.money.currencies/USD) 20.5)` => `"USD20.50"` (will use system locale by default)

To use it, simply require the namespace and then use Cheshire
generation functions as usual.

This extension requires Cheshire `5.0.x` or later. `clojure.data.json`
is not supported.


### Monger Integration

`clojurewerkz.money.monger`, when loaded, registers BSON serializers
for `org.joda.money.Money` and
`org.joda.money.CurrencyUnit`. Serialization conventions used are
straightforward and produce human readable values:

 * `(clojurewerkz.money.currencies/USD)` => `"USD"`
 * `(clojurewerkz.money.amounts/amount-of (clojurewerkz.money.currencies/USD) 20.5)` => `{"currency-unit" "USD" "amount-in-minor-units" 2050}`

Note that serialization is one-way: loaded documents are returned as
maps because there is no way to tell them from regular BSON
documents. `clojurewerkz.money.monger/from-stored-map` can be used to
produce `Money` instances from maps following the serialization
convention described above.


### Hiccup Integration

`clojurewerkz.money.hiccup`, when loaded, extends [Hiccup](https://github.com/weavejester/hiccup) HTML rendering protocol to render
monetary amounts and currency units.
Rendering conventions used are straightforward and
produce human readable values:

 * `(clojurewerkz.money.currencies/USD)` => `"USD"`
 * `(clojurewerkz.money.amounts/amount-of (clojurewerkz.money.currencies/USD) 20.5)` => `"USD20.50"` (will use system locale by default)

To use it, simply require the namespace and then use Hiccup
as usual.


## Community

[ClojureWerkz Money has a mailing list](https://groups.google.com/group/clojure-money). Feel free to join it and ask any questions you may have.

To subscribe for announcements of releases, important changes and so on, please follow [@ClojureWerkz](https://twitter.com/#!/clojurewerkz) on Twitter.



## Supported Clojure Versions

ClojureWerkz Money is built from the ground up for Clojure 1.4 and up.
The most recent release is always recommended.


## Continuous Integration

[![Continuous Integration status](https://secure.travis-ci.org/clojurewerkz/money.png)](http://travis-ci.org/clojurewerkz/money)

CI is hosted by [travis-ci.org](http://travis-ci.org)


## Development

Money uses [Leiningen 2](https://github.com/technomancy/leiningen/blob/master/doc/TUTORIAL.md). Make sure you have it installed and then run tests
against all supported Clojure versions using

    lein all test

Then create a branch and make your changes on it. Once you are done with your changes and all tests pass, submit
a pull request on GitHub.


## License

Copyright © 2012-2016 Michael S. Klishin, Alex Petrov, and the ClojureWerkz team.

Double licensed under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html) (the same as Clojure) or
the [Apache Public License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
