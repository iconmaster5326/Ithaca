package info.iconmaster.ithaca.library;

import java.util.List;

import info.iconmaster.ithaca.eval.IthacaThread;
import info.iconmaster.ithaca.eval.StackFrame;
import info.iconmaster.ithaca.object.IthacaMacro;
import info.iconmaster.ithaca.object.IthacaObject;
import info.iconmaster.ithaca.object.IthacaSymbol;
import info.iconmaster.ithaca.object.IthacaVoid;
import info.iconmaster.ithaca.util.ListUtils;

public class MacroDefine extends IthacaMacro {

	@Override
	public IthacaObject callMacro(IthacaThread thread, IthacaObject argList) {
		List<IthacaObject> args = ListUtils.unwrapProperList(argList);
		
		thread.frames.push(new StackFrame(thread) {
			@Override
			public void step() {
				thread.scope().defineBinding((IthacaSymbol) args.get(0), thread.recieved);
				thread.recieved = IthacaVoid.VOID;
				thread.frames.pop();
			}
		});
		
		thread.eval(args.get(1));
		return null;
	}
}
