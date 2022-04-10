package bytetrans;

import gen.LispParser;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ByteTranslator implements common.Translator {

    private final StringBuilder initializer;
    private final List<StringBuilder> functions;
    private final List<ByteLispWalker.Var> globalScope;

    ByteTranslator(StringBuilder initializer, List<StringBuilder> functions, List<ByteLispWalker.Var>globalScope) {
        this.initializer = initializer;
        this.functions = functions;
        this.globalScope = globalScope;
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
        ByteLispWalker.Var var = new ByteLispWalker.Var();
        var.name = ident;
        var.value = translateForm(ident_body, "");
        globalScope.add(var);
        String ident_init = "\t" + ident + " = " + translateForm(ident_body, "") + ";\n";
        initializer.append(ident_init);
        out.append(translateForm(let_body, ""));
    }

    @Override
    public void translateLambdaForm(List<TerminalNode> arg_names, LispParser.FormContext lambda_body, StringBuilder out) {}

    @Override
    public void translateApplication(String applicator, List<LispParser.FormContext> args, StringBuilder out) {}

    @Override
    public String translateSimpleForm(LispParser.FormContext firstForm, List<LispParser.FormContext> args, String delimiter, StringBuilder out) {
        List<String> reserved_words = Arrays.asList("+", "-", "*", "/", "mod", "inc", "dec", "print", "read", ">", "<", "=", "or", "and", "not", "list", "if", "cons", "car", "cdr");
        if (firstForm.IDENTIFIER() != null) {
            String out1 = translateSpecialForm(firstForm, args, delimiter, out);
            if (out1 != null) return out1;
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
            case "print" -> out.append("lisp_print").append("(");
            case "readInt" -> out.append("lisp_readInt").append("(");
            case "readBool" -> out.append("lisp_readBool").append("(");
            case "readString" -> out.append("lisp_readString").append("(");
            case "=" -> out.append("lisp_eq").append("(");
            case "or" -> out.append("lisp_or").append("(");
            case "and" -> out.append("lisp_and").append("(");
            case "not" -> out.append("lisp_not").append("(");
            case "if" -> {
                out.append("((VBool) ");
                String if_body = translateForm(args.get(0), "") + ").getValue().booleanValue()" + " ? " +
                        translateForm(args.get(1), "") + " : " +
                        translateForm(args.get(2), "");
                out.append(if_body);
                return out + delimiter;
            }
            default -> {
                out.append(firstForm.IDENTIFIER()).append("0").append("(");
            }
        }
        return null;
    }

    @Override
    public void translateString(String lisp_string, StringBuilder out) {
        out.append("new VString(").append(lisp_string).append(")");
    }

    @Override
    public void translateNumber(String lisp_number, StringBuilder out) {
        out.append("new VInt(").append(lisp_number).append(")");
    }

    @Override
    public void translateIdentifier(String lisp_ident, StringBuilder out) {
        // TODO boolean
        if (lisp_ident.equals("true")) {
            out.append("new VBool(Boolean.TRUE)");
        } else if (lisp_ident.equals("false")) {
            out.append("new VBool(Boolean.FALSE)");
        }
        else {
            out.append(lisp_ident);
        }
    }

    @Override
    public void translateFunctionDefinition(String funName, String symbol_name, List<TerminalNode> arg_names, LispParser.FormContext fun_body) {
        StringBuilder funDefinition = new StringBuilder();
        // generate a new function itself
        funDefinition.append("private static Value ").append(funName).append("(");
        String prefix = "";
        for(var arg : arg_names) {
            funDefinition.append(prefix);
            funDefinition.append("Value ").append(arg.getText());
            prefix = ", ";
        }
        funDefinition.append(") {\n" + "\treturn ").append(this.translateForm(fun_body, ";\n}\n"));
        functions.add(funDefinition);
    }
}
