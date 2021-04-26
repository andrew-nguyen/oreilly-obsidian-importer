(ns oreilly-obsidian-importer.core
  (:require [oreilly-obsidian-importer.oreilly :as o]
            [me.raynes.fs :as fs]
            [clojure.java.io :as io])
  (:gen-class))

(def version (-> "project.clj" slurp read-string (nth 2)))

(defn get-cwd
  []
  (.getAbsolutePath (io/as-file ".")))

(defn abs-path
  [p]
  (let [cwd (get-cwd)]
    (if (clojure.string/starts-with? p "/")
      p
      (str cwd "/" p))))

(defn run
  "I don't do a whole lot ... yet."
  [& args]
  (let [input-file (first args)
        root-dir (second args)]
    (println (format "Reading: %s into %s" input-file root-dir))
    (fs/with-cwd (get-cwd)
      (o/write-all (clojure.string/replace root-dir #"/$" "") (o/read-file input-file)))
    ))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]

  (println "OReilly Importer (ORI) version" version "\n")

  (when (< (count args) 2)
    (println "Usage: ori <input CSV file> <root dir>")
    (System/exit -1))
  (let [input-file (first args)
        root-dir (second args)]
    (run input-file root-dir)))
