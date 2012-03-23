(defproject clojurewerkz/money "1.0.0-SNAPSHOT"
  :description "A Clojure library that deals with monetary values and currencies. Built on top of Joda Money."
  :min-lein-version "2.0.0"
  :url "http://github.com/clojurewerkz/money"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.joda/joda-money "0.6"]]
  :profiles {:1.4 {:dependencies [[org.clojure/clojure "1.4.0-beta5"]]}}
  :aliases { "all" ["with-profile" "dev:dev,1.4"] }
  :source-paths      ["src/clojure"]
  :java-source-paths ["src/java"]
  :warn-on-reflection true)
