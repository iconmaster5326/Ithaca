package info.iconmaster.ithaca.parse;

public class Token {
	public static enum Type {
		WHITESPACE,
		COMMENT,
		WORD,
		NUMBER,
		STRING,
		L_PAREN,
		R_PAREN,
		DOT,
		READ_SYNTAX,
		QUOTE,
		QUASIQUOTE,
		UNQUOTE,
		UNQUOTE_SPLICING,
	}
	
	public Type type;
	public String value;
	
	public Token(Type type, String value) {
		super();
		this.type = type;
		this.value = value;
	}
	
	public void append(String s) {
		value += s;
	}
	
	public Token check() {
		switch (type) {
		case WORD:
			if (value.equals(".")) {
				type = Type.DOT;
			} else if (value.matches("^\\-?(\\d+|\\d*\\.\\d+|\\d+\\.\\d*)(e[+-]?\\d+)?$")) {
				type = Type.NUMBER;
			}
			break;
		}
		
		return this;
	}
}
