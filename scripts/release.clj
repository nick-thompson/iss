(require '[cljs.build.api :as b])

(println "Building ...")

(let [start (System/nanoTime)]
  (b/build "src"
    {:main 'iss.core
     :libs ["lib/stylesheet.js"]
     :language-in :ecmascript6
     :language-out :ecmascript5-strict
     :output-to "release/iss.js"
     :output-dir "release"
     :optimizations :advanced
     :verbose true})
  (println "... done. Elapsed" (/ (- (System/nanoTime) start) 1e9) "seconds"))
