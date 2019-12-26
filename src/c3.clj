(ns c3
  (:require [org.httpkit.server]
            [cheshire.core]
            [sodium])
  (:gen-class))

(defn hello [ctx req]
  {:status 200
   :body "
<html> <body>
<center><h1>Welcome to C3</h1></center>
</body> </html>"})

(defn hook [{secret :secret :as ctx} {body :body :as req}]
  (let [body (cheshire.core/parse-string body keyword)
        ;; url  (str/replace (get-in body [:repository :contents_url]) #"\{\+path\}$" "/")
        ;; ref  (-> body :commits last :id) 
        ;; key (sodium/decrypt secret (:key params))
        ;;url (-> body :commits last) 
        c3 {:msg "hello"} #_(*http ctx (gh-file-req url key ref "c3.yaml"))]

    {:status 200
     :body (cheshire.core/generate-string c3)}))

(defn read-body [body]
  (when body
    (if (string? body)
      body
      (slurp body))))

(defn encrypt [{secret :secret} {body :body :as req}]
  {:status 200
   :header {"Content-Type" "text"}
   :body   (sodium/encrypt secret body)})

(defn decrypt [{secret :secret} {body :body  :as req}]
  {:status 200
   :header {"Content-Type" "text"}
   :body (sodium/decrypt secret body)})

(defn handle [ctx {uri :uri body :body :as req}]
  (let [req (assoc req :body (read-body body))]
    (cond
      (= uri "/enc") (encrypt ctx req)
      (= uri "/dec") (decrypt ctx req)
      (and (= :get (:request-method req)) (= uri "/")) (hello ctx req)
      (= uri "/")    (hook ctx req))))

(defn mk-handler [ctx]
  (fn [req] (handle ctx req)))

(defn get-secret []
  (if-let [secret (System/getenv "C3_SECRET")]
    secret
    (throw
     (Exception.
      (str "C3_SECRET environment variable is required. Here is new one generated for you - "
           (sodium/gen-key))))))

(defn start [{:keys [port secret]}]
  (println "Start C3 server at: " port)
  (org.httpkit.server/run-server (mk-handler {:secret secret}) {:port port}))

(defn -main [& args]
  (let [srv (atom nil)]
    (.addShutdownHook (Runtime/getRuntime)
                      (Thread. (fn [] (@srv) (println "Good by!"))))
    (reset! srv (start {:port 8668 :secret (get-secret)}))))


(comment

  (def srv (start {:port 8668 :secret (get-secret)}))
  (srv)

  )
