(ns oarlock.validation
  (:require [schema.core :as s]))

(def validations
  {:task           {:id-sk        s/Str
                    :id-sk-origin s/Keyword
                    :name         s/Str
                    :version      s/Str
                    :description  s/Str}
   :perf-asmt      {:id-sk                    s/Str
                    :id-sk-origin             s/Keyword
                    (s/optional-key :name)    s/Str
                    :version                  s/Str
                    :type                     s/Keyword
                    :duration-rating-days     s/Int
                    :comps                    [s/Str]
                    :credit-value-numerator   s/Int
                    :credit-value-denominator s/Int}
   :user2perf-asmt {:user  s/Str
                    :task  s/Str
                    :grade s/Bool}})

(defn validator
  [entity-type data]
  (let [validation (entity-type validations)]
    (try
      (s/validate
       validation
       data)
      (catch Exception e (.getMessage e)))))
