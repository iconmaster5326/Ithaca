package info.iconmaster.ithaca.eval;

import info.iconmaster.ithaca.object.IthacaObject;
import info.iconmaster.ithaca.object.IthacaPair;

public class EvalStackFrame extends StackFrame {
	public IthacaObject form;
	
	public EvalStackFrame(IthacaThread thread, IthacaObject form) {
		super(thread);
		this.form = form;
	}

	@Override
	public void step() {
		if (form instanceof IthacaPair) {
			IthacaPair pair = (IthacaPair) form;
			IthacaRunnable procedure;
			IthacaObject argList = pair.tail;
			
			if (pair.head instanceof IthacaRunnable) {
				procedure = (IthacaRunnable) pair.head;
			} else {
				throw new IllegalArgumentException("Object not callable: "+pair.head);
			}
			
			procedure.call(thread, argList);
		} else {
			thread.recieved = form;
		}
	}
}
