(ns oarlock
  (:require [clojure.edn :as edn]
            [datomic.api :as d]
            [oarlock.schema :as schema]
            [hatch]))

;; Creation/update functions

(def partitions (hatch/schematode->partitions schema/schema))

(def attrs
  {:perf-asmt
   [:perf-asmt/id-sk
    :perf-asmt/id-sk-origin
    [:perf-asmt/id-sk-with-origin :perf-asmt/id-sk :perf-asmt/id-sk-origin]
    :perf-asmt/name
    :perf-asmt/version
    :perf-asmt/type
    :perf-asmt/duration-rating-days
    :perf-asmt/comps
    :perf-asmt/credit-value-numerator
    :perf-asmt/credit-value-denominator]
   :task
   [:task/id-sk
    :task/id-sk-origin
    [:task/id-sk-with-origin :task/id-sk :task/id-sk-origin]
    :task/name
    :task/version
    :task/description
    :task/comps]
   :section
   [:section/id-sk
    :section/name
    :section/instructors
    :section/perf-asmts
    :section/lms
    :section/status]
   :student2perf-asmt
   [:student2perf-asmt/user
    :student2perf-asmt/perf-asmt
    :student2perf-asmt/grade]
   :student2task
   [:student2task/user :student2task/task :student2task/progress]
   :student2section
   [:student2section/user
    :student2section/section
    :student2section/events
    :student2section/status
    :student2section/access]})

(def tx-entity! (partial hatch/tx-clean-entity! partitions attrs))

;; queue functions

(defn task-in [db-conn task]
  (tx-entity! db-conn :task task))

(defn perf-asmt-in [db-conn perf-asmt]
  (tx-entity! db-conn :perf-asmt (-> perf-asmt
                                     (hatch/attr-as-lookup-refs :comps :comp/id-sk)
                                     (hatch/slam-all :perf-asmt))))

(defn section-in [db-conn section]
  (tx-entity! db-conn :section (-> section
                                   (update-in [:status] #(hatch/slam :section.status (keyword %)))
                                   (update-in [:lms] keyword)
                                   (hatch/attr-as-lookup-refs :instructors :user/id-sk)
                                   (hatch/attr-as-lookup-refs :perf-asmts :perf-asmt/id-sk-with-origin)
                                   (hatch/slam-all :section))))

(defn student2perf-asmt-in [db-conn user2perf-asmt]
  (tx-entity! db-conn :student2perf-asmt user2perf-asmt))
