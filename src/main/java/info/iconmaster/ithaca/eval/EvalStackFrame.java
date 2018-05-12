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
		if (thread.recieved == null) {
			if (form instanceof IthacaPair) {
				// eval the head, and then execute it when we get the result
				thread.eval(((IthacaPair)form).head);
			} else if (form instanceof IthacaSymbol) {
				// look it up in the scope
				IthacaObject value = scope.getBinding((IthacaSymbol)form);
				if (value == null) {
					throw new IllegalArgumentException("Unbound symbol: "+form);
				}
				
				thread.recieved = value;
				thread.frames.pop();
			} else {
				// it's a constant, leave it unchanged
				thread.frames.pop();
				thread.recieved = form;
			}
		} else {
			// execute a func/macro
			if (!(thread.recieved instanceof IthacaRunnable)) throw new IllegalArgumentException("Object not callable: "+thread.recieved);
			IthacaRunnable procedure = (IthacaRunnable) thread.recieved;
			IthacaObject argList = ((IthacaPair)form).tail;
			
			thread.recieved = null;
			thread.frames.pop();
			procedure.call(thread, argList);
		}
	}
	
	@Override
	public StackFrame clone(IthacaThread thread) {
		return new EvalStackFrame(thread, scope, form);
	}
}
