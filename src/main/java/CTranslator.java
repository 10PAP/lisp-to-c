import common.RuntimeGenerator;
import gen.LispParser;

import java.util.List;

public class CTranslator implements Translator {

    public String translateForm(LispParser.FormContext form, String delimeter) {
        StringBuilder out = new StringBuilder();
        if(form.IDENTIFIER() != null){
            String ident = form.IDENTIFIER().getText();
            if (ident.equals("true")) {
                return "MakeBoolean(1)";
            } else if (ident.equals("false")) {
                return "MakeBoolean(0)";
            }
            return ident;
        }
        if(form.NUMBER() != null){
            return "MakeInt(" + form.NUMBER().getText() + ")";
        }
        if(form.STRING() != null){
            return "MakeString(" + form.STRING().getText() + ")";
        }
        if(form.simple_form() != null){

            // special forms
            switch (form.simple_form().IDENTIFIER().getText()) {
                case "+" -> out.append("lisp_add").append("(");
                case "-" -> out.append("lisp_sub").append("(");
                case "*" -> out.append("lisp_mul").append("(");
                case "/" -> out.append("lisp_div").append("(");
                case "mod" -> out.append("lisp_mod").append("(");
                case "inc" -> out.append("lisp_inc").append("(");
                case "dec" -> out.append("lisp_dec").append("(");
                case "print" -> out.append("lisp_print").append("(");
                case ">" -> out.append("lisp_gt").append("(");
                case "<" -> out.append("lisp_lt").append("(");
                case "=" -> out.append("lisp_eq").append("(");
                case "list" -> out.append("lisp_list").append("(");
                case "if" ->  {
                    out.append("(");
                    List<LispParser.FormContext> args = form.simple_form().form();
                    out.append(translateIf(args)).append(")");
                    return out + delimeter;
                }
                default -> out.append(form.simple_form().IDENTIFIER()).append("0").append("("); // подразумевается, что сейчас есть только глобальный scope!!!
            }
            StringBuilder args = new StringBuilder();
            var arg_iter = form.simple_form().form().iterator();
            while(arg_iter.hasNext()) {
                args.append(translateForm(arg_iter.next(), ""));
                if(arg_iter.hasNext())
                    args.append(", ");
            }
            if(form.simple_form().IDENTIFIER().getText().equals("list")) {
                out.append(form.simple_form().form().size());
                if(form.simple_form().form().size() > 0){
                    out.append(", ");
                }
            }
            out.append(args);
            out.append(")");
        }
        return out + delimeter;
    }

    private String translateIf(List<LispParser.FormContext> args) {
        String out = "";
        out += "(" + translateForm(args.get(0), "") + ").b.value == 1" + " ? " +
                translateForm(args.get(1), "") + " : " +
                translateForm(args.get(2), "");
        return out;
    }

    CTranslator(StringBuilder headers) {
        headers.append("#include \"runtime.h\"\n");

        RuntimeGenerator.generateHeader();
        RuntimeGenerator.generateCFile();
        RuntimeGenerator.generateMakefile();
    }

}
