package info.iconmaster.ithaca.eval;

import java.util.ArrayList;
import java.util.List;

import info.iconmaster.ithaca.object.IthacaObject;
import info.iconmaster.ithaca.object.IthacaVoid;

/**
 * This StackFrame evaluates a list of expressions, returning the last one executed to its caller.
 * @author iconmaster
 *
 */
public class EvalBodyStackFrame extends StackFrame {
	public List<IthacaObject> toEval = new ArrayList<>();
	public boolean emptyBody = false;

	public EvalBodyStackFrame(IthacaThread thread, Scope scope, List<IthacaObject> body) {
		super(thread, scope);
		toEval.addAll(body);
		if (toEval.isEmpty()) emptyBody = true;
	}

	@Override
	public void step() {
		if (emptyBody) {
			// body was empty; return void
			thread.frames.pop();
			thread.recieved = IthacaVoid.VOID;
		} else if (toEval.isEmpty()) {
			// let thread.recieved pass through to our caller
			thread.frames.pop();
		} else {
			// discard thread.recieved and run the next expression
			thread.recieved = null;
			IthacaObject next = toEval.remove(0);
			thread.frames.push(new EvalStackFrame(thread, scope, next));
		}
	}

	@Override
	public StackFrame clone(IthacaThread thread) {
		return new EvalBodyStackFrame(thread, scope, toEval);
	}
}
