import gen.LispBaseListener;
import gen.LispParser;
import common.FunctionIdGenerator;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class LispWalker extends LispBaseListener {

    StringBuilder headers = new StringBuilder();
    StringBuilder mainBody = new StringBuilder();
    StringBuilder initializer = new StringBuilder();
    List<StringBuilder> functions = new ArrayList<>();
    CTranslator cTranslator = new CTranslator(headers);

    @Override
    public void enterProgram(LispParser.ProgramContext ctx) {
        // static Value makeInt()

        // пройдем по всем form высшего уровня - это вызывы для main.c
        for(var top_level_form : ctx.top_level_from()){
           if(top_level_form.form() != null){
               mainBody.append("\t").append(cTranslator.translateForm(top_level_form.form(), ";\n"));
           }
           else if(top_level_form.fun_definition() != null){
               LispParser.Fun_definitionContext fun_definition = top_level_form.fun_definition();
               StringBuilder c_function_definition = new StringBuilder();
               String c_function_name = fun_definition.IDENTIFIER().getText() + FunctionIdGenerator.createID();
               // generate a new function itself
               c_function_definition.append("Value ").append(c_function_name).append("(");
               String prefix = "";
               for(var arg : fun_definition.decl().IDENTIFIER()) {
                   c_function_definition.append(prefix);
                   c_function_definition.append("Value ").append(arg.getText());
                   prefix = ", ";
               }
               c_function_definition.append(") {\n" + "\treturn ").append(cTranslator.translateForm(fun_definition.form(), ";\n}\n"));
               // generate Value-typed variable with pointer to new function inside
               c_function_definition.append("Value ").append(fun_definition.IDENTIFIER().getText()).append(";\n");
               initializer.append("\t").append(fun_definition.IDENTIFIER().getText()).append(" = MakePrimitive(&").append(c_function_name).append(");\n");
               functions.add(c_function_definition);
           }
           else if (top_level_form.include() != null) {
                headers.append("#include ").append(top_level_form.include().HEADER()).append("\n");
           }
        }
    }
    public void exitProgram(LispParser.ProgramContext ctx) {
        try (PrintWriter out = new PrintWriter("out/main.c")) {
            out.println(headers.toString());
            functions.forEach(out::println);
            out.println("void init(){\n" + initializer + "}\n");
            out.println("int main() {\n\tinit();\n" + mainBody + "}\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enterTop_level_from(LispParser.Top_level_fromContext ctx) {
    }

    @Override
    public void enterFun_definition(LispParser.Fun_definitionContext ctx) {
    }

    @Override
    public void enterForm(LispParser.FormContext ctx){
    }

    @Override
    public void enterSimple_form(LispParser.Simple_formContext ctx) {
    }
}
