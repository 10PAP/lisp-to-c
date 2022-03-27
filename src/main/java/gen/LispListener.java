// Generated from /home/charlie/GitHub/lisp-to-c/src/main/antlr4/Lisp.g4 by ANTLR 4.9.2
package gen;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LispParser}.
 */
public interface LispListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LispParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(LispParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(LispParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link LispParser#top_level_from}.
	 * @param ctx the parse tree
	 */
	void enterTop_level_from(LispParser.Top_level_fromContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispParser#top_level_from}.
	 * @param ctx the parse tree
	 */
	void exitTop_level_from(LispParser.Top_level_fromContext ctx);
	/**
	 * Enter a parse tree produced by {@link LispParser#include}.
	 * @param ctx the parse tree
	 */
	void enterInclude(LispParser.IncludeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispParser#include}.
	 * @param ctx the parse tree
	 */
	void exitInclude(LispParser.IncludeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LispParser#fun_definition}.
	 * @param ctx the parse tree
	 */
	void enterFun_definition(LispParser.Fun_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispParser#fun_definition}.
	 * @param ctx the parse tree
	 */
	void exitFun_definition(LispParser.Fun_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LispParser#macro_definition}.
	 * @param ctx the parse tree
	 */
	void enterMacro_definition(LispParser.Macro_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispParser#macro_definition}.
	 * @param ctx the parse tree
	 */
	void exitMacro_definition(LispParser.Macro_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LispParser#form}.
	 * @param ctx the parse tree
	 */
	void enterForm(LispParser.FormContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispParser#form}.
	 * @param ctx the parse tree
	 */
	void exitForm(LispParser.FormContext ctx);
	/**
	 * Enter a parse tree produced by {@link LispParser#lambda_form}.
	 * @param ctx the parse tree
	 */
	void enterLambda_form(LispParser.Lambda_formContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispParser#lambda_form}.
	 * @param ctx the parse tree
	 */
	void exitLambda_form(LispParser.Lambda_formContext ctx);
	/**
	 * Enter a parse tree produced by {@link LispParser#let_form}.
	 * @param ctx the parse tree
	 */
	void enterLet_form(LispParser.Let_formContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispParser#let_form}.
	 * @param ctx the parse tree
	 */
	void exitLet_form(LispParser.Let_formContext ctx);
	/**
	 * Enter a parse tree produced by {@link LispParser#simple_form}.
	 * @param ctx the parse tree
	 */
	void enterSimple_form(LispParser.Simple_formContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispParser#simple_form}.
	 * @param ctx the parse tree
	 */
	void exitSimple_form(LispParser.Simple_formContext ctx);
	/**
	 * Enter a parse tree produced by {@link LispParser#decl}.
	 * @param ctx the parse tree
	 */
	void enterDecl(LispParser.DeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispParser#decl}.
	 * @param ctx the parse tree
	 */
	void exitDecl(LispParser.DeclContext ctx);
}