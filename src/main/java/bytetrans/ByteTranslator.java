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
    private final ArrayList<String> globalScope;

    ByteTranslator(StringBuilder initializer, List<StringBuilder> functions) {
        this.initializer = initializer;
        this.functions = functions;
        globalScope = new ArrayList<>();
    }

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

    }

    @Override
    public void translateLambdaForm(List<TerminalNode> arg_names, LispParser.FormContext lambda_body, StringBuilder out) {

    }

    @Override
    public void translateApplication(String applicator, List<LispParser.FormContext> args, StringBuilder out) {

    }

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
            case "read" -> out.append("lisp_read").append("(");
            case "if" -> {
                out.append("(");
                String if_body = translateForm(args.get(0), "") + ").getValue().booleanValue()" + " ? " +
                        translateForm(args.get(1), "") + " : " +
                        translateForm(args.get(2), "");
                out.append(if_body);
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
    public void translateFunctionDefinition(String c_function_name, String symbol_name, List<TerminalNode> arg_names, LispParser.FormContext fun_body) {

    }
}
