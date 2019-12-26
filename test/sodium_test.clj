(ns sodium-test
  (:require [sodium :as sut]
            [clojure.test :refer :all]))


(deftest sodium

  (def s-key (sut/gen-key))

  (def s "hello")
  (is (= s (sut/decrypt s-key (sut/encrypt s-key s)) ))

  (def s "{\"fo\": \"bar\"}")
  (is (= s (sut/decrypt s-key (sut/encrypt s-key s)) ))

  )
