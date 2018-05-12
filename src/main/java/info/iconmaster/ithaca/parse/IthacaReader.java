package info.iconmaster.ithaca.parse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import info.iconmaster.ithaca.eval.IthacaThread;
import info.iconmaster.ithaca.eval.Scope;
import info.iconmaster.ithaca.object.IthacaNull;
import info.iconmaster.ithaca.object.IthacaNumber;
import info.iconmaster.ithaca.object.IthacaObject;
import info.iconmaster.ithaca.object.IthacaPair;
import info.iconmaster.ithaca.object.IthacaString;
import info.iconmaster.ithaca.object.IthacaSymbol;
import info.iconmaster.ithaca.parse.Token.Type;
import info.iconmaster.ithaca.util.ListUtils;

public class IthacaReader {
	public IthacaThread thread;
	public Scope scope;
	public TokenStream tokenStream;
	
	public IthacaReader(TokenStream ts, IthacaThread thread, Scope scope) {
		super();
		this.tokenStream = ts;
		this.thread = thread;
		this.scope = scope;
	}
	
	public IthacaReader(TokenStream ts, IthacaThread thread) {
		super();
		this.tokenStream = ts;
		this.thread = thread;
		this.scope = thread.scope();
	}

	private IthacaObject read(Token t) throws IOException {
		if (t == null) return null;
		
		switch (t.type) {
		case COMMENT:
		case WHITESPACE:
			return read();
		case WORD:
			return IthacaSymbol.intern(t.value);
		case STRING:
			return new IthacaString(t.value);
		case NUMBER:
			return new IthacaNumber(new BigDecimal(t.value));
		case L_PAREN:
			IthacaObject result = IthacaNull.NULL;
			IthacaPair last = null;
			boolean hadDot = false;
			
			while (true) {
				t = tokenStream.next();
				if (t == null) {
					throw new IOException("Unexpected EOF while constructing list");
				} else if (t.type == Type.WHITESPACE || t.type == Type.COMMENT) {
					continue;
				} else if (t.type == Type.R_PAREN) {
					return result;
				} else if (hadDot) {
					throw new IOException("Illegal dotted list form");
				} else if (t.type == Type.DOT) {
					IthacaObject next = read(tokenStream.next());
					if (next == null) throw new IOException("Unexpected EOF while constructing list");
					
					if (last == null) {
						throw new IOException("Illegal dotted list form");
					} else {
						last.tail = next;
						hadDot = true;
					}
				} else {
					IthacaObject next = read(t);
					if (next == null) throw new IOException("Unexpected EOF while constructing list");
					
					if (last == null) {
						result = last = new IthacaPair(next, IthacaNull.NULL);
					} else {
						IthacaPair pair = new IthacaPair(next, IthacaNull.NULL);
						last.tail = pair;
						last = pair;
					}
				}
			}
		case QUOTE:
			return ListUtils.wrapList(IthacaSymbol.intern("quote"), read());
		case QUASIQUOTE:
			return ListUtils.wrapList(IthacaSymbol.intern("quasiquote"), read());
		case UNQUOTE:
			return ListUtils.wrapList(IthacaSymbol.intern("unquote"), read());
		case UNQUOTE_SPLICING:
			return ListUtils.wrapList(IthacaSymbol.intern("unquote-splicing"), read());
		case READ_SYNTAX:
			Token identifier = tokenStream.next();
			if (identifier == null) throw new IOException("Unexpected EOF while parsing read syntax");
			ReadSyntax rs = scope.getReadSyntax(identifier.value);
			if (rs == null) throw new IOException("Unknown read syntax identifier #"+identifier.value);
			return rs.read(this);
		default:
			throw new IOException("Unexpected token type "+t.type+": "+t.value);
		}
	}
	
	public IthacaObject read() throws IOException {
		return read(tokenStream.next());
	}
	
	public List<IthacaObject> readAll() throws IOException {
		IthacaObject o;
		List<IthacaObject> result = new ArrayList<>();
		while ((o = read()) != null) {
			result.add(o);
		}
		return result;
	}
}
