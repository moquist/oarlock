(ns oarlock
  (:require [clojure.edn :as edn]
            [datomic.api :as d]
            [oarlock.schema :as schema]
            [hatch]))

;; Creation/update functions

(def partitions (hatch/schematode->partitions schema/schema))

(def valid-attrs (hatch/schematode->attrs schema/schema))

(def tx-entity! (partial hatch/tx-clean-entity! partitions valid-attrs))

;; queue functions

(defn task-in [db-conn task]
  (tx-entity! db-conn :task task))

(defn perf-asmt-in [db-conn perf-asmt]
  (tx-entity! db-conn :perf-asmt perf-asmt))

(defn user2perf-asmt-in [db-conn user2perf-asmt]
  (tx-entity! db-conn :user2perf-asmt user2perf-asmt))
