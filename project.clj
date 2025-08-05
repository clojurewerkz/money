(defproject clojurewerkz/money "1.12.0-SNAPSHOT"
  :description "A Clojure library that deals with monetary values and currencies. Built on top of Joda Money."
  :min-lein-version "2.5.1"
  :url "http://github.com/clojurewerkz/money"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.joda/joda-money "0.12"]]
  :profiles {:dev {:plugins      [[codox "0.10.8"]]
                   :dependencies [[com.novemberain/monger "3.6.0"]
                                  [cheshire               "6.0.0"]
                                  [hiccup                 "1.0.5"]]}
             :1.10 {:dependencies [[org.clojure/clojure "1.10.2"]]}
             :1.9 {:dependencies [[org.clojure/clojure "1.9.0"]]}
             :master {:dependencies [[org.clojure/clojure "1.11.0-master-SNAPSHOT"]]}}
  :repositories {"sonatype" {:url "http://oss.sonatype.org/content/repositories/releases"
                             :snapshots false
                             :releases {:checksum :fail :update :always}}
                 "sonatype-snapshots" {:url "https://oss.sonatype.org/content/repositories/snapshots"
                                       :snapshots true
                                       :releases {:checksum :fail :update :always}}}
  :aliases  {"all" ["with-profile" "+dev:+1.9:+1.10:+master"]}
  :source-paths      ["src/clojure"]
  :java-source-paths ["src/java"]
  :resource-paths    ["src/resources"]
  :global-vars {*warn-on-reflection* true})
