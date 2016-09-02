(ns iss.macros
  (:require [iss.constants]
            [clojure.walk :as walk])
  (:import [cljs.tagged_literals JSValue]))

(defn ->js-val*
  "Recursive helper method for wrapping maps in JSValue types."
  [m]
  (letfn [(reducer [acc k v]
            (if (map? v)
              (assoc acc k (JSValue. (->js-val* v)))
              (assoc acc k v)))]
    (reduce-kv reducer {} m)))

(defn ->js-val
  "Recursively wraps map forms in JSValue types."
  [m]
  (JSValue. (->js-val* m)))

(defn inline
  "Recursively inlines constant values in a form."
  [m]
  (letfn [(visit [x]
            (if-not (symbol? x)
              x
              (let [res (ns-resolve 'iss.constants x)]
                (if (nil? res)
                  x
                  (var-get res)))))]
    (walk/postwalk visit m)))

(defmacro defstyles
  "Rewrites a style definition with inlined values and a syntax marker."
  [label styles]
  (let [inlined (->js-val (inline styles))]
    `(def ~label (js/__issStyleSheetCreate__ ~inlined))))

(defmacro gradient
  "Simple diagonal linear gradient from one corner to the other."
  [from to]
  (str "linear-gradient(to top left," from ", " to ")"))

(defmacro border
  "A one pixel solid border of the given color."
  [color]
  (str "1px solid " color))
