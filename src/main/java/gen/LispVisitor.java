// Generated from /home/charlie/GitHub/lisp-to-c/src/main/antlr4/Lisp.g4 by ANTLR 4.9.2
package gen;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LispParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LispVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link LispParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(LispParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link LispParser#top_level_from}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTop_level_from(LispParser.Top_level_fromContext ctx);
	/**
	 * Visit a parse tree produced by {@link LispParser#include}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInclude(LispParser.IncludeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LispParser#fun_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFun_definition(LispParser.Fun_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LispParser#macro_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacro_definition(LispParser.Macro_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LispParser#form}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForm(LispParser.FormContext ctx);
	/**
	 * Visit a parse tree produced by {@link LispParser#lambda_form}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLambda_form(LispParser.Lambda_formContext ctx);
	/**
	 * Visit a parse tree produced by {@link LispParser#let_form}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLet_form(LispParser.Let_formContext ctx);
	/**
	 * Visit a parse tree produced by {@link LispParser#simple_form}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_form(LispParser.Simple_formContext ctx);
	/**
	 * Visit a parse tree produced by {@link LispParser#decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecl(LispParser.DeclContext ctx);
}