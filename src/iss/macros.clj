(ns iss.macros
  (:require [iss.constants])
  (:import [cljs.tagged_literals JSValue]))

(defn inline [m]
  "Recursively inlines constant values in a map.
  TODO: GOtta watch out for values which resolve nil in ns-resolve.. like literals"
  (reduce-kv (fn [acc k v]
               (if (map? v)
                 (assoc acc k (JSValue. (inline v)))
                 (assoc acc k (var-get (ns-resolve 'iss.constants v))))) {} m))

(defmacro defstyles [label styles]
  (let [inlined (JSValue. (inline styles))]
    `(def ~label (js/__issStyleSheetCreate__ ~inlined))))
