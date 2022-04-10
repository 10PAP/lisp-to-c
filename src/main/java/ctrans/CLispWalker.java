package ctrans;

import gen.LispBaseListener;
import gen.LispParser;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CLispWalker extends LispBaseListener {

    StringBuilder headers = new StringBuilder();
    StringBuilder mainBody = new StringBuilder();
    StringBuilder initializer = new StringBuilder();
    List<StringBuilder> functions = new ArrayList<>();
    CTranslator cTranslator = new CTranslator(headers, initializer, functions);


    @Override
    public void enterProgram(LispParser.ProgramContext ctx) {
        // пройдем по всем form высшего уровня - это вызывы для main.c
        for(var top_level_form : ctx.top_level_from()){
           if(top_level_form.form() != null){
               mainBody.append("\t").append(cTranslator.translateForm(top_level_form.form(), ";\n"));
           }
           else if(top_level_form.fun_definition() != null){
               LispParser.Fun_definitionContext fun_definition = top_level_form.fun_definition();
               String symbol_name = fun_definition.IDENTIFIER().getText();
               String c_function_name = symbol_name + "0";
               cTranslator.updateGlobalScope(c_function_name);
               cTranslator.translateFunctionDefinition(c_function_name, symbol_name, fun_definition.decl().IDENTIFIER(), fun_definition.form());
           }
           else if (top_level_form.include() != null) {
               headers.append("#include ").append(top_level_form.include().HEADER()).append("\n");
           }
        }
    }

    public void exitProgram(LispParser.ProgramContext ctx) {
        try (PrintWriter out = new PrintWriter("out/main.c")) {
            out.println(headers.toString());
            out.println("Value nil;\n");
            functions.forEach(out::println);
            initializer.append("""
                                    \tnil.cell.t = CELL;
                                    \tnil.cell.car = 0;
                                    \tnil.cell.cdr = 0;
                                    """);
            out.println("void init(){\n" + initializer + "}\n");
            out.println("int main() {\n\tinit();\n" + mainBody + "}\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
