package info.iconmaster.ithaca.library;

import info.iconmaster.ithaca.eval.Scope;
import info.iconmaster.ithaca.object.IthacaSymbol;

public class GlobalScope extends Scope {
	public GlobalScope() {
		defineBinding(IthacaSymbol.intern("pair"), new FuncPair());
		defineBinding(IthacaSymbol.intern("head"), new FuncHead());
		defineBinding(IthacaSymbol.intern("tail"), new FuncTail());
		defineBinding(IthacaSymbol.intern("print"), new FuncPrint());
		
		defineBinding(IthacaSymbol.intern("begin"), new MacroBegin());
		defineBinding(IthacaSymbol.intern("define"), new MacroDefine());
		defineBinding(IthacaSymbol.intern("func"), new MacroFunc());
		defineBinding(IthacaSymbol.intern("macro"), new MacroMacro());
		defineBinding(IthacaSymbol.intern("quote"), new MacroQuote());
		defineBinding(IthacaSymbol.intern("let"), new MacroLet());
		defineBinding(IthacaSymbol.intern("begin/return"), new MacroBeginReturn());
	}
}
