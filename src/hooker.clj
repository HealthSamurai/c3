(ns hooker
  (:require [clojure.string :as str]
            [clojure.java.shell :as shell]))


(defn ref->branch [ref]
  (last (str/split (or ref "") #"/")))

(defn normalize-hook
  "Get repository url, commit hash, brabch and other info from hook"
  [hook]
  {:commit (or (-> hook :head_commit :id)
               (-> hook :commits last :id)
               (-> hook :push :changes first :new :target :hash))

   :branch (or (-> hook :ref ref->branch)
               (-> hook :push :changes first :new :name))

   :clone-url (or (-> hook :repository :clone-url)
                  (-> hook :repository :links :html :href))

   :author (or (-> hook :head_commit :author :name)
               (-> hook :commits last :author :name)
               (-> hook :push :changes first :new :target :author :raw))

   :message (or (-> hook :head_commit :message)
                (-> hook :commits last :message)
                (-> hook :push :changes first :new :target :summary :raw))})


(comment


(def gh-token  "9787c41576b1__________________9d06e7ac6")
(def gt-token  "b3057486____________________79815b76b53")
(def commit    "b6e825____________________3cfb3debd3991")

(def gt-cmd "curl -k -H \"Authorization: token $token\" https://git.rmis.med.cap.ru/api/v1/repos/Alkona/rmis/contents/ci3.yaml?ref=$commit | jq -r .content | base64 -d ")
(def gh-cmd "curl -H  \"Authorization: token $token\" https://raw.githubusercontent.com/HealthSamurai/alkona/$commit/ci3.yaml")

  (:out
   (shell/with-sh-env {:token gt-token :commit commit}
     (shell/sh "sh" "-c" gt-cmd)))

  (:out
   (shell/with-sh-env {:token gh-token :commit commit}
     (shell/sh "sh" "-c" gh-cmd)))
  

  (:out (shell/sh  "sh" "-c" ))

  )

(defn c3-req
  "Create c3.yaml file request from hook request and normalized hook info"
  [req normalized-hook]
  {:url "https://foo/bar"
   :headers {"Authorization" "token ......"}})

(defn normalize-req
  "Get all nessacary information for start build from hook request, such as c3.ayml request and commit information"
  [{body :body :as req}]
  (let [normalized-hook (normalize-hook body)]
    ;; Assoc in to commit iffo  c3.yaml file request
    (assoc normalized-hook
           :c3-req (c3-req req  normalized-hook))))
