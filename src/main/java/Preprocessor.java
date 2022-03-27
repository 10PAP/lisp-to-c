import gen.LispBaseVisitor;
import gen.LispParser;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Preprocessor extends LispBaseVisitor<String> {

    class Macro {
        String name = null;
        List<String> args = new ArrayList<>();
        LispParser.FormContext backquote_form = null;
    }

    Map<String, Macro> macros = new HashMap<>();

    Map<String, String> macro_ctx = null;

    @Override
    public String visitMacro_definition(LispParser.Macro_definitionContext ctx) {
        Macro macro = new Macro();

        macro.name = ctx.IDENTIFIER().getText();

        macro.backquote_form = ctx.form();
        macro.args = ctx.decl().IDENTIFIER().stream().
                map(ParseTree::getText).collect(Collectors.toList());

        macros.put(macro.name, macro);
        return "";
    }

    @Override
    public String visitProgram(LispParser.ProgramContext ctx) {
        StringBuilder program = new StringBuilder();
        for (var i : ctx.top_level_from()) {
            program.append(visitTop_level_from(i)).append("\n\n");
        }
        return program.toString();
    }

    @Override
    public String visitInclude(LispParser.IncludeContext ctx) {
        return "(" + "include " + ctx.HEADER().getText() + ")";
    }

    @Override
    public String visitFun_definition(LispParser.Fun_definitionContext ctx) {
        return "(defn " + ctx.IDENTIFIER().getText() + " " +
                visitDecl(ctx.decl()) + " " +
                visitForm(ctx.form()) + ")";
    }

    @Override
    public String visitDecl(LispParser.DeclContext ctx) {
        StringBuilder decl = new StringBuilder();
        decl.append("[");
        for (var i : ctx.IDENTIFIER()) {
            decl.append(i.getText()).append(" ");
        }
        return decl.append("]").toString();
    }

    @Override
    public String visitForm(LispParser.FormContext ctx) {

        if (ctx.IDENTIFIER() != null) {
            // we are in substitution, let's substitute arguments
            if (macro_ctx != null) {
                if (macro_ctx.containsKey(ctx.IDENTIFIER().getText())) {
                    return macro_ctx.get(ctx.IDENTIFIER().getText());
                }
            }
            return ctx.IDENTIFIER().getText();
        }
        if (ctx.NUMBER() != null) {
            return ctx.NUMBER().getText();
        }
        if (ctx.STRING() != null) {
            return ctx.STRING().getText();
        }

        if (ctx.simple_form() != null) {
            return visitSimple_form(ctx.simple_form());
        }
        if (ctx.lambda_form() != null) {
            return visitLambda_form(ctx.lambda_form());
        }
        if (ctx.let_form() != null) {
            return visitLet_form(ctx.let_form());
        }

        return "";
    }

    @Override
    public String visitLambda_form(LispParser.Lambda_formContext ctx) {
        return "(fn " + visitDecl(ctx.decl()) + " " + visitForm(ctx.form()) + ")";
    }

    @Override
    public String visitLet_form(LispParser.Let_formContext ctx) {
        return "(let [" + ctx.IDENTIFIER().getText() + " " + visitForm(ctx.form(0)) + "] " + visitForm(ctx.form(1)) + ")";
    }

    @Override
    public String visitSimple_form(LispParser.Simple_formContext ctx) {
        StringBuilder simple = new StringBuilder();

        // if first IDENTIFIER is macro-call then substitute macro
        if (ctx.form(0).IDENTIFIER() != null) {
            String ident = ctx.form(0).IDENTIFIER().getText();
            // here we know that it's macro call
            if (macros.containsKey(ident)) {
                // create arguments substitution map
                macro_ctx = new HashMap<>();
                int arg_n = 0;
                for (var i : ctx.form().stream().skip(1).collect(Collectors.toList())) {
                    macro_ctx.put(macros.get(ident).args.get(arg_n), visitForm(i));
                    arg_n++;
                }
                String form = visitForm(macros.get(ident).backquote_form);
                macro_ctx = null;  // yes, it's not cool...
                return form;
            }
        }

        simple.append("(");
        for (var i : ctx.form()) {
            simple.append(visitForm(i)).append(" ");
        }
        return simple.append(")").toString();
    }
}
