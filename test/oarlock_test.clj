(ns oarlock-test
  (:require [clojure.test :refer :all]
            [datomic.api :as d]
            [oarlock.test-config :as ot-config]
            [oarlock.testslib :as n-tl]
            [datomic-schematode :as dst]))

(use-fixtures :once ot-config/testing-fixture)

;; TODO: write some tests!
(comment

  (deftest create-comps
    (testing "raw assertion"
      (is (n-tl/ensure-tx (dst/tx (:db-conn ot-config/system)
                                  :enforce
                                  [{:db/id (d/tempid :db.part/user)
                                    :comp/id-sk "i am a shared key"
                                    :comp/name "COMP there it is"}]))))
    (let [comp {:comp/name "I will keep typing"
                :comp/version "v1"
                :comp/status :comp.status/active
                :comp/id-sk "yeep"}]
      (testing "create competency"
        (is (n-tl/ensure-tx (oarlock/tx-entity! (:db-conn ot-config/system) :comp comp))))
      (testing "get transacted comp"
        (is (= (sort comp)
               (sort (into {} (oarlock/get-competency (:db-conn ot-config/system) "yeep"))))))
      (testing "create duplicate competency"
        (is (n-tl/should-throw @(oarlock/tx-entity! (:db-conn ot-config/system)
                                                    :comp
                                                    (assoc comp :comp/id-sk "jeep")))))))

  (deftest update-comps
    (let [comp {:comp/name "can I update?"
                :comp/version "v1"
                :comp/status :comp.status/active
                :comp/id-sk "yeep"}
          comp-archived (assoc comp :comp/status :comp.status/archived)]
      (testing "update competency"
        (is (n-tl/ensure-tx (oarlock/tx-entity! (:db-conn ot-config/system) :comp comp)))
        (is (n-tl/ensure-tx (oarlock/tx-entity! (:db-conn ot-config/system) :comp comp-archived)))
        (is (= (sort comp-archived)
               (sort (into {} (oarlock/get-competency (:db-conn ot-config/system) "yeep"))))))))

)
