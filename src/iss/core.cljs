(ns iss.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [iss.constants :refer [black system-font-large default-padding]])
  (:require-macros [iss.macros :as macros :refer [defstyles lighten add]]))

(enable-console-print!)

(def app-state (atom {:foo "bar"}))

(defstyles bump-app
  {:container
    {:alignItems "center"
     :backgroundColor (lighten black 10)
     :color black
     :display "flex"
     :flexDirection "column"
     :font system-font-large
     :padding (add default-padding 8)}})

(defn app [data owner]
  (reify
    om/ICheckState
    om/IInitState
    (init-state [_]
      {:count 0})
    om/IWillMount
    (will-mount [_]
      (println "Hello app mounting"))
    om/IWillUnmount
    (will-unmount [_]
      (println "Hello app unmounting"))
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

(om/root app app-state
  {:target (.-body js/document)})
