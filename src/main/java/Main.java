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
                        (defn factorial [n]
                            (if (= n 1)
                                1
                                (* n (factorial (- n 1)))))
                        
                        (defn zerop [x] (= x 0))
                        
                        (defn fibonacci [N]
                            (if (or (zerop N) (= N 1))
                                1
                                (+ (fibonacci (- N 1)) (fibonacci (- N 2)))))
                                
                        (print (list (factorial 12) (fibonacci 20)))
                        """));
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        LispParser parser = new LispParser(tokens);
        ParseTree tree = parser.program();

        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new LispWalker(), tree);
    }
    
}
