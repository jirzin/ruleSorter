(ns rule-sorter.core
  (:gen-class))

(def language-mods (hash-set :cz :eng))
(def language-select (ref :cz))
(def data-path "data/")

(def questions (sorted-map  :1-name {:cz "Jaké je tvoje křestní jméno?"
                                     :eng "What is your name"}
                            :2-surename {:cz "Jaké je tvoje příjmenní?"
                                         :eng "What is your surename?"}
                            :3-age {:cz "Kolik ti je let?"
                                    :eng "How old are you"}
                            :4-sex {:cz "K jakému pohlaví sám sebe řadíš?"
                                    :eng "What is te sex that suits you the best"}
                            :5-occupation {:cz "Jaké je tvé povolání?"
                                           :eng "What is your occupation?"}
                            :6-rule {:cz "Napiš své pravidlo pro kolektivní hru:"
                                     :eng "Write the rule for collective game:"}))

(def rules (ref (sorted-map :0 {:1-name "Jirka"
                                :2-surename "Rouš"
                                :3-age "29"
                                :4-sex "male"
                                :5-occupation "student"
                                :6-rule "Každý z návštěvníků výstavy musí jednou za deset minut po dobu své návštěvy v galerii odejít z místnosti kde právě přebývá do místonsti jiné a tam počkat do dalšího znamení pro přesun, místnost si může vybrat libovolnou, krom té ve které se v době změny nachází."})))


(defn question
  ([lselect k]
     (let [l @lselect]
       (println (l (k questions)))
       (if-let [v (read-line)]
         v)))
  ([lselect k q]
     (let [l @lselect]
       (println (l (k q)))
       (if-let [v (read-line)]
         v))))


(defn change-language [lselect lmods]
  (let [l @lselect]
    (cond (= l :cz) (do (println "K dispozici jsou tyto jazykové mutace:"))
          (= l :eng) (do (println "There are following langugae mutaions:")))
    (println lmods)
    (cond (= l :cz) (do (println "Prosím, zvolte jednu z nabízených možností."))
          (= l :eng) (do (println "Please choose one of the offered options.")))
    (let [new-l (keyword (read-line))
          t (contains? lmods new-l)]
      (if t
        (dosync (ref-set lmods new-l)
                (cond (= new-l :cz) (println "Jazyk je změněn.")
                      (= new-l :eng) (println "The language is changed.")))
        (do (cond (= l :cz) (println "Vaše volba není validní. Prosím zkuste to znovu.")
                  (= l :eng) (println "Your choise is not walid. Please try again.")))))))

(defn generate-entry [questions lselect]
  (loop [answers (map #(question lselect %1 questions)
                      (keys questions))
         k (keys questions)
         entry {}]
    (if (not (empty? k))
      (recur (butlast answers)
             (butlast k)
             (assoc entry
               (last k)
               (last answers)))
      entry)))

(defn assoc-new-rule [rules questions language-select]
  (dosync (ref-set rules
                 (assoc @rules
                   (keyword (str (count @rules)))
                   (generate-entry questions language-select)))))

(defn save-rules []
  (spit (str data-path (read-line)) (prn-str @rules)))

(defn load-rules [data-path file-name]
  (read-string (slurp (str data-path file-name) )))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))



(spit "20160112-rules-testing.dat" (prn-str @rules))


(slurp "20160112-rules-testing.dat")
(def rules-from-file (read-string (slurp "20160112-rules-testing.dat")))
(:2 rules-from-file)


(map #(let [k
            e #(%1 rules-from-file)]) (keys rules-from-file))



(defn filter-entries [field-key expected-value]
  (loop [k (keys rules-from-file)
         f {}]
    (if (not (empty? (rest k)))
      (if (= (field-key ((first k) rules-from-file)) expected-value)
                                        ;(assoc f (first k) ((first k) rules-from-file))
        (recur (rest k) (assoc f (first k) ((first k) rules-from-file)))
        (recur (rest k) f))
      f)))
