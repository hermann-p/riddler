(require '[riddler.main :refer [read-input transform-input parse-input]])
(require '[babashka.cli :as cli])
(require '[babashka.curl :as curl])
(import java.util.UUID)

(def cache-file "cache.edn")
(def riddle-source "https://spiele.zeit.de/eckeapi/game")

(def cli-options {:url {:coerce :string}
                  :file {:coerce :string}
                  :riddle-number {:coerce :string}})

(defn display-riddle [input]
  (->> input transform-input (format "%s") println))

(defn create-from-json [file-name]
  (display-riddle (read-input file-name)))
  

(defn with-cached-url [url f]
  (let [cache (if (.exists (io/file cache-file))
               (read-string (slurp cache-file))
               {})]
    (if-let [cached-file (get cache url)]
      (f (slurp cached-file))
      (let [downloaded-file (str (UUID/randomUUID) ".json")
            content (-> url curl/get :body)]
        (spit downloaded-file content)
        (spit cache-file (assoc cache url downloaded-file))
        (f content)))))

(defn create-from-url [url]
  (with-cached-url url #(display-riddle (parse-input %))))

(defn build-riddle-url [riddle-number]
  (format "%s/%s" riddle-source riddle-number))
  

(defn exec [{:keys [url file riddle-number]}]
  (cond
    file          (create-from-json file)
    url           (create-from-url url)
    riddle-number (create-from-url (build-riddle-url riddle-number))))

(exec (cli/parse-opts *command-line-args* {:spec cli-options}))
