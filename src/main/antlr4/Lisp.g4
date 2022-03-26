grammar Lisp;

//TODO: macros

/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/
program : top_level_from* EOF ;

top_level_from: form |
                fun_definition |
                macro_definition |
                include;

include: OP 'include' HEADER CP; // debug

fun_definition: OP ('defn' | 'DEFN') IDENTIFIER decl form CP;

/* (defmacro example [x y] (+ x y))
 *
 * (example 2 3) --before-translation--> ((fn [x y] (+ x y)) 2 3) --translation--> ...
 */
macro_definition: OP ('defmacro' | 'DEFMACRO') IDENTIFIER decl '`' form CP;

form: (OP (lambda_form | let_form |
          simple_form) CP) | IDENTIFIER | STRING | NUMBER;

lambda_form: ('fn' | 'FN') decl form;
let_form: ('let' | 'LET') OSP IDENTIFIER form CSP form; // do we really want to have simple form here?
simple_form: (form)+;

decl: OSP IDENTIFIER* CSP;

/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/
IDENTIFIER : ((LETTER (LETTER | DIGIT | LOW | EQ)*) | PLUS | MINUS | MULT | DIV | EQ | LOW | ORDER) ;

PLUS : '+';
MINUS : '-';
MULT : '*';
DIV : '/';
LOW : '_';
EQ : '=';
ORDER : '>' | '<' | '>=' |'<=';

OP : '(';
CP : ')';
OSP : '[';
CSP : ']';

STRING : '"' (LETTER | DIGIT)+ '"';
HEADER : '"' (LETTER | DIGIT | '.') + '"';

NUMBER : (DIGIT)+ ;

WHITESPACE : [ \r\n\t] + -> channel (HIDDEN);

DIGIT : '0'..'9';

LETTER : LOWER | UPPER ;

LOWER : ('a'..'z') ;
UPPER : ('A'..'Z') ;