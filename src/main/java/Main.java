import gen.LispLexer;
import gen.LispParser;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Main {

    public static void main(String[] args) {

        LispLexer lexer = new LispLexer(CharStreams.fromString(
                        """
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
                                        
                                (print (list ((proxy factorial) 2) (((fn [x] x) factorial) 4) (factorial (proxy 1)) (factorial 5) (fibonacci 3)))
                                (print (car (cons nil nil)))
                                """));
        /*
        (defn filter [xs pred]
        (if (= nil xs)
        xs
                (if (pred (car xs))
            (cons (car xs) (filter (cdr xs) pred))
        (filter (cdr xs) pred)
                                        )
                                    )
                                )
         */
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        LispParser parser = new LispParser(tokens);
        ParseTree tree = parser.program();

        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new LispWalker(), tree);
    }
    
}
