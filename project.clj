(defproject oreilly-obsidian-importer "0.1.1"
  :description ""
  :url "https://github.com/andrew-nguyen/oreilly-obsidian-importer"
  :license {:name "Apache License, Version 2.0"
            :url  "https://www.apache.org/licenses/LICENSE-2.0.txt"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 ; YAML front matter
                 [io.forward/yaml "1.0.10"]
                 ; markdown generator
                 [org.clojars.andrew-nguyen/marge "0.16.0"]
                 ; filesystem utilities
                 [me.raynes/fs "1.4.6"]
                 ; data processing
                 [org.clojure/data.csv "1.0.0"]]
  :plugins [[io.taylorwood/lein-native-image "0.3.1"]]
  :native-image {:name "ori"                                ;; name of output image, optional
                 ;:graal-bin "/path/to/graalvm/"             ;; path to GraalVM home, optional
                 :opts      ["--no-fallback"
                             "--report-unsupported-elements-at-runtime"
                             "--initialize-at-build-time"]
                 }                                          ;; pass-thru args to GraalVM native-image, optional
  :main ^:skip-aot oreilly-obsidian-importer.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot          :all
                       :jvm-opts     ["-Dclojure.compiler.direct-linking=true"]
                       :native-image {:jvm-opts  ["-Dclojure.compiler.direct-linking=true"]}}})
