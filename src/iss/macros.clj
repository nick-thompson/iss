(ns iss.macros
  (:require [iss.constants])
  (:import [cljs.tagged_literals JSValue]))

; (defmacro inline [x] (var-get (ns-resolve 'iss.constants x)))

(defn inline [m]
  "Recursively inlines constant values in a map.
  TODO: GOtta watch out for values which resolve nil in ns-resolve.. like literals"
  (reduce-kv (fn [acc k v]
               (if (map? v)
                 (assoc acc k (JSValue. (inline v)))
                 (assoc acc k (var-get (ns-resolve 'iss.constants v))))) {} m))

;(defn map->js-obj [x]
;  (if (map? x)
;    `(cljs.core/js-obj ~@(mapcat (fn [[k v]] [(name k) (map->js-obj v)]) x))
;    x))

;(defn map->js [x]
;  (list 'js* "~{}" (json/write-str x)))

;(defmacro defstyles [label styles]
;  (let [swag (walk/postwalk-demo (.-val styles))]
;    `(def ~label ~styles)))
;        sty (inline styles)
;        jsform (map->js-obj sty)]
;    `(def ~label (js/__issStyleSheetCreate__ ~jsform))))

(defmacro defstyles [label styles]
  (let [inlined (JSValue. (inline styles))]
    `(def ~label ~inlined)))

(defmacro defclass [label styles]
  (let [inlined (JSValue. (inline (.-val styles)))]
    `(def ~label ~inlined)))
