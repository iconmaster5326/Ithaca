package info.iconmaster.ithaca.object;

import java.util.ArrayList;
import java.util.List;

import info.iconmaster.ithaca.eval.IthacaRunnable;
import info.iconmaster.ithaca.eval.IthacaThread;
import info.iconmaster.ithaca.eval.StackFrame;
import info.iconmaster.ithaca.util.ListUtils;

public abstract class IthacaFunc extends IthacaObject implements IthacaRunnable {
	public class Frame extends StackFrame {
		IthacaObject toEval;
		List<IthacaObject> doneEval = new ArrayList<>();
		
		public Frame(IthacaThread thread, IthacaObject toEval) {
			super(thread);
			this.toEval = toEval;
		}
		
		@Override
		public void step() {
			if (thread.recieved != null) {
				doneEval.add(thread.recieved);
				thread.recieved = null;
			}
			
			if (toEval instanceof IthacaPair) {
				thread.eval(((IthacaPair) toEval).head);
				toEval = ((IthacaPair) toEval).tail;
			} else {
				doneEval.add(toEval);
				thread.frames.pop();
				thread.recieved = callFunc(thread, ListUtils.wrapDottedList(doneEval));
			}
		}
	}
	
	public abstract IthacaObject callFunc(IthacaThread thread, IthacaObject argList);
	
	@Override
	public void call(IthacaThread thread, IthacaObject argList) {
		thread.frames.push(new Frame(thread, argList));
	}
}
