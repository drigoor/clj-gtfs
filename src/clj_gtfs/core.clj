(ns clj-gtfs.core
  (:require [io.pedestal.http :as http]
            [clj-gtfs.utils.gtfs :refer [stops]])
  (:use [clojure.string :only (split)]))


(defn hello-world [_]
  {:status 200
   :body   "These aren’t the Droids You are Looking For"})


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


(def routes #{["/" :get hello-world :route-name :hello-world]
              ["/stops" :get get-stops :route-name :get-stops]
              ["/stops/:stop" :get get-stop :route-name :get-stop]})


(def service-map
  {::http/routes routes
   ::http/type   :immutant
   ::http/host   "0.0.0.0"
   ::http/join?  false
   ::http/port   8080})


(defn -main [& [http-port]]
  (-> service-map
      http/create-server
      http/start))


(comment

  (require '[vlaaad.reveal :as r])

  (r/tap-log)

  (tap> (sort-by :stop-id stops))


  (tap> (group-by :trip-id stop-times))

  (-main))


  ;;; returns the number of seconds contained in str (is in "HH:mm:ss" format)
  ;;; where HH is an hour in 24h
  ;(defn parse-hour-minute-second [str]
  ;  (let [[h m s] (map ->long (split str #":"))]
  ;    (+ (* h 60 60) (* m 60) s)))
  ;
  ;(parse-hour-minute-second "00:00:54")
  ;(parse-hour-minute-second "00:01:54")
  ;(parse-hour-minute-second "00:02:54")
  ;(parse-hour-minute-second "06:51:54")
  ;(parse-hour-minute-second "24:00:00")



