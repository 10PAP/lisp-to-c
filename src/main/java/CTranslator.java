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
            out.append(form.simple_form().IDENTIFIER()).append("(");
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
}
