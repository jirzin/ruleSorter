(defproject rule-sorter "0.1.0-SNAPSHOT"
  :description "simple aplication taht reads user input, those inputs in form of individual lines should of text that willl be sorted according to different sorting rules"
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :main ^:skip-aot rule-sorter.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
