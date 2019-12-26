(ns c3-test
  (:require [c3 :as sut]
            [clojure.test :refer :all]
            [clojure.java.io :as io]))

(def ctx
  {:secret "4B81FEE3EF5877F899AB0325C0CFE786489433D82DC3909B22B6E638C81B5A36"})

(deftest c3

  (def enc-key
    (:body (sut/handle ctx {:uri "/enc" :body "mysecretkey"})))

  (def dec-key
    (:body (sut/handle ctx {:uri "/dec" :body enc-key})))

  (is (= dec-key "mysecretkey"))

  #_(sut/handle ctx {:uri "/"
                   :request-method :post
                   :params {:key enc-key}
                   :body (slurp (io/resource "fixtures/gh-commit.json"))})


  )

