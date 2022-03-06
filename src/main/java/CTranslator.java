import common.RuntimeGenerator;
import gen.LispParser;

public class CTranslator implements Translator {

    public String translateForm(LispParser.FormContext form, String delimeter) {
        StringBuilder out = new StringBuilder();
        if(form.IDENTIFIER() != null){
            return form.IDENTIFIER().getText();
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
                default -> out.append(form.simple_form().IDENTIFIER()).append("(");
            }

            var arg_iter = form.simple_form().form().iterator();
            while(arg_iter.hasNext()) {
                out.append(translateForm(arg_iter.next(), ""));
                if(arg_iter.hasNext())
                    out.append(", ");
            }
            out.append(")");
        }
        return out + delimeter;
    }

    CTranslator(StringBuilder headers) {
        headers.append("#include \"runtime.h\"\n");

        RuntimeGenerator.generateHeader();
        RuntimeGenerator.generateCFile();
    }

}
