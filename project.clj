(defproject tic_tac_toe "0.1.0-SNAPSHOT"
  :description "Classic Tic-Tac-Toe Game"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :main ^:skip-aot tic-tac-toe.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
