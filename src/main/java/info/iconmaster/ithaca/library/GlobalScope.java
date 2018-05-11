package info.iconmaster.ithaca.library;

import info.iconmaster.ithaca.eval.Scope;
import info.iconmaster.ithaca.object.IthacaSymbol;

public class GlobalScope extends Scope {
	public GlobalScope() {
		defineBinding(IthacaSymbol.intern("begin"), new FuncBegin());
		defineBinding(IthacaSymbol.intern("pair"), new FuncPair());
		
		defineBinding(IthacaSymbol.intern("define"), new MacroDefine());
	}
}
