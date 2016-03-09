;Sebasian Appelberg och Dat Trieu

(import java.io.Closeable)
(import java.io.FileReader)
(import java.io.File)

(defmacro safe 
	"try-catch or try-with-catch"
  ([expression] 
  `(try ~expression
	(catch ~Exception e# 
	  (.toString e#)))) ;.printStackTrace för en längre print
  ([[variable val] expression] 
  `(let [~variable ~val]
    (try ~expression
      (catch ~Exception e# 
	    (.toString e#)) ;.printStackTrace för en längre print
      (finally  (if (instance? Closeable ~variable)
	    (.close ~variable)))))))

;test för safe.

;(safe (/ 1 0))
;(safe (/ 10 2))
;(safe [s (FileReader. (File. "file.txt"))] (.read s))
;(safe [s (FileReader. (File. "missing-file"))] (. s read))

; Används som exempel i reflektionen.

; (defn safefn [& vec]
	; (if (= (count vec) 1)
		; (try (eval (read-string (nth vec 0)))
			; (catch Exception e# (.getMessage e#)
			; )
		; )
		; (try (eval (read-string (clojure.string/join (conj (into ["(def "] (nth vec 0)) ")")))) 
		; (eval (read-string (nth vec 1)))
			; (catch Exception e# (.getMessage e#)
			; )
			; (finally (if (instance? Closeable (eval (read-string (nth (nth vec 0) 0)))) 
			 ; (.close (eval (read-string (nth (nth vec 0) 0)))))))))

;(safefn ["s" " (FileReader. (File. \"file.txt\"))"] "(. s read)")

; mindre läsbar version som bättre överstämmer med python exemplet

; (defmacro safe 
	; "try-catch or try-with-catch"
  ; ([& args] (if (= (count args) 1)
  ; `(try ~(nth args 0)
	; (catch ~Exception e# (.printStackTrace e#))))
  ; `(let [~(nth (nth args 0) 0) ~(nth (nth args 0) 1)]
    ; (try ~(nth args 1)
      ; (catch ~Exception e# (.printStackTrace e#))
      ; (finally  (if (instance? Closeable ~(nth (nth args 0) 0))
        ; (.close ~(nth (nth args 0) 0))))))))
		
;SQL-like Macro
(defmacro select
  [var _ coll _ wherearg _ orderarg]
  `(map 
     (fn [x#] 
       (select-keys x# (reverse [~@var])))
        (filter 
          (fn [x#] 
            (~(second wherearg) (get x# ~(first wherearg)) ~(last wherearg)))
                (sort-by 
                  (fn[x#]
                     (get x# ~orderarg)) 
                  ~coll))))

(defn <> 
  [op1 op2]
  (not= op1 op2))

;Test för select
(def persons '({:id 1 :name "olle"} {:id 2 :name "anna"} {:id 3 :name "isak"} {:id 4 :name "beatrice"}))

(select [:name :id] from persons 
        where  (:name <> "olle") 
        orderby :name)
(select [:name :id] from persons 
        where  (:name = "anna") 
        orderby :id)
(select [:id :name] from persons 
        where  (:id > 2) 
        orderby :name)
(select [:id :name] from persons 
        where  (:id < 3) 
        orderby :id)
