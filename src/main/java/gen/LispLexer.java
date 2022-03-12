// Generated from /home/charlie/GitHub/lisp-to-c/src/main/antlr4/Lisp.g4 by ANTLR 4.9.2
package gen;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LispLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, IDENTIFIER=8, 
		PLUS=9, MINUS=10, MULT=11, DIV=12, LOW=13, EQ=14, ORDER=15, OP=16, CP=17, 
		OSP=18, CSP=19, STRING=20, HEADER=21, NUMBER=22, WHITESPACE=23, DIGIT=24, 
		LETTER=25, LOWER=26, UPPER=27;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "IDENTIFIER", 
			"PLUS", "MINUS", "MULT", "DIV", "LOW", "EQ", "ORDER", "OP", "CP", "OSP", 
			"CSP", "STRING", "HEADER", "NUMBER", "WHITESPACE", "DIGIT", "LETTER", 
			"LOWER", "UPPER"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'include'", "'defn'", "'DEFN'", "'fn'", "'FN'", "'let'", "'LET'", 
			null, "'+'", "'-'", "'*'", "'/'", "'_'", "'='", null, "'('", "')'", "'['", 
			"']'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, "IDENTIFIER", "PLUS", 
			"MINUS", "MULT", "DIV", "LOW", "EQ", "ORDER", "OP", "CP", "OSP", "CSP", 
			"STRING", "HEADER", "NUMBER", "WHITESPACE", "DIGIT", "LETTER", "LOWER", 
			"UPPER"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public LispLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Lisp.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\35\u00b0\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\6\3\7\3\7"+
		"\3\7\3\7\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\7\t_\n\t\f\t\16\tb\13\t\3"+
		"\t\3\t\3\t\3\t\3\t\3\t\3\t\5\tk\n\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r"+
		"\3\16\3\16\3\17\3\17\3\20\3\20\3\20\3\20\3\20\5\20~\n\20\3\21\3\21\3\22"+
		"\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\25\6\25\u008b\n\25\r\25\16\25\u008c"+
		"\3\25\3\25\3\26\3\26\3\26\3\26\6\26\u0095\n\26\r\26\16\26\u0096\3\26\3"+
		"\26\3\27\6\27\u009c\n\27\r\27\16\27\u009d\3\30\6\30\u00a1\n\30\r\30\16"+
		"\30\u00a2\3\30\3\30\3\31\3\31\3\32\3\32\5\32\u00ab\n\32\3\33\3\33\3\34"+
		"\3\34\2\2\35\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16"+
		"\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34"+
		"\67\35\3\2\4\4\2>>@@\5\2\13\f\17\17\"\"\2\u00c4\2\3\3\2\2\2\2\5\3\2\2"+
		"\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21"+
		"\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2"+
		"\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3"+
		"\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3"+
		"\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\39\3\2\2\2\5A\3\2\2\2\7F\3\2\2\2\tK\3"+
		"\2\2\2\13N\3\2\2\2\rQ\3\2\2\2\17U\3\2\2\2\21j\3\2\2\2\23l\3\2\2\2\25n"+
		"\3\2\2\2\27p\3\2\2\2\31r\3\2\2\2\33t\3\2\2\2\35v\3\2\2\2\37}\3\2\2\2!"+
		"\177\3\2\2\2#\u0081\3\2\2\2%\u0083\3\2\2\2\'\u0085\3\2\2\2)\u0087\3\2"+
		"\2\2+\u0090\3\2\2\2-\u009b\3\2\2\2/\u00a0\3\2\2\2\61\u00a6\3\2\2\2\63"+
		"\u00aa\3\2\2\2\65\u00ac\3\2\2\2\67\u00ae\3\2\2\29:\7k\2\2:;\7p\2\2;<\7"+
		"e\2\2<=\7n\2\2=>\7w\2\2>?\7f\2\2?@\7g\2\2@\4\3\2\2\2AB\7f\2\2BC\7g\2\2"+
		"CD\7h\2\2DE\7p\2\2E\6\3\2\2\2FG\7F\2\2GH\7G\2\2HI\7H\2\2IJ\7P\2\2J\b\3"+
		"\2\2\2KL\7h\2\2LM\7p\2\2M\n\3\2\2\2NO\7H\2\2OP\7P\2\2P\f\3\2\2\2QR\7n"+
		"\2\2RS\7g\2\2ST\7v\2\2T\16\3\2\2\2UV\7N\2\2VW\7G\2\2WX\7V\2\2X\20\3\2"+
		"\2\2Y`\5\63\32\2Z_\5\63\32\2[_\5\61\31\2\\_\5\33\16\2]_\5\35\17\2^Z\3"+
		"\2\2\2^[\3\2\2\2^\\\3\2\2\2^]\3\2\2\2_b\3\2\2\2`^\3\2\2\2`a\3\2\2\2ak"+
		"\3\2\2\2b`\3\2\2\2ck\5\23\n\2dk\5\25\13\2ek\5\27\f\2fk\5\31\r\2gk\5\35"+
		"\17\2hk\5\33\16\2ik\5\37\20\2jY\3\2\2\2jc\3\2\2\2jd\3\2\2\2je\3\2\2\2"+
		"jf\3\2\2\2jg\3\2\2\2jh\3\2\2\2ji\3\2\2\2k\22\3\2\2\2lm\7-\2\2m\24\3\2"+
		"\2\2no\7/\2\2o\26\3\2\2\2pq\7,\2\2q\30\3\2\2\2rs\7\61\2\2s\32\3\2\2\2"+
		"tu\7a\2\2u\34\3\2\2\2vw\7?\2\2w\36\3\2\2\2x~\t\2\2\2yz\7@\2\2z~\7?\2\2"+
		"{|\7>\2\2|~\7?\2\2}x\3\2\2\2}y\3\2\2\2}{\3\2\2\2~ \3\2\2\2\177\u0080\7"+
		"*\2\2\u0080\"\3\2\2\2\u0081\u0082\7+\2\2\u0082$\3\2\2\2\u0083\u0084\7"+
		"]\2\2\u0084&\3\2\2\2\u0085\u0086\7_\2\2\u0086(\3\2\2\2\u0087\u008a\7$"+
		"\2\2\u0088\u008b\5\63\32\2\u0089\u008b\5\61\31\2\u008a\u0088\3\2\2\2\u008a"+
		"\u0089\3\2\2\2\u008b\u008c\3\2\2\2\u008c\u008a\3\2\2\2\u008c\u008d\3\2"+
		"\2\2\u008d\u008e\3\2\2\2\u008e\u008f\7$\2\2\u008f*\3\2\2\2\u0090\u0094"+
		"\7$\2\2\u0091\u0095\5\63\32\2\u0092\u0095\5\61\31\2\u0093\u0095\7\60\2"+
		"\2\u0094\u0091\3\2\2\2\u0094\u0092\3\2\2\2\u0094\u0093\3\2\2\2\u0095\u0096"+
		"\3\2\2\2\u0096\u0094\3\2\2\2\u0096\u0097\3\2\2\2\u0097\u0098\3\2\2\2\u0098"+
		"\u0099\7$\2\2\u0099,\3\2\2\2\u009a\u009c\5\61\31\2\u009b\u009a\3\2\2\2"+
		"\u009c\u009d\3\2\2\2\u009d\u009b\3\2\2\2\u009d\u009e\3\2\2\2\u009e.\3"+
		"\2\2\2\u009f\u00a1\t\3\2\2\u00a0\u009f\3\2\2\2\u00a1\u00a2\3\2\2\2\u00a2"+
		"\u00a0\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a4\u00a5\b\30"+
		"\2\2\u00a5\60\3\2\2\2\u00a6\u00a7\4\62;\2\u00a7\62\3\2\2\2\u00a8\u00ab"+
		"\5\65\33\2\u00a9\u00ab\5\67\34\2\u00aa\u00a8\3\2\2\2\u00aa\u00a9\3\2\2"+
		"\2\u00ab\64\3\2\2\2\u00ac\u00ad\4c|\2\u00ad\66\3\2\2\2\u00ae\u00af\4C"+
		"\\\2\u00af8\3\2\2\2\16\2^`j}\u008a\u008c\u0094\u0096\u009d\u00a2\u00aa"+
		"\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}