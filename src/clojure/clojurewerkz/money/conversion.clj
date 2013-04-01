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
