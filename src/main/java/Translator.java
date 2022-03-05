import gen.LispParser;

public interface Translator {
    String translateForm(LispParser.FormContext form, String delimeter);
}
