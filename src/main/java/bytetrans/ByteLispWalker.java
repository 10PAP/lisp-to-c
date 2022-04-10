package bytetrans;

import gen.LispBaseListener;
import gen.LispParser;
import javassist.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ByteLispWalker extends LispBaseListener {

    CtClass runtimeClass;
    ClassPool pool = ClassPool.getDefault();

    StringBuilder mainBody = new StringBuilder();
    StringBuilder initializer = new StringBuilder();
    List<StringBuilder> functions = new ArrayList<>();

    ByteTranslator byteTranslator = new ByteTranslator(initializer, functions);

    @Override
    public void enterProgram(LispParser.ProgramContext ctx) throws NotFoundException {

        try {
            pool.insertClassPath(new ClassClassPath(Class.forName("Runtime")));
            pool.insertClassPath(new ClassClassPath(Class.forName("Value")));
            pool.insertClassPath(new ClassClassPath(Class.forName("VInt")));
            pool.insertClassPath(new ClassClassPath(Class.forName("VString")));
            pool.insertClassPath(new ClassClassPath(Class.forName("VBool")));

            CtClass valueClass = pool.get("Value");
            CtClass vintClass = pool.get("VInt");
            CtClass vstringClass = pool.get("VString");
            CtClass vboolClass = pool.get("VBool");
            valueClass.writeFile("out/");
            vintClass.writeFile("out/");
            vstringClass.writeFile("out/");
            vboolClass.writeFile("out/");

            runtimeClass = pool.get("Runtime");
        } catch (NotFoundException | ClassNotFoundException | CannotCompileException | IOException e) {
            e.printStackTrace();
        }

        // пройдем по всем form высшего уровня - это вызывы для main метода
        for(var top_level_form : ctx.top_level_from()){
            if(top_level_form.form() != null){
                mainBody.append("\t").append(byteTranslator.translateForm(top_level_form.form(), ";\n"));
            }
            else if(top_level_form.fun_definition() != null){
                LispParser.Fun_definitionContext fun_definition = top_level_form.fun_definition();
                String symbol_name = fun_definition.IDENTIFIER().getText();
                String function_name = symbol_name + "0";
                byteTranslator.updateGlobalScope(function_name);
                byteTranslator.translateFunctionDefinition(function_name, symbol_name, fun_definition.decl().IDENTIFIER(), fun_definition.form());
            }
        }


    }

    public void exitProgram(LispParser.ProgramContext ctx) {
        // add main method
        String main = "public static void main(String [] args) {\n" + mainBody.toString() + "}";

        try {
            System.out.println(mainBody.toString());
            CtMethod mainM = CtMethod.make(main, runtimeClass);
            runtimeClass.addMethod(mainM);
            System.out.println(runtimeClass.getName());
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }

        try {
            runtimeClass.writeFile("out/");
        } catch (IOException | CannotCompileException e) {
            e.printStackTrace();
        }
    }
}
