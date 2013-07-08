(ns dreplemographics.data-model.meeting)

(defn init []
  {:col-names [:a]
   :row-names [:X]
   :rows [[0]]})

(defn change-col-name [meeting old-name new-name]
  (assoc meeting :col-names (replace {old-name new-name} (:col-names meeting))))

(defn change-row-name [meeting old-name new-name]
    (assoc meeting :row-names (replace {old-name new-name} (:row-names meeting))))


(defn- contains-col? [meeting col-name]
  (contains? (set (:col-names meeting)) col-name))

(defn- contains-row? [meeting row-name]
  (contains? (set (:row-names meeting)) row-name))


(defn add-col [meeting col-name]
  (if (contains-col? meeting col-name)
    meeting
    (assoc meeting
      :col-names (conj (:col-names meeting) col-name)
      :rows (map #(conj % 0) (:rows meeting)))))

(defn add-row [meeting row-name]
  (if (contains-row? meeting row-name)
    meeting
    (assoc meeting
      :row-names (conj (:row-names meeting) row-name)
      :rows (conj (:rows meeting) (vec (take (count (:col-names meeting)) (repeat 0)))))))


(defn remove-vec-item [v n]
  (vec (concat (subvec v 0 n) (subvec v (inc n))))
  )

(defn remove-col [meeting col-name]
  (let [idx (.indexOf (:col-names meeting) col-name)]

    ;TODO

    (assoc meeting
      :col-names (filterv #(not= % col-name) (:col-names meeting))
      :rows (map (fn [row] (remove-vec-item row idx)) (:rows meeting))
      ))
  )

(defn remove-row [meeting row-name]
  (let [idx (.indexOf (:row-names meeting) row-name)]
    (if (contains? (:rows meeting) idx)
      (assoc meeting
        :rows (vec (concat (subvec (:rows meeting) 0 idx) (subvec (:rows meeting) (inc idx))))
        :row-names (filterv #(not= % row-name) (:row-names meeting)))    
      meeting)))

(defn increment [meeting col row] meeting)
