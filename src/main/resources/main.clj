(defn fac_helper [n acc]
  (if (= 1 n)
    acc
    (fac_helper (- n 1) (* acc n))))

(defn factorial [n]
  (fac_helper n 1))

(defn proxy [f] f)
(defn nul [] 2)

(defn fibonacci [N]
  (if (or (= N 0) (= N 1))
    1
    (+ (fibonacci (- N 1)) (fibonacci (- N 2)))))

(print (+ (proxy (nul)) 2))

(print
  (map
   (fn [n] (+ n 10))
   (cons 1 (cons 15 (cons 8 (cons 10 (cons 3 nil)))))))

(print
  (filter
   (fn [n] (> n 2))
   (cons 1 (cons 15 (cons 8 (cons 10 (cons 3 nil)))))))

(print
  (fold
   (fn [x acc] (+ x acc))
   0
   (cons 1 (cons 15 (cons 8 (cons 10 (cons 3 nil)))))))

(print
    (let [funny_func (fn [n] (+ n 10))]
      (let [ugly_arg 10] (funny_func ugly_arg))))

(let [testList1 (cons 1 (cons 15 (cons 8 (cons 10 (cons 3 nil)))))]
  (print (snd testList1)))

(print
  (max (cons 2 (cons 149 (cons 999 (cons 54 (cons 11 nil)))))))

    (let [funny_func (fn [n] (+ n 17))]
    (let [ugly_arg 10] (funny_func ugly_arg))))