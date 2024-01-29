(defproject tic_tac_toe "0.1.0"
  
  ;;; Project Metadata
  :description "Classic Tic-Tac-Toe Game"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  
  ;;; Dependencies, Plugins
  :dependencies [[org.clojure/clojure "1.10.0"]]
  
  ;;; Profiles
  :profiles {:uberjar {:aot :all}}
  
  ;;; Running Project Code
  :main ^:skip-aot tic-tac-toe.core)
