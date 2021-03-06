package info.iconmaster.ithaca.object;

import info.iconmaster.ithaca.eval.EvalStackFrame;
import info.iconmaster.ithaca.eval.IthacaRunnable;
import info.iconmaster.ithaca.eval.IthacaThread;

public abstract class IthacaMacro extends IthacaObject implements IthacaRunnable {
	public abstract IthacaObject callMacro(IthacaThread thread, IthacaObject argList);
	
	@Override
	public void call(IthacaThread thread, IthacaObject argList) {
		IthacaObject result = callMacro(thread, argList);
		if (result != null) thread.frames.push(new EvalStackFrame(thread, result));
	}
}
