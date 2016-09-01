(defproject iss "0.1.0-SNAPSHOT"
  :description "Write inline styles; ship optimized CSS stylesheets."
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.226"]
                 [org.omcljs/om "0.9.0"]]
  :jvm-opts ^:replace ["-Xmx1g" "-server"]
  :source-paths ["src" "target/classes"]
  :clean-targets ["out" "release"]
  :target-path "target")
