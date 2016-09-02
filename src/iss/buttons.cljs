(ns iss.buttons
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [clojure.string :refer [join blank?]]
            [iss.constants :refer [light-gray]])
  (:require-macros [iss.macros :as macros :refer [defstyles border]]))

(defstyles buttons
  {:button
   {:display "inline-block"
    :padding "0 3rem"
    :color light-gray
    :fontWeight "200"
    :whiteSpace "nowrap"
    :backgroundColor "transparent"
    :borderRadius 4
    :border (border light-gray)
    :cursor "pointer"
    :outline "none"
    :fontSize "2rem"}
   :accent
   {:color accent-yellow
    :border (border accent-yellow)}})

(defn button [data owner]
  (reify
    om/IRender
    (render [_]
      (let [accent (when (true? (:even data)) (.-accent buttons))
            names (remove blank? [(.-button buttons) accent])
            classes (join " " names)]
        (dom/button
          (clj->js (merge data {:className classes}))
          (om/get-state owner :text))))))
