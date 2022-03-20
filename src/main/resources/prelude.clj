(defn filter [pred xs]
    (if (= nil xs)
        nil
        (if (pred (car xs))
            (cons (car xs) (filter pred (cdr xs)))
            (filter pred (cdr xs)))))

(defn map [foo xs]
    (if (= nil xs)
        nil
        (cons (foo (car xs)) (map foo (cdr xs)))))

(defn fold [bin acc xs]
    (if (= nil xs)
        acc
        (fold bin (bin (car xs) acc) (cdr xs))))