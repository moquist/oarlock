(ns oarlock.schema
  (:require [datomic-schematode.constraints :as constraints]))

(def schema
  [{:namespace :perf-asmt
    :attrs [[:id-sk :string "Shared key from 3rd parties"]
            [:id-sk-origin :keyword] ; e.g., :moodle, :show-evidence, etc.
            [:id-sk-with-origin :string "Combined id-sk and id-sk-origin" :db.unique/identity]
            [:name :string]
            [:version :string] ; not null
            ;; Example types: :project :course-competency ... TODO: enum?
            [:type :string] ; not null
            [:duration-rating-days :bigint]
            [:comps :ref :many]
            [:credit-value-numerator :bigint]
            [:credit-value-denominator :bigint]]
    :dbfns [(constraints/unique :perf-asmt :name :version :type)
            (constraints/unique :perf-asmt :id-sk :id-sk-origin)]}
   {:namespace :task
    :attrs [[:id-sk :string]
            [:id-sk-origin :keyword] ; e.g., :moodle, :show-evidence, etc.
            [:id-sk-with-origin :string "Combined id-sk and id-sk-origin" :db.unique/identity]
            [:name :string]
            [:version :string]
            [:description :string]
            ;; TODO: is this correct?
            [:comps :ref :many]]
    :dbfns [(constraints/unique :perf-asmt :id-sk :id-sk-origin)]}
   {:namespace :user2perf-asmt
    :attrs [[:user :ref]
            [:perf-asmt :ref]
            ;; TODO: finish this
            [:grade :boolean]]}
   {:namespace :user2task
    :attrs [[:user :ref]
            [:task :ref]
            ;; TODO: finish this
            [:progress :boolean]]}])
