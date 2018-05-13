package info.iconmaster.ithaca.library.core;

import info.iconmaster.ithaca.eval.EvalBodyStackFrame;
import info.iconmaster.ithaca.eval.IthacaThread;
import info.iconmaster.ithaca.object.IthacaMacro;
import info.iconmaster.ithaca.object.IthacaObject;
import info.iconmaster.ithaca.util.ListUtils;

public class MacroBegin extends IthacaMacro {
	@Override
	public IthacaObject callMacro(IthacaThread thread, IthacaObject argList) {
		thread.frames.push(new EvalBodyStackFrame(thread, thread.scope(), ListUtils.unwrapProperList(argList)));
		return null;
	}
}
