(defproject clojurewerkz/money "1.11.0-SNAPSHOT"
  :description "A Clojure library that deals with monetary values and currencies. Built on top of Joda Money."
  :min-lein-version "2.5.1"
  :url "http://github.com/clojurewerkz/money"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.joda/joda-money "0.10.0"]]
  :profiles {:dev {:plugins      [[codox "0.8.10"]]
                   :dependencies [[com.novemberain/monger "2.0.0"]
                                  [cheshire               "5.3.1"]
                                  [hiccup                 "1.0.3"]]}
             :1.6 {:dependencies [[org.clojure/clojure "1.6.0"]]}
             :1.7 {:dependencies [[org.clojure/clojure "1.7.0"]]}
             :master {:dependencies [[org.clojure/clojure "1.9.0-master-SNAPSHOT"]]}}
  :repositories {"sonatype" {:url "http://oss.sonatype.org/content/repositories/releases"
                             :snapshots false
                             :releases {:checksum :fail :update :always}}
                 "sonatype-snapshots" {:url "https://oss.sonatype.org/content/repositories/snapshots"
                                       :snapshots true
                                       :releases {:checksum :fail :update :always}}}
  :aliases  {"all" ["with-profile" "+dev:+1.6:+1.7:+master"]}
  :source-paths      ["src/clojure"]
  :java-source-paths ["src/java"]
  :resource-paths    ["src/resources"]
  :global-vars {*warn-on-reflection* true})
