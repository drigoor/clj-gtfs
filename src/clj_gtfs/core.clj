(ns clj-gtfs.core
  (:require [clojure-csv.core :as csv]
            [clj-gtfs.utils :as utils]
            [io.pedestal.http :as http]
            [environ.core :refer [env]])
  (:use [clojure.string :only (split)]))


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


;; returns the number of seconds contained in str (is in "HH:mm:ss" format)
;; where HH is an hour in 24h
(defn parse-hour-minute-second [str]
  (let [[h m s] (map ->long (split str #":"))]
    (+ (* h 60 60) (* m 60) s)))


(defn parse-gtfs [filename function]
  (->> (slurp filename)
       (csv/parse-csv)
       (rest)
       (map function)))


(def stops (parse-gtfs "data/stops.txt"
                       (fn [[stop_id, stop_code, stop_name, stop_desc, stop_lat, stop_lon, zone_id, stop_url, location_type, parent_station]]
                         {:stop-id (keyword stop_id)
                          :name    stop_name
                          :lat     (Float/parseFloat stop_lat)
                          :lon     (Float/parseFloat stop_lon)})))


(defn get-stops [_]
  {:status 200
   :body   stops})


(defn get-stop [{{:keys [stop]}     :path-params
                 {:keys [extended]} :query-params}]
  (if-let [stop (->> stops
                     (filter #(= stop (:name %)))
                     first)]
    {:status 200
     :body   (if extended
               stop
               (dissoc stop :stop))}
    {:status 404}))


(defn hello-world [_]
  {:status 200
   :body   "These aren’t the Droids You are Looking For"})


(def routes #{["/" :get hello-world :route-name :hello-world]
              ["/stops" :get get-stops :route-name :get-stops]
              ["/stops/:stop" :get get-stop :route-name :get-stop]})


(def service-map
  {::http/routes routes
   ::http/type   :immutant
   ::http/host   "0.0.0.0"
   ::http/join?  false
   ::http/port   (or (->int (env :port)) 5000)})


(defn -main [& [http-port]]
  (-> service-map
      http/create-server
      http/start))


(comment



  (require '[vlaaad.reveal :as r])

  (r/tap-log)

  (tap> stops)

  (-main)





  (parse-hour-minute-second "00:00:54")
  (parse-hour-minute-second "00:01:54")
  (parse-hour-minute-second "00:02:54")
  (parse-hour-minute-second "06:51:54")
  (parse-hour-minute-second "24:00:00")



  (def stop-times (parse-gtfs "data/stop_times.txt"
                              (fn [[trip_id, arrival_time, departure_time, stop_id, stop_sequence, stop_headsign, pickup_type, drop_off_type, shape_dist_traveled]]
                                {:trip-id        (Integer/parseInt trip_id)
                                 :arrival-time   arrival_time ;(parse-hour-minute-second arrival_time)
                                 :departure-time departure_time ;(parse-hour-minute-second departure_time)
                                 :stop-id        (keyword stop_id)
                                 :stop-sequence  (Integer/parseInt stop_sequence)})))


  (sort-by :stop-id stops)

  (group-by :trip-id stop-times)

  )