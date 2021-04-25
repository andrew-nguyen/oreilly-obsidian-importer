(defproject oreilly-obsidian-importer "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "https://github.com/andrew-nguyen/oreilly-obsidian-importer"
  :license {:name "Apache License, Version 2.0"
            :url "https://www.apache.org/licenses/LICENSE-2.0.txt"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [techascent/tech.ml.dataset "6.00-beta-7"]]
  :main ^:skip-aot oreilly-obsidian-importer.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
