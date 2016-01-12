(ns rule-sorter.core
  (:gen-class))

(def language-mods (hash-set "cz" "eng"))
(def language-select (ref :cz))


(def questions {:name {:cz "Jaké je tvoje křestní jméno?"
                       :eng "What is your name"}
                :surename {:cz "Jaké je tvoje příjmenní?"
                           :eng "What is your surename?"}
                :age {:cz "Kolik ti je let?"
                      :age "How old are you"}
                :sex {:cz "K jakému pohlaví sám sebe řadíš?"
                      :eng "What is te sex that suits you the best"}
                :occupation {:cz "Jaké je tvé povolání?"
                             :eng "What is your occupation?"}
                :rule {:cz "Napiš své pravidlo pro kolektivní hru:"
                       :eng "Write the rule for collective game:"}})



(def rules (ref {:entry {:name "Jirka"
                         :surename "Rouš"
                         :age 29
                         :sex :male
                         :occupation :studnet
                         :rule "Každý z návštěvníků výstavy musí jednou za deset minut po dobu své návštěvy v galerii odejít z místnosti kde právě přebývá do místonsti jiné a tam počkat na do dalšího znamení pro přesun, místnost si může vybrat libovolnou, krom té ve které se v době změny nachází."
                         }}))


(defn what-is-your-name []
  (let [l @language-select]
    (println (l (:name questions)))
    (if-let [v (read-line)]
      v)))

(defn change-language []
  (let [l @language-select]
    (cond (= l :cz) (do (println "K dispozici jsou tyto jazykové mutace:"))
          (= l :eng) (do (println "There are following langugae mutaions:")))
    (println language-mods)
    (cond (= l :cz) (do (println "Prosím, zvolte jednu z nabízených možností."))
          (= l :eng) (do (println "Please choose one of the offered options.")))
    (let [new-l (keyword (read-line))
          t (contains? language-mods new-l)]
      (println t)
      (if t
        (dosync (ref-set language-select new-l)
                (cond (= new-l :cz) (println "Jazyk byl změněn.")
                      (= new-l :eng) (println "The language was be changed.")))
        (cond (= l :cz) (println "Vaše volba není validní. Prosím zkuste to znovu.")
              (= l :eng) (println "Your choise is not walid. Please try again."))))))

(keyword :cz)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(println @language-select)
