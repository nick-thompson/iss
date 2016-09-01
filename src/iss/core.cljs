(ns iss.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [iss.constants :refer [black white system-font-large]])
  (:require-macros [iss.macros :as macros :refer [defstyles]]))

(enable-console-print!)

(def app-state (atom {:foo "bar"}))

(defn widget [data owner]
  (reify
    om/ICheckState
    om/IInitState
    (init-state [_]
      {:count 0})
    om/IWillMount
    (will-mount [_]
      (println "Hello widget mounting"))
    om/IWillUnmount
    (will-unmount [_]
      (println "Hello widget unmounting"))
    om/IRenderState
    (render-state [_ {:keys [count]}]
      (println "Render!")
      (dom/div #js {:className (.-container bump-app)}
        (dom/h2 nil "Hello world!")
        (dom/p nil (str "Count: " count))
        (dom/button
          #js {:onClick
               #(do
                  (println "I can read!" (:foo data))
                  (om/update-state! owner :count inc))}
          "Bump!")
        (dom/button
          #js {:onClick
               #(do
                  (println "I can also read!" (:foo data))
                  (om/update-state! owner :count identity))}
          "Do Nothing")))))

(defstyles bump-app
  {:container
    {:backgroundColor white
     :color black
     :font system-font-large}})

(om/root widget app-state
  {:target (.-body js/document)})
