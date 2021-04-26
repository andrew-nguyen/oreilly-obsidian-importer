(ns oreilly-obsidian-importer.oreilly
  (:require [clojure.data.csv :as csv]
            [me.raynes.fs :as fs]
            [marge.core :as m]
            [yaml.core :as y]
            [clojure.java.io :as io])
  (:import (java.time.format DateTimeFormatter)))

(defn csv-data->maps [csv-data]
  (mapv zipmap
        (->> (first csv-data)                               ;; First row is the header
             ;(map keyword) ;; Drop if you want string keys instead
             repeat)
        (rest csv-data)))

(defn read-file
  "Takes a path/URL and returns a seq of maps"
  [f]
  (with-open [reader (io/reader f)]
    (->> (csv/read-csv reader)
         (csv-data->maps))))

(defn ->obsidian-filename
  "Takes a string and turns it into an obsidian-safe filename
  No \\ / :"
  [s]
  (-> s
      (clojure.string/replace #":" "%3A")
      (clojure.string/replace #"/" "%2F")
      (clojure.string/replace #"\\" "%5C")))

(defn ->front-matter
  [x]
  (y/generate-string {:book      {:title (get x "Book Title")
                                  :url   (get x "Book URL")}
                      :chapter   (get x "Chapter URL")
                      :highlight {:date (get x "Date of Highlight")
                                  :url  (get x "Highlight URL")}}
                     :dumper-options {:flow-style :block}))

(defn ->post
  "Generates the post markdown for a given entry"
  [book-name x]
  (format "---\n%s---\n\n%s"
          (->front-matter x)
          (str
            (m/markdown
              [:h2 "Book"])
            (format "[[%s]]\n\n" book-name)
            (m/markdown
              [:h2 "Chapter"])
            (get x "Chapter URL") "\n\n"
            (m/markdown
              [:h2 "Highlight"])
            (m/markdown
              [:blockquote (get x "Highlight")]) "\n\n"
            (m/markdown
              [:h2 "Note"])
            (let [note (get x "Personal Note")]
              (if (empty? note)
                "None"
                note)))))

(defn ->book
  [book]
  (str (m/markdown
         [:h1 (get book "Book Title")]) "\n"
       (get book "Book URL")))

(defn ->filename
  "Generates filename for given entry"
  [x]
  (let [highlight-url (get x "Highlight URL")]
    (-> highlight-url
        (clojure.string/replace "https://learning.oreilly.com/a/" "")
        (clojure.string/replace #"/$" "")
        (clojure.string/replace "/" "__")
        (str ".md"))))

(defn write-all
  "Takes a list of entries and creates a structure as follows
  root/
  |_Books/
    |_<Book Title>/
      |_<Book Title>.md
        |_Highlights/
          |_various highlight.md files"
  [root-dir entries]
  (let [by-book (group-by (fn [x] (get x "Book Title")) entries)
        books-path root-dir]
    (when-not (fs/exists? books-path)
      (println "Making dir: " books-path)
      (fs/mkdirs books-path))
    (doseq [book-name (keys by-book)]
      (let [book-name (clojure.string/trim book-name)
            book-path (str books-path "/" book-name)
            book-filename (->obsidian-filename (format "%s.md" book-name))]
        (fs/mkdir book-path)
        ; creates main md file for the book (to which highlights are backlinked)
        (spit (str book-path "/" book-filename) (->book (first (get by-book book-name))))
        (let [entries (get by-book book-name)]
          (doseq [entry entries]
            (spit (str book-path "/" (->obsidian-filename (->filename entry))) (->post book-name entry)))
          (println (format "Loaded book: '%s', with %d highlights/notes" book-name (count entries))))))
    by-book))