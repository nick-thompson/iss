(ns iss.buttons
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [iss.constants :refer [light-gray]])
  (:require-macros [iss.macros :as macros :refer [defstyles border]]))

(defstyles buttons
  {:button
   {:display "inline-block"
    :padding "0 3rem"
    :color light-gray
    :font-weight 600
    :whiteSpace "nowrap"
    :backgroundColor "transparent"
    :borderRadius 4
    :border (border light-gray)
    :cursor "pointer"
    :outline "none"
    :fontSize "2rem"}})

(defn button [data owner]
  (reify
    om/IRender
    (render [_]
      (dom/button
        (clj->js (merge data {:className (.-button buttons)}))
        (om/get-state owner :text)))))
