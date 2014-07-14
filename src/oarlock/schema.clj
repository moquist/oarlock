(ns oarlock.schema
  (:require [datomic-schematode.constraints :as constraints]))

(def schema
  [{:namespace :perf-asmt
    :attrs [[:id-sk :string "Shared key from 3rd parties"]
            [:id-sk-origin :keyword] ; e.g., :moodle, :show-evidence, etc.
            [:id-sk-with-origin :string "Combined id-sk and id-sk-origin" :db.unique/identity]
            [:name :string]
            [:version :string] ; not null
            [:type :string] ; not null
            [:duration-rating-days :long]
            [:comps :ref :many]
            [:credit-value-numerator :long]
            [:credit-value-denominator :long]]
    :dbfns [(constraints/unique :perf-asmt :name :version :type)
            (constraints/unique :perf-asmt :id-sk :id-sk-origin)]}
   {:namespace :task
    :attrs [[:id-sk :string]
            [:id-sk-origin :keyword] ; e.g., :moodle, :show-evidence, etc.
            [:id-sk-with-origin :string "Combined id-sk and id-sk-origin" :db.unique/identity]
            [:name :string]
            [:version :string]
            [:description :string]
            [:comps :ref :many]]
    :dbfns [(constraints/unique :perf-asmt :id-sk :id-sk-origin)]}
   {:namespace :section
    :attrs [[:id-sk :string]
            [:id-sk-origin :keyword]
            [:id-sk-with-origin :string :db.unique/identity]
            [:instructors :ref :many]
            [:perf-asmts :ref :many]
            [:status :enum [:active :archived :preactive]]]}
   {:namespace :student2perf-asmt
    :attrs [[:user :ref]
            [:perf-asmt :ref]
            [:grade :boolean]]}
   {:namespace :student2task
    :attrs [[:user :ref]
            [:task :ref]
            [:progress :boolean]]}
   {:namespace :student2section
    :attrs [[:user :ref]
            [:section :ref]
            [:events :ref :many] ;; examples: welcome-email-sent, welcome-call-done, follow-up-call-done, etc.
            [:status :enum [:active :completed :withdrawn-failing :withdrawn-no-grade]]
            [:access :enum [:assigned :enabled :contact-instructor]]]}])
