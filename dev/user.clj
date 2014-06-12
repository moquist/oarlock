(ns user
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.pprint :refer (pprint)]
            [clojure.repl :refer :all]
            [clojure.test :as test]
            [datomic.api :as d]
            [clojure.tools.namespace.repl :refer (refresh refresh-all)]
            [oarlock]
            [oarlock.test-config :as ot-config]
            [datomic-schematode :as dst]
            [clojure.edn :as edn]))

;; N.B.: (ns cljs.user (:use [clojure.zip :only [insert-child]])) (see http://stackoverflow.com/questions/12879027/cannot-use-in-clojurescript-repl)

(defn go
  "Start with Datomic."
  []
  (ot-config/start!))

(defn reset
  ([] (reset 'user/go))
  ([go-fn-symbol]
     (ot-config/stop!)
     (refresh :after go-fn-symbol)))

(defn touch-that
  "Execute the specified query on the current DB and return the
   results of touching each entity.

   The first binding must be to the entity.
   All other bindings are ignored."
  [query & data-sources]
  (map #(d/touch
         (d/entity
          (d/db (:db-conn ot-config/system))
          (first %)))
       (apply d/q query (d/db (:db-conn ot-config/system)) data-sources)))

(defn ptouch-that
  "Example: (ptouch-that '[:find ?e :where [?e :user/username]])"
  [query & data-sources]
  (pprint (apply touch-that query data-sources)))

(comment

  ;; TODO: put working examples here
  (ptouch-that '[:find ?e :where [?e :task/name]])

  ;; Utility fns
  ;; -----------------------
  (defn html->hiccup [html]
    (-> html
        hickory/parse-fragment
        (->> (map hickory/as-hiccup))))

  (defn renode
    "Take a rendered enlive template (html) and turn it back into a seq of enlive nodes"
    [template]
    (en/html-snippet (n-tpl/render template)))

)
