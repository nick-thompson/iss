(ns iss.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [iss.buttons :as buttons]
            [iss.constants :refer [black system-font-large default-padding]])
  (:require-macros [iss.macros :as macros :refer [defstyles lighten add]]))

(enable-console-print!)

(def app-state
  (atom
    {:widgets
     [{:my-number 16}
      {:my-number 23}]}))

(defstyles even-odd
  {:container
    {:alignItems "center"
     :backgroundImage "linear-gradient(to top left, #2C3E50, #4CA1AF)"
     :color black
     :display "flex"
     :flexGrow 1
     :font system-font-large
     :justifyContent "space-around"
     :padding (add default-padding 8)}
   :widget
    {:alignItems "center"
     :display "flex"
     :flexBasis "50%"
     :flexDirection "column"
     :zIndex 1}
   :title
    {:color "#eaeaea"
     :margin "1rem"
     :fontSize "4.2rem"}})

(defmulti even-odd-widget
  (fn [props _] (even? (:my-number props))))

(defmethod even-odd-widget true
  [props owner]
  (reify
    om/IWillMount
    (will-mount [_]
      (println "Even widget mounting"))
    om/IWillUnmount
    (will-unmount [_]
      (println "Even widget unmounting"))
    om/IRender
    (render [_]
      (dom/div #js {:className (.-widget even-odd)}
        (dom/h2 #js {:className (.-title even-odd)}
          (str (:my-number props)))
        (om/build buttons/button
          {:onClick #(om/transact! props :my-number inc)}
          {:init-state {:text "+"}})))))

(defmethod even-odd-widget false
  [props owner]
  (reify
    om/IWillMount
    (will-mount [_]
      (println "Odd widget mounting"))
    om/IWillUnmount
    (will-unmount [_]
      (println "Odd widget unmounting"))
    om/IRender
    (render [_]
      (dom/div #js {:className (.-widget even-odd)}
        (dom/h2 #js {:className (.-title even-odd)}
          (str (:my-number props)))
        (om/build buttons/button
          {:onClick #(om/transact! props :my-number inc)}
          {:init-state {:text "+"}})))))

(defn app [props owner]
  (reify
    om/IRender
    (render [_]
      (apply dom/div #js {:className (.-container even-odd)}
        (om/build-all even-odd-widget (:widgets props))))))

(om/root app app-state
  {:target (.-body js/document)})
