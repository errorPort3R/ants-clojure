(ns ants-clojure.core
  (:require [clojure.java.io :as io])
  (:gen-class :extends javafx.application.Application))

(def width 800)
(def height 600)
(def ant-count 200)
(def ants (atom []))
(def min-distance 15)

(defn create-ants []
  (for [i (range 0 ant-count)]
    {:x (rand-int width)
     :y (rand-int height)
     :color (javafx.scene.paint.Color/BLACK)}))



(defn draw-ants! [context]
  (.clearRect context 0 0 width height)
  (doseq [ant @ants]
    (.setFill context (:color ant))
    (.fillOval context (:x ant) (:y ant) 5 5)))

(defn move-ant [ant]
  (let [x-change (- (* 2 (rand)) 1)
        y-change (- (* 2 (rand)) 1)]
    (assoc ant 
      :x (+ (:x ant) x-change)
      :y (+ (:y ant) y-change))))


(defn aggravate-ant [ant]
  (let [nearby-ants (filter (fn [a]
                              (and (< (Math/abs (- (:x ant) (:x a))) min-distance)
                                   (< (Math/abs (- (:y ant) (:y a))) min-distance)))
                      @ants)
        nearby-count (count nearby-ants)]
    (assoc ant
      :color
      (if (> nearby-count 1)
        javafx.scene.paint.Color/RED 
        javafx.scene.paint.Color/BLACK))))
  
(defn move-ants []
  (doall (pmap aggravate-ant (pmap move-ant @ants))))

(def last-timestamp (atom 0))

(defn fps [current-timestamp]
  (let [diff  (- current-timestamp @last-timestamp)
        diff-seconds (/ diff 1000000000)]
    (int (/ 1 diff-seconds))))

(defn -start[app stage]
  (let [root (javafx.fxml.FXMLLoader/load (io/resource "main.fxml"))
        scene (javafx.scene.Scene. root width height)
        canvas (.lookup scene "#canvas")
        context (.getGraphicsContext2D canvas)
        fps-label ( .lookup scene "#fps")
        timer (proxy [javafx.animation.AnimationTimer] []
                (handle [now]
                  (reset! ants (move-ants))
                  (draw-ants! context)
                  (.setText fps-label (str (fps now)))
                  (reset! last-timestamp now)))]
    (.setTitle stage "Ants")
    (.setScene stage scene)
    (.show stage)
    (reset! ants (create-ants))
    (.start timer)))
    

(defn -main []
  (javafx.application.Application/launch ants_clojure.core(into-array String [])))
