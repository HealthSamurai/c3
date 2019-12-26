(ns hooker-test
  (:require [hooker :as sut]
            [clojure.test :refer :all]
            [matcho.core :refer [match]]
            [cheshire.core :as json]
            [clojure.java.io :as io]))

;; Github
(def gh (json/parse-string (slurp (io/resource "fixtures/gh-hook.json")) keyword))
;; Bitbucket 
(def bb (json/parse-string (slurp (io/resource "fixtures/gh-hook.json")) keyword))
;; Gitea 
(def gt (json/parse-string (slurp (io/resource "fixtures/gt-hook.json")) keyword))

(deftest hooker
  (def normalized-hook
    {:commit "228ff88162933626efaa82ce3b70b58546824548"
     :branch "master"
     :author "Aitem"
     :message "add hook payload"
     :clone-url "https://github.com/niquola/c3.git"})

  (testing "GitHub"
    (match (sut/normalize-hook gh) normalized-hook))
  (testing "BitBucket"
    (match (sut/normalize-hook bb) normalized-hook))
  (testing "Gitea"
    (match (sut/normalize-hook gt) normalized-hook))
  )

