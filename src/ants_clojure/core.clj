(ns ants-clojure.core
  (:require [clojure.java.io :as io])
  (:gen-class :extends javafx.application.Application))

(def width 800)
(def height 600)

(defn -start[app stage]
  (let [root (javafx.fxml.FXMLLoader/load (io/resource "main.fxml"))
        scene (javafx.scene.Scene. root width height)]
    (.setTitle stage "Ants")
    (.setScene stage scene)
    (.show stage)))

(defn -main []
  (javafx.application.Application/launch ants_clojure.core(into-array String [])))
