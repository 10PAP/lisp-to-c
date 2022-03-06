import gen.LispBaseListener;
import gen.LispParser;
import common.FunctionIdGenerator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class LispWalker extends LispBaseListener {

    StringBuilder headers = new StringBuilder();
    StringBuilder mainBody = new StringBuilder();
    List<StringBuilder> functions = new ArrayList<>();
    CTranslator cTranslator = new CTranslator(headers);

    @Override
    public void enterProgram(LispParser.ProgramContext ctx) {
        // TODODODOODODODODODO:
        // static Value makeInt()

        // пройдем по всем form высшего уровня - это вызывы для main.c
        for(var top_level_form : ctx.top_level_from()){
           if(top_level_form.form() != null){
               mainBody.append("\t").append(cTranslator.translateForm(top_level_form.form(), ";\n"));
           }
           else if(top_level_form.fun_definition() != null){
               var function = new StringBuilder();
               function.append("Value ").append(top_level_form.fun_definition().IDENTIFIER()).append(FunctionIdGenerator.createID()).append("(");
               var arg_iter = top_level_form.fun_definition().decl().IDENTIFIER().iterator();
               while(arg_iter.hasNext()) {
                   function.append("Value ").append(arg_iter.next().getText());
                   if(arg_iter.hasNext())
                       function.append(", ");
               }
               function.append(") {\n" + "\treturn ").append(cTranslator.translateForm(top_level_form.fun_definition().form(), ";\n}\n\n"));
               functions.add(function);
           }
           else if (top_level_form.include() != null) {

           }
        }
    }
    public void exitProgram(LispParser.ProgramContext ctx) {
        try (PrintWriter out = new PrintWriter("out/main.c")) {
            out.println(headers.toString());
            functions.forEach(out::println);
            out.println("int main() {");
            out.println(mainBody);
            out.println("}\n");
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
