(ns key-translation.routes.event
  (:import [java.io File])
  (:require [compojure.core :refer :all]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [cheshire.core :refer [generate-string]]
            [liberator.core :refer [defresource resource request-method-in]]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.java.io :refer [copy make-parents]]
            [clojure.string :refer [trim]]))

(def events (atom {}))
(defn- add-to [storage payload]
  (swap! storage merge @storage payload))

(defn- route-param [context var-name]
  (get-in context [:request :route-params var-name]))

(defresource get-events
  :allowed-methods [:get]
  :malformed? (fn [context]
                          (let [tenant-id (route-param context :tenant)]
                            (println tenant-id)
                            (empty? (get @events tenant-id))))
  :handle-ok (fn [context] 
                      (let [tenant-id (route-param context :tenant)
                             event (route-param context :event)]
                        (get-in @events [tenant-id event])))
  :available-media-types ["application/json"])

(defn- open-file [filename]
  (with-open [in-file (io/reader filename)]
      (csv/read-csv in-file)))

(defn update-event [filename]
    (letfn [(select- [row indx]
                (trim (nth row indx)))
              (process-row [row]
                (-> [(select- row 1)  (select- row 2)]))]

      (let [contents (open-file filename)]
        (->> contents
          (map process-row)
          (into {})))))

(defn- csv-tempfile [filename]
 (java.io.File/createTempFile filename ".csv"))


(defn upload-file [tenant-id folder file]
  (let [file-name (file :filename)
         size (file :size)
         actual-file (file :tempfile)
         destination (csv-tempfile (format "%s-%s-%s" tenant-id folder file-name))]
         (make-parents destination)
       (copy actual-file (File. destination))))

(defresource replace-events
  :allowed-methods [:post]
  :post! 
  (fn [context]
    (let [tenant-id (route-param context :tenant)
           payload (route-param context :event)
           event-file (get-in context [:request :multipart-params "event"])
           id-file (get-in context [:request :multipart-params "id"])
           updated-event (update-event (id-file :tempfile))
           event-by-tenant {tenant-id updated-event}
           upload-file (partial upload-file tenant-id)
           ]
           ;;(clojure.pprint/pprint (:file context))
           ;;(def ctx context)
           ;;(if event-file (upload-file "event" event-file))
           ;;(if id-file (upload-file "id" id-file))
           (add-to events event-by-tenant)))
  :handle-created (fn [_] (generate-string @events))
  :available-media-types ["application/json"])

(defroutes event-routes
  (POST ["/tenants/:tenant/events/:event" :tenant #".+" :event #".+"] [tenant event] replace-events)
  (GET ["/tenants/:tenant/events/:event" :tenant #".+" :event #".+"] [tenant event] get-events))
