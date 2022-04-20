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

    public static class Var {
        String name;
        String value;
    }
    List<Var> globalScope = new ArrayList<>();

    ByteTranslator byteTranslator = new ByteTranslator(initializer, functions, globalScope);

    @Override
    public void enterProgram(LispParser.ProgramContext ctx) {

        runtimeClass = byteTranslator.InitClassPool(pool);

        // пройдем по всем form высшего уровня - это вызывы для main метода
        for(var top_level_form : ctx.top_level_from()){
            if(top_level_form.form() != null){
                mainBody.append("\t").append(byteTranslator.translateForm(top_level_form.form(), ";\n"));
            }
            else if(top_level_form.fun_definition() != null){
                LispParser.Fun_definitionContext fun_definition = top_level_form.fun_definition();
                String symbol_name = fun_definition.IDENTIFIER().getText();
                String function_name = symbol_name + "0";
                //byteTranslator.updateGlobalScope(function_name);
                byteTranslator.translateFunctionDefinition(function_name, symbol_name, fun_definition.decl().IDENTIFIER(), fun_definition.form());
            }
        }


    }

    public void exitProgram(LispParser.ProgramContext ctx) {
        // add main method
        String main = "public static void main(String [] args) {\n" +
                "initializer();\n" +
                mainBody.toString() + "}";
        initializer.append("}");

        try {
            System.out.println(globalScope.size());
            for (var i : globalScope) {
                String fieldStr = "private static Value " + i.name + ";";
                System.out.println(fieldStr);
                CtField ctField = CtField.make(fieldStr, runtimeClass);
                runtimeClass.addField(ctField);
            }

            CtMethod init = CtMethod.make(initializer.toString(), runtimeClass);
            runtimeClass.addMethod(init);

            // add global functions into the classfile
            for (var f : functions) {
                System.out.println(f.toString());
                CtMethod funcM = CtMethod.make(f.toString(), runtimeClass);
                runtimeClass.addMethod(funcM);
            }

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
