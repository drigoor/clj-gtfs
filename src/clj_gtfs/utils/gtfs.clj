;   initialises gtfs data in global variables


(ns clj-gtfs.utils.gtfs
  (:require [clojure-csv.core :as csv]
            [clj-gtfs.utils :refer :all]))


(defn parse-gtfs [filename function]
  (->> (slurp filename)
       (csv/parse-csv)
       (rest)
       (map function)))


(defonce stops (parse-gtfs "data/stops.txt"
                           (fn [[stop_id, stop_code, stop_name, stop_desc, stop_lat, stop_lon, zone_id, stop_url, location_type, parent_station]]
                             {:stop-id (keyword stop_id)
                              :name    stop_name
                              :lat     (->float stop_lat)
                              :lon     (->float stop_lon)})))


(defonce stop-times (parse-gtfs "data/stop_times.txt"
                                (fn [[trip_id, arrival_time, departure_time, stop_id, stop_sequence, stop_headsign, pickup_type, drop_off_type, shape_dist_traveled]]
                                  {:trip-id        (->int trip_id)
                                   :arrival-time   arrival_time ;(parse-hour-minute-second arrival_time)
                                   :departure-time departure_time ;(parse-hour-minute-second departure_time)
                                   :stop-id        (keyword stop_id)
                                   :stop-sequence  (->int stop_sequence)})))

