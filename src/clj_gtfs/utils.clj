(ns clj-gtfs.utils)


;; from: https://github.com/seancorfield/dot-clojure
(defn- ->long
  "Attempt to parse a string as a long and return nil if it fails."
  [s]
  (try
    (and s (Long/parseLong s))
    (catch Throwable _)))


(defn ->int
  "Attempt to parse a string as an int and return nil if it fails."
  [s]
  (try
    (and s (Integer/parseInt s))
    (catch Throwable _)))


(defn ->float
  "Attempt to parse a string as an int and return nil if it fails."
  [s]
  (try
    (and s (Float/parseFloat s))
    (catch Throwable _)))

