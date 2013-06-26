(ns demographics.data-model)

(def meeting-attendees (atom {}))

(def meeting-template {:male {:frog 0 :fish 0}
                       :female {:frog 0 :fish 0}})

(defn- add-participant [attendees meeting-id attr1 attr2]
        (update-in attendees [meeting-id attr1 attr2] inc)) 

(defn- add-new-meeting [meetings meeting-id]
  (assoc meetings meeting-id meeting-template))

(defn create-new-meeting!
  "Creates a brand new meeting with the given ID"
  [id]
  (swap! meeting-attendees add-new-meeting id))

(defn meeting-exists?
  "Checks whether the meeting with the given ID exists"
  [meeting-id]
  (contains? @meeting-attendees (keyword meeting-id)))

(defn get-meeting-info
  "Return the current info on a particular meeting"
  [id]
  ((keyword id) @meeting-attendees))

(defn register-participant!
  "Adds a participant to a given meeting"
  [meeting-id attr1 attr2]
  (swap! meeting-attendees add-participant meeting-id attr1 attr2))
