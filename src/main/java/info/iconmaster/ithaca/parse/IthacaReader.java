package info.iconmaster.ithaca.parse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import info.iconmaster.ithaca.object.IthacaNull;
import info.iconmaster.ithaca.object.IthacaNumber;
import info.iconmaster.ithaca.object.IthacaObject;
import info.iconmaster.ithaca.object.IthacaPair;
import info.iconmaster.ithaca.object.IthacaString;
import info.iconmaster.ithaca.object.IthacaSymbol;
import info.iconmaster.ithaca.parse.Token.Type;

public class IthacaReader {
	private IthacaReader() {}
	
	private static IthacaObject read(Token t, TokenStream ts) throws IOException {
		if (t == null) return null;
		
		switch (t.type) {
		case COMMENT:
		case WHITESPACE:
			return read(ts.next(), ts);
		case WORD:
			return IthacaSymbol.intern(t.value);
		case STRING:
			return new IthacaString(t.value);
		case NUMBER:
			return new IthacaNumber(new BigDecimal(t.value));
		case L_PAREN:
			IthacaObject result = null;
			IthacaObject last = IthacaNull.NULL;
			
			while (true) {
				t = ts.next();
				if (t == null) {
					throw new IOException("Unexpected EOF while constructing list");
				} else if (t.type == Type.R_PAREN) {
					return result;
				} else {
					IthacaObject next = read(t, ts);
					if (next == null) throw new IOException("Unexpected EOF while constructing list");
					last = new IthacaPair(next, last);
					if (result == null) result = last;
				}
			}
		default:
			throw new IOException("Unexpected token type "+t.type+": "+t.value);
		}
	}
	
	public static IthacaObject read(TokenStream ts) throws IOException {
		return read(ts.next(), ts);
	}
	
	public static List<IthacaObject> readAll(TokenStream ts) throws IOException {
		IthacaObject o;
		List<IthacaObject> result = new ArrayList<>();
		while ((o = read(ts)) != null) {
			result.add(o);
		}
		return result;
	}
}
