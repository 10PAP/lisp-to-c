import gen.LispLexer;
import gen.LispParser;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Main {

    public static void main(String[] args) {

        LispLexer lexer = new LispLexer(CharStreams.fromString("" +
                "(defn bebra [x y] (plus x y))" +
                "(bebra (bebra \"4\") 3)"));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LispParser parser = new LispParser(tokens);
        ParseTree tree = parser.program();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new LispWalker(), tree);
    }
    
}
