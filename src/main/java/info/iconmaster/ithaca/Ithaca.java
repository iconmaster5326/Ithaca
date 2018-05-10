package info.iconmaster.ithaca;

import java.io.IOException;

import info.iconmaster.ithaca.eval.Scope;
import info.iconmaster.ithaca.eval.IthacaThread;
import info.iconmaster.ithaca.library.FuncBegin;
import info.iconmaster.ithaca.library.MacroDefine;
import info.iconmaster.ithaca.object.IthacaString;
import info.iconmaster.ithaca.object.IthacaSymbol;
import info.iconmaster.ithaca.util.ListUtils;

public class Ithaca {
	public static void main(String[] args) throws IOException {
		IthacaThread thread = new IthacaThread(
				ListUtils.wrapList(new FuncBegin(),
						ListUtils.wrapList(new MacroDefine(),
								IthacaSymbol.intern("x"),
								new IthacaString("hello")
						),
						IthacaSymbol.intern("x")
				)
		, new Scope());
		System.out.println(thread.run());
	}
}
