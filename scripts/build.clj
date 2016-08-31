(require '[cljs.build.api :as b])
(require '[clojure.java.shell :refer [sh]])

(println "Building ...")

(defn extract-styles [file]
  (let [path (.getAbsolutePath (:file file))
        proc (sh "./node_modules/.bin/jscodeshift" "-t" "./transforms/iss.js" path)]
    (println (:out proc))
    file))

(let [start (System/nanoTime)]
  (b/build "src"
    {:main 'iss.core
     :libs ["lib/stylesheet.js"]
     :plugin extract-styles
     :language-in :ecmascript6
     :language-out :ecmascript5-strict
     :output-to "out/iss.js"
     :output-dir "out"
     :verbose true})
  (println "... done. Elapsed" (/ (- (System/nanoTime) start) 1e9) "seconds"))

(shutdown-agents)
