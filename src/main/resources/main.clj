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