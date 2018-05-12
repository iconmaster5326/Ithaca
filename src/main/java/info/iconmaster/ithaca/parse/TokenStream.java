package info.iconmaster.ithaca.parse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import info.iconmaster.ithaca.parse.Token.Type;

public class TokenStream {
	InputStreamReader isr;
	
	public TokenStream(InputStream in) {
		isr = new InputStreamReader(in, StandardCharsets.UTF_8);
	}
	
	public TokenStream(InputStreamReader isr) {
		this.isr = isr;
	}
	
	public TokenStream(String s) {
		this.isr = new InputStreamReader(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)));
	}
	
	/**
	 * Reads a UTF-8 codepoint from a stream.
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public Integer readCodepoint(Reader in) throws IOException {
		String result;
		
		int i16 = in.read(); // UTF-16 as int
		if (i16 == -1) {
			return null;
		}
		
		char c16 = (char) i16; // UTF-16
		if (Character.isHighSurrogate(c16)) {
			int low_i16 = in.read(); // low surrogate UTF-16 as int
			if (low_i16 == -1)
				throw new IOException("EOF before UTF-8 codepoint was completed");
			char low_c16 = (char) low_i16;
			int codepoint = Character.toCodePoint(c16, low_c16);
			result = new String(Character.toChars(codepoint));
		} else {
			result = Character.toString(c16);
		}
		
		return result.codePointAt(0);
	}
	
	private String utf8ToString(int c) {
		return new String(Character.toChars(c));
	}
	
	private Token token = null;
	private boolean stringMode, stringEscapeMode, commentMode;
	private static final Map<Integer, Type> singleCharTypes = new HashMap<Integer, Type>() {{
		put((int) '(', Type.L_PAREN);
		put((int) ')', Type.R_PAREN);
		put((int) '#', Type.READ_SYNTAX);
		put((int) '\'', Type.QUOTE);
		put((int) '`', Type.QUASIQUOTE);
	}};
	
	private Token addCharToToken(int c) {
		if (commentMode) {
			if (c == '\n') {
				commentMode = false;
				
				Token result = token;
				token = null;
				return result;
			} else {
				token.append(utf8ToString(c));
				return null;
			}
		} else if (stringEscapeMode) {
			switch (c) {
				case 'n': token.append("\n"); break;
				case 't': token.append("\t"); break;
				case 'r': token.append("\r"); break;
				case 'b': token.append("\b"); break;
				case 'f': token.append("\f"); break;
				default: token.append(utf8ToString(c));
			}
			
			stringEscapeMode = false;
			return null;
		} else if (stringMode) {
			if (c == '"') {
				stringMode = false;
				
				Token result = token;
				token = null;
				return result;
			} else if (c == '\\') {
				stringEscapeMode = true;
				return null;
			} else {
				token.append(utf8ToString(c));
				return null;
			}
		} else if (c == ';') {
			commentMode = true;
			
			Token result = token;
			token = new Token(Type.COMMENT, "");
			return result;
		} else if (c == ',') {
			Token result = token;
			token = new Token(Type.UNQUOTE, ",");
			return result;
		} else if (singleCharTypes.containsKey(c)) {
			// ( ) # ` '
			Type type = singleCharTypes.get(c);
			Token result = token;
			token = new Token(type, utf8ToString(c));
			return result;
		} else if (c == '"') {
			// STRING
			stringMode = true;
			
			Token result = token;
			token = new Token(Type.STRING, "");
			return result;
		} else if (Character.isWhitespace(c)) {
			// WHITESPACE
			if (token == null || token.type != Type.WHITESPACE) {
				Token result = token;
				token = new Token(Type.WHITESPACE, utf8ToString(c));
				return result;
			} else {
				token.append(utf8ToString(c));
				return null;
			}
		} else {
			// WORD
			if (c == '@' && token != null && token.type == Type.UNQUOTE) {
				token.type = Type.UNQUOTE_SPLICING;
				token.value = ",@";
				
				Token result = token;
				token = null;
				return result;
			} else if (token == null || token.type != Type.WORD) {
				Token result = token;
				token = new Token(Type.WORD, utf8ToString(c));
				return result;
			} else {
				token.append(utf8ToString(c));
				return null;
			}
		}
	}
	
	private Token finish() throws IOException {
		if (stringMode) {
			throw new IOException("Unexpected EOF while reading string");
		} else if (token == null) {
			return null;
		} else {
			Token result = token;
			token = null;
			return result.check();
		}
	}
	
	public Token next() throws IOException {
		Integer codepoint = readCodepoint(isr);
		if (codepoint == null) return finish();
		
		Token result;
		while ((result = addCharToToken(codepoint)) == null) {
			codepoint = readCodepoint(isr);
			if (codepoint == null) return finish();
		}
		
		return result.check();
	}
}
