(ns demographics.data-model.data-model)

(defprotocol MeetingsStore
  "A collection of meetings"
  (create-new-meeting! [id] "Creates a new meeting in the collection, with the specified identifier")
  ;; TODO: How about creating a new meeting without a predefined ID?
  (meeting-exists? [id] "Checks whether the meeting with the given ID exists")
  (get-meeting-info [id] "Return the current info on a particular meeting")
  (register-participant! [meeting-id attr1 attr2] "Adds a participant to a given meeting")  
  )


(def meetings (atom {}))

(def meeting-template {:male {:frog 0 :fish 0}
                       :female {:frog 0 :fish 0}})

(defn- add-participant [attendees meeting-id attr1 attr2]
        (update-in attendees [meeting-id attr1 attr2] inc)) 

(defn- add-new-meeting [meetings meeting-id]
  (assoc meetings meeting-id meeting-template))

(defn create-new-meeting!
  "Creates a brand new meeting with the given ID"
  [id]
  (swap! meetings add-new-meeting id))

(defn meeting-exists?
  "Checks whether the meeting with the given ID exists"
  [meeting-id]
  (contains? @meetings (keyword meeting-id)))

(defn get-meeting-info
  "Return the current info on a particular meeting"
  [id]
  ((keyword id) @meetings))

(defn register-participant!
  "Adds a participant to a given meeting"
  [meeting-id attr1 attr2]
  (swap! meetings add-participant meeting-id attr1 attr2))
