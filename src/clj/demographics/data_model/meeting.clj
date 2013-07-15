(ns demographics.data-model.meeting)

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
      :rows (vec (map #(conj % 0) (:rows meeting))))))

(defn add-row [meeting row-name]
  (if (contains-row? meeting row-name)
    meeting
    (assoc meeting
      :row-names (conj (:row-names meeting) row-name)
      :rows (conj (:rows meeting) (vec (take (count (:col-names meeting)) (repeat 0)))))))


(defn remove-vec-item [v n]
  (vec (concat (subvec v 0 n) (subvec v (inc n))))
  )

(defn- col-index [meeting col-name]
  (.indexOf (:col-names meeting) col-name))

(defn remove-col [meeting col-name]
  (let [idx (col-index meeting col-name)]
    ; TODO (???) not sure what might remain here...
    (assoc meeting
      :col-names (filterv #(not= % col-name) (:col-names meeting))
      :rows (map (fn [row] (remove-vec-item row idx)) (:rows meeting)))))

(defn- row-index [meeting row-name]
  (.indexOf (:row-names meeting) row-name))

(defn remove-row [meeting row-name]
  (let [idx (row-index meeting row-name)]
    (if (contains? (:rows meeting) idx)
      (assoc meeting
        :row-names (filterv #(not= % row-name) (:row-names meeting))
        :rows (vec (concat (subvec (:rows meeting) 0 idx) (subvec (:rows meeting) (inc idx)))))    
      meeting)))

(defn increment [meeting col-name row-name]
  (let [col (col-index meeting col-name)
        row (row-index meeting row-name)
        rows (:rows meeting)]
    (assoc meeting
      :rows (assoc rows row (assoc (rows row) col (inc ((rows row) col)))))))
