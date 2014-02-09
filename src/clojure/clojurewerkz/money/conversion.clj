;; Copyright (c) 2012-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns clojurewerkz.money.conversion
  (:import java.math.RoundingMode))

;;
;; API
;;

(defprotocol ToRoundingMode
  (^java.math.RoundingMode to-rounding-mode [input] "Converts input to java.math.RoundingMode"))

(extend-protocol ToRoundingMode
  RoundingMode
  (to-rounding-mode [input]
    input)

  clojure.lang.Named
  (to-rounding-mode [^clojure.lang.Named input]
    (to-rounding-mode (name input)))

  String
  (to-rounding-mode [^String input]
    (case (.toLowerCase input)
      "up"   RoundingMode/UP
      "down" RoundingMode/DOWN
      "ceiling"   RoundingMode/CEILING
      "floor"     RoundingMode/FLOOR
      "half-up"   RoundingMode/HALF_UP
      "half-down" RoundingMode/HALF_DOWN
      "half-even" RoundingMode/HALF_EVEN
      "unnecessary" RoundingMode/UNNECESSARY
      RoundingMode/UNNECESSARY))

  nil
  (to-rounding-mode [_]
    RoundingMode/UNNECESSARY))
