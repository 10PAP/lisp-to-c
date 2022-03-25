import common.FunctionIdGenerator;
import common.RuntimeGenerator;
import gen.LispParser;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CTranslator implements Translator {
    private final StringBuilder initializer;
    private final List<StringBuilder> functions;

    private final ArrayList<String> globalScope;

    // arch: try to make it private
    public void updateGlobalScope(String funName) {
        globalScope.add(funName);
        System.out.println("Global function: " + funName);
    }

    public String translateForm(LispParser.FormContext form, String delimiter) {
        StringBuilder out = new StringBuilder();
        if (form.IDENTIFIER() != null) {
            String ident = form.IDENTIFIER().getText();
            if (ident.equals("true")) {
                out.append("MakeBoolean(1)");
            } else if (ident.equals("false")) {
                out.append("MakeBoolean(0)");
            }
            else {
                // TODO: идентификатор из какого скоупа?
                out.append(ident);
            }
        }
        if (form.NUMBER() != null) {
            out.append("MakeInt(").append(form.NUMBER().getText()).append(")");
        }
        if (form.STRING() != null) {
            out.append("MakeString(").append(form.STRING().getText()).append(")");
        }
        if (form.simple_form() != null) {
            // application happens right here...
            LispParser.FormContext firstForm = form.simple_form().form(0);
            List<String> reserved_words = Arrays.asList("+", "-", "*", "/", "mod", "inc", "dec", "print", "read", ">", "<", "=", "or", "and", "not", "list", "if", "cons", "car", "cdr");
            if (firstForm.IDENTIFIER() != null) {
                // special forms
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
                        out.append(form.simple_form().form().size() - 1);
                        if (form.simple_form().form().size() - 1 > 0) {
                            out.append(", ");
                        }
                    }
                    case "if" -> {
                        out.append("(");
                        List<LispParser.FormContext> args = form.simple_form().form();
                        args.remove(0);
                        out.append(translateIf(args)).append(")");
                        return out + delimiter;
                    }
                    case "cons" -> {
                        var conses = form.simple_form().form();
                        out.append("NewCell(");
                    }
                    case "car", "cdr" -> {
                        var cons = form.simple_form().form().get(1);
                        out.append("(*(").append(translateForm(cons, "")).append(".cell.").append(firstForm.IDENTIFIER()).append("))");
                        return out + delimiter;
                    }
                    default -> {
                        // TODO: функция-идентификатор из какого скоупа?
                        if (globalScope.contains(firstForm.IDENTIFIER().getText() + "0")) {
                            out.append(firstForm.IDENTIFIER()).append("0").append("(");
                        } else {
                            List<LispParser.FormContext> args = form.simple_form().form().stream().
                                    skip(1).collect(Collectors.toList());
                            translateApplication(firstForm.IDENTIFIER().getText(), args, out);
                        }
                    }
                }
            }
            else {
                String applicator = translateForm(firstForm, "");
                List<LispParser.FormContext> args = form.simple_form().form().stream().
                        skip(1).collect(Collectors.toList());
                translateApplication(applicator, args, out);
            }
            StringBuilder args = new StringBuilder();
            String prefix = "";
            for(var arg : form.simple_form().form().stream().skip(1).collect(Collectors.toList())){
                args.append(prefix);
                args.append(translateForm(arg, ""));
                prefix = ", ";
            }
            out.append(args);
            out.append(")");
        }
        if (form.lambda_form() != null) {
            // it's like function definition but better
            String symbol_name = "fn" + FunctionIdGenerator.createID();
            translateFunctionDefinition(symbol_name + "0", symbol_name, form.lambda_form().decl().IDENTIFIER(), form.lambda_form().form());
            out.append(symbol_name);
        }
        if (form.let_form() != null){
            String ident = form.let_form().IDENTIFIER().getText();
            var ident_body = form.let_form().form(0);
            var let_body = form.let_form().form(1);
            // добавить имя ident в скоуп, чтобы по нему можно было получить ident_body
            var c_ident_definition = "Value " + ident + ";";
            var c_ident_initial = "\t" + ident + " = " + translateForm(ident_body, "") + ";\n";
            functions.add(new StringBuilder(c_ident_definition));
            initializer.append(c_ident_initial);
            out.append(translateForm(let_body, ""));
        }
        return out + delimiter;
    }

    private void translateApplication(String applicator, List<LispParser.FormContext> args, StringBuilder out) {
        int num_of_args = args.size();
        StringBuilder dirty_hack = new StringBuilder("(Value (*)(");
        String prefix = "";
        for(int i = 0 ; i < num_of_args ; i++){
            dirty_hack.append(prefix).append("Value");
            prefix = ", ";
        }
        dirty_hack.append("))");
        out.append("(").append(dirty_hack).append("(").append(applicator).append(").clo.lam)").append("(");
    }

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

    private String translateIf(List<LispParser.FormContext> args) {
        String out = "";
        out += "(" + translateForm(args.get(0), "") + ").b.value == 1" + " ? " +
                translateForm(args.get(1), "") + " : " +
                translateForm(args.get(2), "");
        return out;
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
