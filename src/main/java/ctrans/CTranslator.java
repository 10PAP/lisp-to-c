package ctrans;

import common.FunctionIdGenerator;
import common.RuntimeGenerator;
import gen.LispParser;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import common.Translator;

public class CTranslator implements Translator {
    private final StringBuilder initializer;
    private final List<StringBuilder> functions;

    private final ArrayList<String> globalScope;

    // arch: try to make it private
    public void updateGlobalScope(String funName) {
        globalScope.add(funName);
        System.out.println("Global function: " + funName);
    }

    @Override
    public String translateForm(LispParser.FormContext form, String delimiter) {
        StringBuilder out = new StringBuilder();
        if (form.IDENTIFIER() != null) {
            translateIdentifier(
                    form.IDENTIFIER().getText(),
                    out);
        }
        if (form.NUMBER() != null) {
            translateNumber(
                    form.NUMBER().getText(),
                    out);
        }
        if (form.STRING() != null) {
            translateString(
                    form.STRING().getText(),
                    out);
        }
        if (form.simple_form() != null) {
            // application happens right here...
            String out1 = translateSimpleForm(
                    form.simple_form().form(0),
                    form.simple_form().form().stream().skip(1).collect(Collectors.toList()),
                    delimiter,
                    out);
            if (out1 != null) return out1;
        }
        if (form.lambda_form() != null) {
            // it's like function definition but better
            translateLambdaForm(
                    form.lambda_form().decl().IDENTIFIER(),
                    form.lambda_form().form(),
                    out);
        }
        if (form.let_form() != null){
            translateLetForm(form.let_form().IDENTIFIER().getText(),
                             form.let_form().form(0),
                             form.let_form().form(1),
                             out);
        }
        return out + delimiter;
    }

    @Override
    public void translateLetForm(String ident, LispParser.FormContext ident_body, LispParser.FormContext let_body, StringBuilder out) {
        // добавить имя ident в скоуп, чтобы по нему можно было получить ident_body
        String c_ident_definition = "Value " + ident + ";";
        String c_ident_initial = "\t" + ident + " = " + translateForm(ident_body, "") + ";\n";
        functions.add(new StringBuilder(c_ident_definition));
        initializer.append(c_ident_initial);
        out.append(translateForm(let_body, ""));
    }

    @Override
    public void translateLambdaForm(List<TerminalNode> arg_names, LispParser.FormContext lambda_body, StringBuilder out) {
        String symbol_name = "fn" + FunctionIdGenerator.createID();
        translateFunctionDefinition(symbol_name + "0", symbol_name, arg_names, lambda_body);
        out.append(symbol_name);
    }

    @Override
    public void translateApplication(String applicator, List<LispParser.FormContext> args, StringBuilder out) {
        int num_of_args = args.size();
        StringBuilder dirty_hack = new StringBuilder("(Value (*)(");
        String prefix = "";
        for (int i = 0; i < num_of_args; i++) {
            dirty_hack.append(prefix).append("Value");
            prefix = ", ";
        }
        dirty_hack.append("))");
        out.append("(").append(dirty_hack).append("(").append(applicator).append(").clo.lam)").append("(");
    }

    @Override
    public String translateSimpleForm(LispParser.FormContext firstForm, List<LispParser.FormContext> args, String delimiter, StringBuilder out) {
        List<String> reserved_words = Arrays.asList("+", "-", "*", "/", "mod", "inc", "dec", "print", "read", ">", "<", "=", "or", "and", "not", "list", "if", "cons", "car", "cdr");
        if (firstForm.IDENTIFIER() != null) {
            String out1 = translateSpecialForm(firstForm, args, delimiter, out);
            if (out1 != null) return out1;
        }
        else {
            String applicator = translateForm(firstForm, "");
            translateApplication(applicator, args, out);
        }
        StringBuilder c_args = new StringBuilder();
        String prefix = "";
        for(var arg : args){
            c_args.append(prefix);
            c_args.append(translateForm(arg, ""));
            prefix = ", ";
        }
        out.append(c_args);
        out.append(")");
        return null;
    }

    @Override
    public String translateSpecialForm(LispParser.FormContext firstForm, List<LispParser.FormContext> args, String delimiter, StringBuilder out) {
        switch (firstForm.IDENTIFIER().getText()) {
            case "+" -> out.append("lisp_add").append("(");
            case "-" -> out.append("lisp_sub").append("(");
            case "*" -> out.append("lisp_mul").append("(");
            case "/" -> out.append("lisp_div").append("(");
            case "mod" -> out.append("lisp_mod").append("(");
            case "inc" -> out.append("lisp_inc").append("(");
            case "dec" -> out.append("lisp_dec").append("(");
            case "print" -> out.append("lisp_print_0").append("(");
            case "read" -> out.append("lisp_read").append("(");
            case ">" -> out.append("lisp_gt").append("(");
            case "<" -> out.append("lisp_lt").append("(");
            case "=" -> out.append("lisp_eq").append("(");
            case "or" -> out.append("lisp_or").append("(");
            case "and" -> out.append("lisp_and").append("(");
            case "not" -> out.append("lisp_not").append("(");
            case "list" -> {
                out.append("lisp_list").append("(");
                out.append(args.size());
                if (args.size() > 0) {
                    out.append(", ");
                }
            }
            case "if" -> {
                out.append("(");
                String if_body = "(" + translateForm(args.get(0), "") + ").b.value == 1" + " ? " +
                        translateForm(args.get(1), "") + " : " +
                        translateForm(args.get(2), "");
                out.append(if_body).append(")");
                return out + delimiter;
            }
            case "cons" -> {
                out.append("NewCell(");
            }
            case "car", "cdr" -> {
                var cons_body = args.get(0);
                out.append("(*(").append(translateForm(cons_body, "")).append(".cell.").append(firstForm.IDENTIFIER()).append("))");
                return out + delimiter;
            }
            default -> {
                if (globalScope.contains(firstForm.IDENTIFIER().getText() + "0")) {
                    out.append(firstForm.IDENTIFIER()).append("0").append("(");
                } else {
                    translateApplication(firstForm.IDENTIFIER().getText(), args, out);
                }
            }
        }
        return null;
    }

    @Override
    public void translateString(String lisp_string, StringBuilder out) {
        out.append("MakeString(").append(lisp_string).append(")");
    }

    @Override
    public void translateNumber(String lisp_number, StringBuilder out) {
        out.append("MakeInt(").append(lisp_number).append(")");
    }

    @Override
    public void translateIdentifier(String lisp_ident, StringBuilder out) {
        if (lisp_ident.equals("true")) {
            out.append("MakeBoolean(1)");
        } else if (lisp_ident.equals("false")) {
            out.append("MakeBoolean(0)");
        }
        else {
            out.append(lisp_ident);
        }
    }

    @Override
    public void translateFunctionDefinition(String c_function_name, String symbol_name, List<TerminalNode> arg_names, LispParser.FormContext fun_body) {
        StringBuilder c_function_definition = new StringBuilder();
        // generate a new function itself
        c_function_definition.append("Value ").append(c_function_name).append("(");
        String prefix = "";
        for(var arg : arg_names) {
            c_function_definition.append(prefix);
            c_function_definition.append("Value ").append(arg.getText());
            prefix = ", ";
        }
        c_function_definition.append(") {\n" + "\treturn ").append(this.translateForm(fun_body, ";\n}\n"));
        // generate Value-typed variable with pointer to new function inside
        c_function_definition.append("Value ").append(symbol_name).append(";\n");
        initializer.append("\t").append(symbol_name).append(" = MakePrimitive(&").append(c_function_name).append(");\n");
        functions.add(c_function_definition);
    }

    CTranslator(StringBuilder headers, StringBuilder initializer, List<StringBuilder> functions) {
        headers.append("#include \"runtime.h\"\n");
        this.initializer = initializer;
        this.functions = functions;
        globalScope = new ArrayList<>();

        RuntimeGenerator.generateHeader();
        RuntimeGenerator.generateCFile();
        RuntimeGenerator.generateMakefile();
    }

}
