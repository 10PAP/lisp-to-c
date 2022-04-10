(print "Test1")

(print (+ (- 4 3) (* 10 (/ 4 2))))
(print (if false (inc 10) (dec 10)))

(print "Test2")

(if (= 10 (readInt)) (print "ThisIsTrue") (print "ThisIsFalse"))

(print "Test3")

(defn fac_helper [n acc]
  (if (= 0 n)
    acc
    (fac_helper (- n 1) (* acc n))))

(defn factorial [n]
  (fac_helper n 1))

(defn fibonacci [N]
  (if (or (= N 0) (= N 1))
    1
    (+ (fibonacci (- N 1)) (fibonacci (- N 2)))))

(print (fibonacci 10))
(print (factorial 10))

(print "Test4")

(defn BoolToString [pred]
  (let [t "True"]
    (let [f "False"]
      (if pred t f))))

(print (BoolToString true))
(print (BoolToString false))

(print "Test5")

(print (let [newVal 20] (fibonacci newVal)))