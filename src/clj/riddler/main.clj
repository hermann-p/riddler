(ns riddler.main
  (:require [cheshire.core :as json]
            [hiccup2.core :as h]))

(defn parse-input [string]
  (json/parse-string string true))

(defn read-input [file-name]
  (parse-input (slurp file-name))) ;; keywordize

(defn get-grid-size [input {:keys [dir coord]}]
  (->> (:questions input)
       (filter (fn [{:keys [direction]}] (= dir direction)))
       (reduce (fn [acc question]
                 (max acc (+ (get question coord 0) (:length question 0))))
               1)
       dec))

(defn get-grid-dims [input]
  [(get-grid-size input {:dir "h", :coord :xc})
   (get-grid-size input {:dir "v", :coord :yc})])

(defn to-cell-list [{:keys [xc yc length direction answer nr]}]
  (cons [[xc yc] {:kind (if (= "v" direction) :start--v :start--h)
                  :letter (subs answer 0 1)
                  :nr nr}]
        (map (fn [d]
               [[(if (= "v" direction) xc (+ d xc))
                 (if (= "v" direction) (+ d yc) yc)]
                {:class :cell, :letter (subs answer d (inc d))}])
             (range 1 length))))

(defn from-cell-list [cells]
  (reduce (fn [acc [pos {:keys [kind nr]}]]
            (update acc pos (fn [elem]
                              (if elem
                                (-> elem
                                    (update :kinds conj kind)
                                    (#(if nr (assoc % :nr nr) %)))
                                {:kinds #{kind}
                                 :nr nr}))))
          {}
          cells))

(defn to-cell-html [cell-map]
  (into {}
        (for [[coords {:keys [kinds nr]}] cell-map]
          [coords [:div.cell {:class kinds}
                   (when nr [:span.cell_number nr])]])))

(defn get-questions [direction questions]
  (->> questions
       (filter #(= direction (:direction %)))
       (sort-by :nr)
       (map (fn [{:keys [nr question]}]
              [:span.question [:span.question__id nr] [:span.question__text] question]))))

(defn transform-input [input]
  (let [[width height] (get-grid-dims input)
        questions (:questions input)
        lookup (->> questions
                (mapcat to-cell-list)
                from-cell-list
                to-cell-html)
        grid (for [y (range 1 (inc height))]
              [:div.grid-row (for [x (range 1 (inc width))]
                              (get lookup [x y] [:div.cell.empty]))])]
    (h/html [:head
             [:meta {:link "rel"
                     :href "styles.css"}]]
           [:body
            [:section.grid grid]
            [:section.questions.questions--h
             [:span.title "Waagrecht:"]
             (get-questions "h" questions)]
            [:section.questions.questions--v
             [:span.title "Senkrecht:"]
             (get-questions "v" questions)]])))

(comment
  {:questions [{:id :number
                :game_id :number
                :nr :number
                :question :string
                :answer :string
                :xc :number
                :yc :number
                :direction ["v" "h"]
                :description :string
                :length :number}]}
  (get-grid-dims (read-input "969.json"))
  (spit "index.html"  (transform-input (read-input  "969.json")))
  (from-cell-list
   (to-cell-list {:xc 3, :yc 1, :direction "h", :length 11, :answer "UEBERHOLUNG"})))

(defn -main [& args])
