(ns riddler.styles
  (:require [gaka.core :refer [css]]))

(def cell-width "9mm")
(def cell-height "9mm")
(def font :sans-serif)

(def title-style [:font-family font
                  :font-weight "bold"
                  :padding-right "1mm"])

(def style
  (css [:body
        [:.puzzle
         :display "flex"
         :justify-content "center"]
        [:.grid
         :position "relative"
         :display "flex"
         :flex-direction "column"
         [:.grid-row
          :display "flex"]
         [:.cell
          :width cell-width
          :height cell-height
          :border "1px solid black"
          :margin-right "-1px"
          :margin-bottom "-1px"
          :box-sizing :border-box]
         [:.cell_number
           :padding-left "2px"
           :font-size "smaller"]
         [:.start--v :border-top-width "3px"]
         [:.start--h :border-left-width "3px"]
         [:.empty :border-color "transparent"]
         [:img
          :transform (format "translate(calc(-0.25 * %s), calc(-0.25 * %s))" cell-width cell-height)]]
        (vec (cons :.title title-style))
        (vec (cons :.question__id title-style))
        [:.questions :padding-top "3mm"]
        [:.question
         :padding-right "2mm"
         :font-family font]]))
