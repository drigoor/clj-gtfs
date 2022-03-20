(ns clj-gtfs.utils
  (:require [clojure.java.io :as io])
  (:import (java.io File)
           [java.util.zip ZipInputStream]))


;;; from: https://clojuredocs.org/clojure.java.io/copy
;(defn copy-uri-to-file [uri file]
;  (with-open [in (io/input-stream uri)
;              out (io/output-stream file)]
;    (io/copy in out)))
;
;(comment
;  (unzip-file "https://transitfeeds.com/p/metro-de-lisboa/1003/latest/download" "data/"))


;; from: https://gist.github.com/mikeananev/b2026b712ecb73012e680805c56af45f
(defn unzip-file
  "uncompress zip archive.
  `input` - name of zip archive to be uncompressed.
  `output` - name of folder where to output."
  [input output]
  (with-open [stream (-> input io/input-stream ZipInputStream.)]
    (loop [entry (.getNextEntry stream)]
      (if entry
        (let [save-path (str output File/separatorChar (.getName entry))
              out-file (File. save-path)]
          (if (.isDirectory entry)
            (if-not (.exists out-file)
              (.mkdirs out-file))
            (let [parent-dir (File. (.substring save-path 0 (.lastIndexOf save-path (int File/separatorChar))))]
              (if-not (.exists parent-dir) (.mkdirs parent-dir))
              (clojure.java.io/copy stream out-file)))
          (recur (.getNextEntry stream)))))))


(comment
  (unzip-file "https://transitfeeds.com/p/metro-de-lisboa/1003/latest/download" "data/"))


;; TODO -- protected agains 'Zip Slip'
;; see: https://mkyong.com/java/how-to-decompress-files-from-a-zip-file/


