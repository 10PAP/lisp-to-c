import common.Prelude;
import gen.LispLexer;
import gen.LispParser;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        String program = null;
        try {
            program = Files.readString(Paths.get(args[0]), StandardCharsets.US_ASCII);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // добавляем встроенные функции, написанные на лиспе к программе пользователя
        String preparedProg = Prelude.getPrelude() +
                program;

        /*
         * PREPROCESSING STAGE
         */
        LispLexer lexer = new LispLexer(CharStreams.fromString(preparedProg));
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        LispParser parser1 = new LispParser(tokens);
        ParseTree tree = parser1.program();

        String preprocessedProgram = (new Preprocessor()).visit(tree);

        System.out.println(preprocessedProgram);

        /*
         * TRANSLATION STAGE
         */
        LispLexer lexer2 = new LispLexer(CharStreams.fromString(preprocessedProgram));
        CommonTokenStream tokens2 = new CommonTokenStream(lexer2);

        LispParser parser2 = new LispParser(tokens2);
        ParseTree preprocessedTree = parser2.program();

        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new LispWalker(), preprocessedTree);
    }

}