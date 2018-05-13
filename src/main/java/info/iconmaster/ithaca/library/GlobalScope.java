package info.iconmaster.ithaca.library;

import info.iconmaster.ithaca.eval.Scope;
import info.iconmaster.ithaca.object.IthacaBool;
import info.iconmaster.ithaca.object.IthacaObject;
import info.iconmaster.ithaca.object.IthacaSymbol;
import info.iconmaster.ithaca.object.IthacaVoid;
import info.iconmaster.ithaca.parse.IthacaReader;
import info.iconmaster.ithaca.parse.ReadSyntax;

public class GlobalScope extends Scope {
	public GlobalScope() {
		// functions
		defineBinding(IthacaSymbol.intern("pair"), new FuncPair());
		defineBinding(IthacaSymbol.intern("head"), new FuncHead());
		defineBinding(IthacaSymbol.intern("tail"), new FuncTail());
		defineBinding(IthacaSymbol.intern("print"), new FuncPrint());
		
		// macros
		defineBinding(IthacaSymbol.intern("begin"), new MacroBegin());
		defineBinding(IthacaSymbol.intern("define"), new MacroDefine());
		defineBinding(IthacaSymbol.intern("func"), new MacroFunc());
		defineBinding(IthacaSymbol.intern("macro"), new MacroMacro());
		defineBinding(IthacaSymbol.intern("quote"), new MacroQuote());
		defineBinding(IthacaSymbol.intern("let"), new MacroLet());
		defineBinding(IthacaSymbol.intern("begin/return"), new MacroBeginReturn());
		defineBinding(IthacaSymbol.intern("if"), new MacroIf());
		
		// read syntax
		defineReadSyntax(new ReadSyntax("t") {
			@Override
			public IthacaObject read(IthacaReader ir) {
				return IthacaBool.TRUE;
			}
		});
		
		defineReadSyntax(new ReadSyntax("f") {
			@Override
			public IthacaObject read(IthacaReader ir) {
				return IthacaBool.FALSE;
			}
		});
		
		defineReadSyntax(new ReadSyntax("v") {
			@Override
			public IthacaObject read(IthacaReader ir) {
				return IthacaVoid.VOID;
			}
		});
	}
}
