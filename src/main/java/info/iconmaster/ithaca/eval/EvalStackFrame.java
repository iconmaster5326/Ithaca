package info.iconmaster.ithaca.eval;

import info.iconmaster.ithaca.object.IthacaObject;
import info.iconmaster.ithaca.object.IthacaPair;
import info.iconmaster.ithaca.object.IthacaSymbol;

public class EvalStackFrame extends StackFrame {
	public IthacaObject form;
	
	public EvalStackFrame(IthacaThread thread, IthacaObject form) {
		super(thread);
		this.form = form;
	}
	
	public EvalStackFrame(IthacaThread thread, Scope scope, IthacaObject form) {
		super(thread, scope);
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
			
			thread.frames.pop();
			procedure.call(thread, argList);
		} else if (form instanceof IthacaSymbol) {
			// look it up in the scope
			IthacaObject value = scope.getBinding((IthacaSymbol)form);
			if (value == null) {
				throw new IllegalArgumentException("Unbound symbol: "+form);
			}
			
			thread.recieved = value;
			thread.frames.pop();
		} else {
			thread.frames.pop();
			thread.recieved = form;
		}
	}
}
