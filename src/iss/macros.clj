(ns iss.macros
  (:require [iss.constants])
  (:import [cljs.tagged_literals JSValue]))

(defn inline
  "Recursively inlines constant values in a map."
  [m]
  (reduce-kv (fn [acc k v]
               (if (map? v)
                 (assoc acc k (JSValue. (inline v)))
                 (if (symbol? v)
                   (assoc acc k (var-get (ns-resolve 'iss.constants v)))
                   (assoc acc k v)))) {} m))

(defmacro defstyles
  "Rewrites a style definition with inlined values and a syntax marker."
  [label styles]
  (let [inlined (JSValue. (inline styles))]
    `(def ~label (js/__issStyleSheetCreate__ ~inlined))))
