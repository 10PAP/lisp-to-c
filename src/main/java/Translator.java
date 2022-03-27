import gen.LispParser;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

public interface Translator {
    String translateForm(LispParser.FormContext form, String delimiter);

    void translateLetForm(String ident, LispParser.FormContext ident_body, LispParser.FormContext let_body, StringBuilder out);

    void translateLambdaForm(List<TerminalNode> arg_names, LispParser.FormContext lambda_body, StringBuilder out);

    void translateApplication(String applicator, List<LispParser.FormContext> args, StringBuilder out);

    String translateSimpleForm(LispParser.FormContext firstForm, List<LispParser.FormContext> args, String delimiter, StringBuilder out);

    String translateSpecialForm(LispParser.FormContext firstForm, List<LispParser.FormContext> args, String delimiter, StringBuilder out);

    void translateString(String lisp_string, StringBuilder out);

    void translateNumber(String lisp_number, StringBuilder out);

    void translateIdentifier(String lisp_ident, StringBuilder out);

    void translateFunctionDefinition(String c_function_name, String symbol_name, List<TerminalNode> arg_names, LispParser.FormContext fun_body);
}
