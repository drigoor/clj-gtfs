(defproject clj-gtfs "0.1.0"
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [clojure-csv "2.0.2"]
                 [io.pedestal/pedestal.service "0.5.10"]
                 [io.pedestal/pedestal.route "0.5.10"]
                 [io.pedestal/pedestal.immutant "0.5.10"]]
                 ;;[environ "1.2.0"]]
  :main clj-gtfs.core
  :repl-options {:init-ns clj-gtfs.core}
  :profiles {:reveal {:dependencies [[org.clojure/clojure "1.10.3"]
                                     [nrepl "0.9.0"]
                                     [nrepl-complete "0.1.0"]
                                     [vlaaad/reveal "1.3.272"]]
                      ;;:repl-options {:nrepl-middleware [vlaaad.reveal.nrepl/middleware]}
                      :jvm-opts     ["-Dfile.encoding=UTF-8"
                                     "-Dvlaaad.reveal.prefs={:theme,:light,:font-family,\"Consolas\",:font-size,12}"]}})
