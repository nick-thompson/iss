(ns iss.macros
  (:require [iss.constants]
             [cljs.core]))

; (defmacro inline [x] (var-get (ns-resolve 'iss.constants x)))

(defn inline [m]
  "Recursively inlines constant values in a map.
  TODO: GOtta watch out for values which resolve nil in ns-resolve.. like literals"
  (reduce-kv (fn [acc k v]
               (if (map? v)
                 (assoc acc k (inline v))
                 (assoc acc k (var-get (ns-resolve 'iss.constants v))))) {} m))

(defn map->js-obj [x]
  (when-not (nil? x)
    (cond
      (map? x) (let [m (cljs.core/js-obj)]
                 (doseq [[k v] x]
                   (aset m (name x) (map->js-obj v)))
                 m)
      :else x)))

(defmacro defstyles [label styles]
  (let [sty (inline styles)
        jsform (map->js-obj sty)]
    `(def ~label ~sty)))
